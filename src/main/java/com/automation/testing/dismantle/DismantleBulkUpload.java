package com.automation.testing.dismantle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.DismantleName;
import com.automation.testing.enums.DismantlePath;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.UserService;
import com.automation.testing.sourcecredentials.UserDetails;
import com.automation.testing.utile.DismantleNavigationUtile;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class DismantleBulkUpload {

	private static final Logger log = LoggerFactory.getLogger(DismantleBulkUpload.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final UserService userService;
	private final DriverConfig driverConfig;
	private final CreateResultSheet createResultSheet;
	private final BulkUploadService bulkUploadService;

	public DismantleBulkUpload(WebDriver driver, WebDriverWait wait, UserService userService, DriverConfig driverConfig,
			CreateResultSheet createResultSheet, BulkUploadService bulkUploadService) {
		this.driver = driver;
		this.wait = wait;
		this.userService = userService;
		this.driverConfig = driverConfig;
		this.createResultSheet = createResultSheet;
		this.bulkUploadService = bulkUploadService;
	}

	private List<ProcessData> data = new ArrayList<>();
	private ProcessCalculationUtile process = new ProcessCalculationUtile();

	private static final String BASE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\dismantle\\";
	private static Map<String, List<DismantleName>> testMap = new LinkedHashMap<>();
	private static Map<DismantleName, String> testFileName = new HashMap<>();
	private static Map<String, Map<String, BulkUploadError>> attributeMap;
	private static Map<String, List<String>> bulkMap = Map.of(Department.CIRCLE_MW_PLANNER.getName(),
			Arrays.asList("DISMANTLE_1", "DISMANTLE_2", "DISMANTLE_3"), Department.CIRCLE_OPERATION_TEAM.getName(),
			Arrays.asList("DISMANTLE_OPERATION"), Department.CIRCLE_DEPLOYMENT_TEAM.getName(),
			Arrays.asList("DISMANTLE_DEPLOYMENT"), Department.CIRCLE_DINC_PARTNER.getName(),
			Arrays.asList("DISMANTLE_DINC"));

	static {
		// MAP, USER AND ITS TEST
		testMap.put(Department.CIRCLE_MW_PLANNER.getName(),
				Arrays.asList(DismantleName.DISMANTLE_DELETE, DismantleName.DISMANTLE_BULK_ASSIGNMENT,
						DismantleName.UPLOAD_TS_RELEASE, DismantleName.DISMANTLE_BULK_STATUS_UPDATE));
		testMap.put(Department.CIRCLE_OPERATION_TEAM.getName(), Arrays.asList(DismantleName.DISMANTLE_BULK_ASSIGNMENT));
		testMap.put(Department.CIRCLE_DEPLOYMENT_TEAM.getName(),
				Arrays.asList(DismantleName.DISMANTLE_BULK_REJECTION, DismantleName.DISMANTLE_BULK_ASSIGNMENT));
		testMap.put(Department.CIRCLE_DINC_PARTNER.getName(), Arrays.asList(DismantleName.DISMANTLE_BULK_ASSIGNMENT));

		// MAP, TEST WITH RESPECTIVE SHEET
		testFileName.put(DismantleName.DISMANTLE_BULK_ASSIGNMENT, "DISMANTLE_BULK_ASSIGNMENT");
		testFileName.put(DismantleName.DISMANTLE_DELETE, "DISMANTLE_DELETE");
		testFileName.put(DismantleName.DISMANTLE_BULK_STATUS_UPDATE, "DISMANTLE_PLAN_STATUS_UPDATE");
		testFileName.put(DismantleName.UPLOAD_TS_RELEASE, "DISMANTLE_TS_REL");
		testFileName.put(DismantleName.DISMANTLE_BULK_REJECTION, "DISMANTLE_BULK_REJECT");

		attributeMap = CreateResultSheet.getDismantleValidation(BASE_PATH + "DISMANTLE_VALIDATIONS.csv");
	}

	public void run() {
		try {

			for (String user : UserDetails.getUserDepListForDismantle()) {

				log.info("Validation Map = " + attributeMap);

				// 1. LOGIN BY CURRENT USER
				runStep(DismantleName.USER_LOGIN, () -> userService.userLogin(user), "User Login",
						"Failed to user login", null, user, null);

				// 2. NAVIGATE TO DISMNATLE TRACK PAGE
				runStep(DismantleName.NAVIGATE_TO_DISMANTLE_UPLOAD, () -> {
					boolean b = true;
					b &= DismantleNavigationUtile.openDismantleInfo(driver,wait, user);
					b &= DismantleNavigationUtile.openReport(driver,wait, user, DismantleName.TRACK);
					return b;
				}, "Bulk upload options open", "Failed to open Bulk upload option", null, user, null);

				// 3. OPEN BULK OPTIONS AND UPLOAD AND TEST
				AtomicInteger idx = new AtomicInteger(user.equals(Department.CIRCLE_MW_PLANNER.getName()) ? 3 : 2);
				for (DismantleName test : testMap.get(user)) {
					List<BulkUploadError> error = new ArrayList<>();
					runStep(test, () -> {
						boolean b = true;
						String file_path = BASE_PATH + testFileName.get(test) + ".xlsx";
						b &= DismantleNavigationUtile.openBulkOptions(idx.getAndIncrement(), wait);
						if (test.equals(DismantleName.DISMANTLE_DELETE)) {
							b &= bulkUploadService.uploadFileTabDismantleDelete(file_path, error);
						} else {
							b &= bulkUploadService.uploadSheet(getCircle(test), file_path, error);
						}
						return b;
					}, "Sheet uploaded", "Failed at Bulk Upload", error, user, null);
					driverConfig.refreshDriver(driver);
				}

				runStep(DismantleName.NAVIGATE_TO_DISMANTLE_UPLOAD, () -> {
					return DismantleNavigationUtile.openDismantleInfo(driver,wait, user)
							&& DismantleNavigationUtile.openReport(driver,wait, user, DismantleName.UPLOAD);
				}, "Navigate to Dismantle Upload Page", "Failed to navigate Dismantle Upload Page", null, user, null);

				// 4. VERIFY DISMANTLE BULK UPLOAD
				for (String test : bulkMap.get(user)) {

					List<BulkUploadError> error = new ArrayList<>();

					runStep(DismantleName.BULK_UPLOAD_TEST, () -> {
						boolean b = true;
						String upload_path = BASE_PATH + test + ".xlsx";
						String circle = getCircleByUser(user);

						b &= bulkUploadService.uploadSheetWindow(circle, upload_path, error);

						driverConfig.refreshDriver(driver);
						driverConfig.waitForIdle(driver);

						return b;
					}, "Navigate To Dismantle Bulk Upload Page", "Failed To Navigate", error, user, test);
				}

				userService.logOut(driver, wait);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			createResultSheet.createResultBulkUpload("Dismantle_Bulk_Test", data);
		}
	}

	private String getCircle(DismantleName name) {
		switch (name) {
		case DISMANTLE_BULK_ASSIGNMENT:
			return "JK";
		case UPLOAD_TS_RELEASE:
			return "PB";
		case DISMANTLE_BULK_STATUS_UPDATE:
			return "JK";
		case DISMANTLE_BULK_REJECTION:
			return "UPW";
		default:
			return "";
		}
	}

	private String getCircleByUser(String user) {
		if (user.equals(Department.CIRCLE_MW_PLANNER.getName()))
			return "GUJ";
		else if (user.equals(Department.CIRCLE_DEPLOYMENT_TEAM.getName()))
			return "AP";
		else if (user.equals(Department.CIRCLE_OPERATION_TEAM.getName()))
			return "JK";
		else if (user.equals(Department.CIRCLE_DINC_PARTNER.getName()))
			return "AP";
		return "";
	}

	private void runStep(DismantleName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String dep, String attributeMapName) {

		try {

			process.start(name.name());
			boolean result = step.get(); // run the actual process
			process.end(name.name());

			String time = process.getExecutionTime(name.name());

			if (result && errors == null) {
				data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, dep, "", ""));
				log.info(name.name() + " -> " + time + " execute");
			} else if (errors != null) {
				String key = (attributeMapName != null && !attributeMapName.isEmpty()) ? attributeMapName : name.name();
				Map<String, BulkUploadError> attribute = attributeMap.get(key);

				log.info("Key = " + key);

				if (errors != null && !errors.isEmpty()) {
					// multiple error rows
					for (BulkUploadError err : errors) {
						String fullMsg = "Row " + err.getRowNumber() + " → " + err.getErrorMessage();
						log.info("Message = " + fullMsg);
						String column = attribute != null ? attribute.get(err.getRowNumber()).getRowNumber() : "";
						String validation = attribute != null ? attribute.get(err.getRowNumber()).getErrorMessage()
								: "";

						data.add(createResultSheet.createResultData(name.name(), time, true, fullMsg, dep, column,
								validation));
					}
				} else {
					// single generic error
					for (Map.Entry<String, BulkUploadError> i : attribute.entrySet()) {
						String column = i.getValue().getRowNumber();
						String validation = i.getValue().getErrorMessage();
						data.add(createResultSheet.createResultData(name.name(), time, false,
								"FAILED TO UPLOAD " + name.name(), dep, column, validation));
					}

				}
				log.info(name.name() + " -> " + time + " FAILED");
			} else {
				data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, dep, "STEP VALIDATOPN",
						"FAILED"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
