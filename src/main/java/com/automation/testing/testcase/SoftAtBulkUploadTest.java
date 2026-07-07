package com.automation.testing.testcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.service.AtService;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.BulkUploadTestService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.DprPlanCompletionService;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.NavigateLocatorUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@SuppressWarnings("unused")
@Component
public class SoftAtBulkUploadTest {

	@Autowired
	private UserService userService;

	@Autowired
	private WebDriver driver;

	@Autowired
	private WebDriverWait wait;

	@Autowired
	private CreateResultSheet createResultSheet;

	@Autowired
	private BulkUploadService bulkUploadService;

	@Autowired
	private AtService atService;

	@Autowired
	private DriverConfig driverConfig;

	@Autowired
	private DprPlanCompletionService dprPlanCompletionService;

	private final String user = Department.CIRCLE_DINC_PARTNER.getName();

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();

	List<String> softAtList = Arrays.asList("CERAGON", "ERICSSON", "HUAWEI", "AVAIT", "NOKIA");
	List<ProcessName> equProcessList = Arrays.asList(ProcessName.UPLOAD_CERAGON_SHEET,
			ProcessName.UPLOAD_ERICSSON_SHEET, ProcessName.UPLOAD_HUAWEI_SHEET, ProcessName.UPLOAD_AVAIT_SHEET,
			ProcessName.UPLOAD_NOKIA_SHEET);

	private final String BASE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\soft_at_sheet\\";
	private Map<String, String> idMap = Map.of(
			"CERAGON", "MW-N-JH-06012025-5604",
			"ERICSSON", "MW-N-HP-13072023-30884",
			"HUAWEI", "MW-N-HAR-05012024-5329",
			"AVAIT", "MW-N-JK-15052025-4298",
			"NOKIA", "MW-N-AP-28122025-662");

	public void run() {

		runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(user), "User able To Login", "Failed to login",
				null, user);

		int idx = 1;
		try {

			runStep(ProcessName.OPEN_SOFT_AT_UPLOAD_PAGE, () -> {
				return NavigateLocatorUtile.navigateToSoftAtUploadForDINC(driver, wait);
			}, "User able To navigate Soft AT Bulk Upload Page",
					"User Not able To navigate Soft AT Bulk Upload Page, there some issue in User Login or Soft At Bulk Upload Page",
					null, user);

			while (idx <= 5) {

				try {
					String softAtEqu = softAtList.get(idx - 1);
					String file_path = BASE_PATH + softAtEqu + ".xlsx";
					String circle = idMap.get(softAtEqu).split("-")[2];
					List<BulkUploadError> errorList = new ArrayList<>();

					String softAtTables = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab["
							+ idx + "]";

					runStep(equProcessList.get(idx - 1), () -> {
						boolean p = true;

						dprPlanCompletionService.clickOnElement(softAtTables);

						p &= bulkUploadService.atSoftATUpload(circle, file_path, errorList);

						return p;
					}, softAtEqu + " Bulk upload working", softAtEqu + " Failed, There is some Issue", errorList, user);

					driverConfig.refreshDriver(driver);
				} catch (Exception e) {
					e.printStackTrace();
				}
				idx++;
			}

			verifyAllVendorFieldsOfSoftAt();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			createResultSheet.createResult("SOFT_AT_TEST", data);
		}

	}

	private void verifyAllVendorFieldsOfSoftAt() {

		try {

			NavigateLocatorUtile.navigateToDprPlanTrackDINC(driver, wait);

			for (String equipmentName : softAtList) {

				String id = idMap.get(equipmentName);

				atService.openSoftAtPage(id);

				runStep(ProcessName.VERIFY_SOFT_AT_DATA, () -> {
					boolean softAt = true;

					switch (equipmentName) {
					case "HUAWEI":
						atService.verifyHauweiData();
						break;

					case "CERAGON":
						atService.verifyCeragonData();
						break;

					case "ERICSSON":
						atService.verifyEricssonData();
						break;

					case "AVAIT":
						atService.verifyAvaitData();
						break;

					case "NOKIA":
						atService.verifyNokiaData();
						break;
					}

					dprPlanCompletionService.saveButton();

					driverConfig.refreshDriver(driver);
					
					dprPlanCompletionService.backButton();
					return softAt;
				}, equipmentName + " data is verified, All fields is valid", equipmentName + " some fields are not filled", null,
						user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String dep) {

		process.start(name.name());
		boolean result = step.get();
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, dep));
			System.out.println(name.name() + " -> " + time + " execute");
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
			System.out.println(name.name() + " -> " + time + " FAILED");

			return false;
		}
	}

}
