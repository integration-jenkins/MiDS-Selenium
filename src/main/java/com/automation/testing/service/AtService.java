package com.automation.testing.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.BulkUploadPath;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.DprPlanTrackPath;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.utile.DprPlanCompletionUtile;
import com.automation.testing.utile.MapperJsonObjectUtile;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.NavigateLocatorUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Service
public class AtService {

	private static final Logger log = LoggerFactory.getLogger(AtService.class);

	@Autowired
	private UserService userService;

	@Autowired
	private WebDriver driver;

	@Autowired
	private WebDriverWait wait;

	@Autowired
	private DriverConfig driverConfig;

	@Autowired
	private BulkUploadService bulkUploadService;

	@Autowired
	private DprPlanCompletionService dprPlanCompletionService;

	@Autowired
	private CreateResultSheet createResultSheet;

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();

	private final String SOFT_AT_DATA = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\soft_at_sheet\\SOFT_AT_ID.json";
	private final String BASE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\soft_at_sheet\\";

	public boolean verifySoftAtBulkUpload() {
		boolean b = false;
		int idx = 1;
		try {

			List<String> softAtList = Arrays.asList("CERAGON", "ERICSSON", "HUAWEI", "AVAIT", "NOKIA");
			Map<String, Boolean> softAtMap = new HashMap<>();
			List<ProcessName> equProcessList = Arrays.asList(ProcessName.UPLOAD_CERAGON_SHEET,
					ProcessName.UPLOAD_ERICSSON_SHEET, ProcessName.UPLOAD_HUAWEI_SHEET, ProcessName.UPLOAD_AVAIT_SHEET,
					ProcessName.UPLOAD_NOKIA_SHEET);

			Map<String, String> softATSample = MidsUtile.readSoftAtData(SOFT_AT_DATA);
			runStep(ProcessName.SOFT_AT_BULK_UPLOAD, () -> {

				if (softATSample != null && softATSample.isEmpty()) {
					log.info("Soft AT Sample Data is Missing");
					return false;
				} else {
					return true;
				}
			}, "Sample Data is verified", "Issue in sample data", null, "System Calculation");

			String user = Department.CIRCLE_DINC_PARTNER.getName();

			runStep(ProcessName.OPEN_SOFT_AT_UPLOAD_PAGE, () -> {
				boolean p = true;
				p &= userService.userLogin(Department.CIRCLE_DINC_PARTNER.getName());

				p &= NavigateLocatorUtile.navigateToSoftAtUploadForDINC(driver, wait);
				return p;
			}, "User able To navigate Soft AT Bulk Upload Page",
					"User Not able To navigate Soft AT Bulk Upload Page, there some issue in User Login or Soft At Bulk Upload Page",
					null, user);

			while (idx <= 5) {

				String softAtEqu = softAtList.get(idx - 1);
				String file_path = BASE_PATH + softAtEqu + ".xlsx";
				String circle = softATSample.get(softAtEqu).split("_")[1];
				List<BulkUploadError> errorList = new ArrayList<>();

				String softAtTables = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab["
						+ idx + "]";

				runStep(equProcessList.get(idx - 1), () -> {
					boolean p = true;

					dprPlanCompletionService.clickOnElement(softAtTables);

					p &= bulkUploadService.atSoftATUpload(circle, file_path, errorList);

					softAtMap.put(softAtEqu, p);
					return p;
				}, softAtEqu + " Bulk upload working", softAtEqu + " Failed, There is some Issue", errorList, user);

				driverConfig.refreshDriver(driver);
				idx++;
			}

			runStep(ProcessName.OPEN_PLAN_TRACKING_REPORT,
					() -> NavigateLocatorUtile.navigateToDprPlanTrackDINC(driver, wait), "MW Plan Tracking Report Open",
					"Not able to open MW Plan Tracking Report", null, user);

			raiseSoftAT(user, softATSample, softAtList);

			userService.logOut(driver, wait);

			user = Department.CIRCLE_MS_PARTNER.getName();
			userService.userLogin(user);

			for (String equipmentName : softAtList) {
				if (softAtList != null && softAtMap.get(equipmentName) != null && softAtMap.get(equipmentName)) {
					String id = softATSample.get(equipmentName).split("_")[0];

					runStep(ProcessName.VERIFY_SOFT_AT_DATA, () -> {
						boolean softAt = true;
 						softAt &= openSoftAtPage(id);

 						switch (equipmentName) {
						case "HUAWEI":
							softAt = verifyHauweiData();
							break;

						case "CERAGON":
							softAt = verifyCeragonData();
							break;

						case "ERICSSON":
							softAt = verifyEricssonData();
							break;

						case "AVAIT":
							softAt = verifyAvaitData();
							break;

						case "NOKIA":
							softAt = verifyNokiaData();
							break;
						}

						dprPlanCompletionService.acceptOrRejectSoftAT(softAt);

 						dprPlanCompletionService.backButton();
						return softAt;
					}, equipmentName + " data is verified, AT Accept",
							equipmentName + " some fields are not filled, AT Reject", null, user);
				}
			}

			createResultSheet.createResult("SoftAtBulkUpload", data);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

 	private void raiseSoftAT(String user, Map<String, String> softATSample, List<String> softAtList) {
		try {
			for (String equipment : softAtList) {
				runStep(ProcessName.AT_RAISE, () -> {
					boolean b = true;
					String id = softATSample.get(equipment).split("_")[0];
					log.info("Link id = " + id);
					b &= dprPlanCompletionService.searchDprPlanByLinkId(id);

					b &= dprPlanCompletionService.softAtRaise();

					dprPlanCompletionService.backButton();
					return b;
				}, equipment + " At Raised ", equipment + " At Not Raised", null, user);

			}

		} catch (Exception e) {
 			e.printStackTrace();
		}
	}

 	public void verifyAtRaiseAccept(String samplePath) {
		try {
			Map<String, String> atDataMap = MapperJsonObjectUtile.readJsonByPath(samplePath);

			if (atDataMap == null || atDataMap.isEmpty()) {
				log.info("Sample Deatils is Empty.");
				return;
			}

			String circle = atDataMap.get("circle");
			String raisePath = atDataMap.get("raise_path");
			String acceptPath = atDataMap.get("accept_path");
			String linkId = atDataMap.get("link_id");

			for (int i = 1; i <= 2; i++) {
 				final Integer idx = i;
				String currentUser = idx == 1 ? Department.CIRCLE_MW_PLANNER.getName()
						: Department.CIRCLE_MS_PARTNER.getName();
				runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(currentUser), "User Login ",
						"User Not Login, There is some issue", null, currentUser);

 				runStep(idx == 1 ? ProcessName.OPEN_AT_RAISE_PAGE : ProcessName.OPEN_AT_ACCEPT_PAGE, () -> {
					boolean b = dprPlanCompletionService
							.clickOnElement(BulkUploadPath.MW_PLAN_TRACKING_BULK_UPLOAD_OPTIONS.getPath()) != null;

 					b &= dprPlanCompletionService.clickOnElement(BulkUploadPath.BULK_AT_UPLOAD.getPath()) != null;
					return b;
				}, "Page Open User able to upload sheet ", "Page Not Open, User issue to upload sheet", null,
						currentUser);

 				List<BulkUploadError> errorList = new ArrayList<>();

				runStep(idx == 1 ? ProcessName.AT_RAISE : ProcessName.AT_ACCEPT,
						() -> bulkUploadService.uploadSheet(circle, idx == 1 ? raisePath : acceptPath, errorList),
						idx == 1 ? "At Raise" : "At Accept", idx == 1 ? "Issue In At Raise" : "Issue in At Accept",
						errorList, currentUser);

 				runStep(ProcessName.VERIFY_PLAN_STATUS,
						() -> verifyPlanStatus(idx == 1 ? "PHY-AT RAISED/SOFT-AT RAISED" : "AT ACCEPTED", linkId),
						idx == 1 ? "Plan Status Updated after At Raise" : "Plan Status Updated after At Accept",
						"Plan Status Not updated", null, currentUser);

 				userService.logOut(driver, wait);
			}

			createResultSheet.createResult("AtRaiseAcceptTest", data);

		} catch (Exception e) {
 			e.printStackTrace();
		}
	}

	private boolean verifyPlanStatus(String status, String id) {
		boolean b = true;
		try {
			driverConfig.refreshDriver(driver);
			dprPlanCompletionService.searchDprPlanByLinkId(id);

			String currentStatus = dprPlanCompletionService.getCurrentPlanStatus();
			log.info("current = " + currentStatus);

			return (currentStatus != null && currentStatus.contains(status));

		} catch (Exception e) {
 			e.printStackTrace();
		}
		return b;
	}

	public boolean openSoftAtPage(String id) {
		boolean b = true;
		try {
 			b &= dprPlanCompletionService.searchDprPlanByPlanId(id);

  			dprPlanCompletionService.clickOnElement(DprPlanTrackPath.ACCEPTANCE_TEST_TAB.getPath());
			driverConfig.waitForIdle(driver);

 			b &= NavigateLocatorUtile.openAtTab(driver, wait, 2);
		} catch (Exception e) {
 			e.printStackTrace();
			b = false;
		}
		return b;
	}

 	public boolean verifyHauweiData() {
		boolean b = true;
		try {
			b &= DprPlanCompletionUtile.verifyHauweiFields(driver);

			String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-accordion[2]/vaadin-accordion-panel[2]/span";

 			b &= dprPlanCompletionService.clickOnElement(path) != null;

			b &= DprPlanCompletionUtile.verifyHauweiFieldsGnoc(driver);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean verifyCeragonData() {
		boolean b = true;
		try {

			String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-accordion[2]/vaadin-accordion-panel[2]/span";

 			b &= dprPlanCompletionService.clickOnElement(path) != null;

 			b &= DprPlanCompletionUtile.verifyCeragonCircleData(driver);

			path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-accordion[2]/vaadin-accordion-panel[3]/span";

			b &= dprPlanCompletionService.clickOnElement(path) != null;

			b &= DprPlanCompletionUtile.verifyCeragonGnoc(driver);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean verifyEricssonData() {
		boolean b = true;
		try {

			b &= DprPlanCompletionUtile.verifyEricssonField(driver);
			String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-accordion[2]/vaadin-accordion-panel[2]/span";

			dprPlanCompletionService.clickOnElement(path);

			b &= DprPlanCompletionUtile.verifyEricssonDataFields(driver);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean verifyNokiaData() {
		boolean b = true;
		try {
			String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-accordion[2]/vaadin-accordion-panel[2]/span";

			b &= dprPlanCompletionService.clickOnElement(path) != null;

			b &= DprPlanCompletionUtile.verifeNokiaDataFields(driver);

		} catch (Exception e) {
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	public boolean verifyAvaitData() {
		boolean b = true;
		try {
			String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-accordion[2]/vaadin-accordion-panel[2]/span";

 			b &= dprPlanCompletionService.clickOnElement(path) != null;

 			b &= DprPlanCompletionUtile.verifyAvaitFields(driver);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String dep) {

		process.start(name.name());
		boolean result = step.get();
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, dep));
			log.info(name.name() + " -> " + time + " execute");
			return true;
		} else {
			if (errors != null && !errors.isEmpty()) {
 				for (BulkUploadError err : errors) {
					String fullMsg = "Row " + err.getRowNumber() + " → " + err.getErrorMessage();
					data.add(createResultSheet.createResultData(name.name(), time, false, fullMsg, dep));
				}
			} else {
 				data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, dep));
			}
			log.info(name.name() + " -> " + time + " FAILED");
			return false;
		}
	}

}
