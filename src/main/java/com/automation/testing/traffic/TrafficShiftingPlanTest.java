package com.automation.testing.traffic;

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
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.TrafficServiceName;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.TrafficShiftingService;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class TrafficShiftingPlanTest {

	private static final Logger log = LoggerFactory.getLogger(TrafficShiftingPlanTest.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final UserService userService;
	private final DriverConfig driverConfig;
	private final CreateResultSheet createResultSheet;
	private final TrafficShiftingService trafficShiftingService;
	private final BulkUploadService bulkUploadService;

	public TrafficShiftingPlanTest(WebDriver driver, WebDriverWait wait, UserService userService,
			DriverConfig driverConfig, CreateResultSheet createResultSheet,
			TrafficShiftingService trafficShiftingService, BulkUploadService bulkUploadService) {
		this.driver = driver;
		this.wait = wait;
		this.userService = userService;
		this.driverConfig = driverConfig;
		this.createResultSheet = createResultSheet;
		this.trafficShiftingService = trafficShiftingService;
		this.bulkUploadService = bulkUploadService;
	}

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();
	private Map<String, String> planMap = new HashMap<>();
	private List<BulkUploadError> errorList = new ArrayList<>();

	public void run() {

		String circle = "DEL";
		String filePath = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\ts_test_sheet\\TS_UPLOAD_SHEET.xlsx";
		List<String> idList = Arrays.asList("TEST_HH1", "TEST_HH2", "TEST_HH3");
		int idx = 1;

		String user = Department.CIRCLE_MW_PLANNER.getName();
		try {
			// verify TEST DATA
			runStep(TrafficServiceName.TS_PLAN_VERIFICATION, () -> {
				if (circle != null && !circle.isEmpty() && idList != null && !idList.isEmpty())
					return true;
				else
					return false;
			}, "Test Credentials is Valid", "Invalid Test Credentials", null, user);

			// 1) Planner Login
			runStep(TrafficServiceName.USER_LOGIN, () -> userService.userLogin(Department.CIRCLE_MW_PLANNER.getName()),
					"User able to Login", "Login Failed", null, user);

			// 1.1) Navigate To Upload Page & Upload the sheet
			runStep(TrafficServiceName.TS_SHEET_UPLOAD, () -> {
				boolean b = true;
				b &= trafficShiftingService.openTsUpload(Department.CIRCLE_MW_PLANNER.getName());
				b &= bulkUploadService.uploadSheetWindow(circle, filePath, errorList);
				userService.logOut(driver, wait);
				return b;
			}, "TS Sheet Uploaded", "Issue In TS Bulk Upload Upload", errorList, user);

			// 2.) login By Operation
			user = Department.CIRCLE_OPERATION_TEAM.getName();
			runStep(TrafficServiceName.USER_LOGIN, () -> {
				return userService.userLogin(Department.CIRCLE_OPERATION_TEAM.getName())
						&& trafficShiftingService.openTsTrackPage(Department.CIRCLE_OPERATION_TEAM.getName());
			}, "Naviagte To Traffic Track View Page", "Failed to open Traffic Track View Page", null, user);

			// 2.1) FILL TS STATUS YES OR HOLD.
			for (String ids : idList) {
				final String id = ids;
				if (idx % 3 == 1) { // THIS PLAN TEST BY TS DONE FUNCTIONALITY
					runStep(TrafficServiceName.VERIFY_TS_STATUS_FIELD, () -> {
						return trafficShiftingService.openTsPlan(id) && trafficShiftingService.fillTsStatus("Yes");
					}, id + ": set fill Yes", id + ": Failed to fill Yes", null, user);
				} else if (idx % 2 == 0) { // THIS PLAN TEST BY HOLD & RESOLVE FUNCTIONALITY
					runStep(TrafficServiceName.VERIFY_TS_STATUS_FIELD, () -> {
						boolean b = true;
						try {
							b &= trafficShiftingService.openTsPlan(id);
							b &= trafficShiftingService.fillTsStatus("Hold");
							Thread.sleep(1000);
							b &= trafficShiftingService.fillHoldData(id);
						} catch (Exception e) {
							e.printStackTrace();
							b = false;
						}
						return b;
					}, id + ": set fill Hold", id + ": Failed to fill Hold", null, user);
					planMap.put(id, "HOLD");

				} else { // THIS PLAN TEST BY CLANCEL FUNCTIONALITY
					runStep(TrafficServiceName.VERIFY_TS_STATUS_FIELD, () -> {
						boolean b = true;
						try {
							b &= trafficShiftingService.openTsPlan(id);
							b &= trafficShiftingService.fillTsStatus("Hold");
							Thread.sleep(1000);
							b &= trafficShiftingService.fillHoldData(id);
						} catch (Exception e) {
							e.printStackTrace();
							b = false;
						}
						return b;
					}, id + ": set fill Hold For Cancel the Plan", id + ": Failed to fill Hold For Cancel the Plan",
							null, user);
					planMap.put(id, "CANCEL");
				}
				idx++;
			}

			log.info("Map = " + planMap);
			userService.logOut(driver, wait);

			// 3.1) VERIFY RESOLVE & CANCEL THE PLANS
			user = Department.CIRCLE_MW_PLANNER.getName();
			runStep(TrafficServiceName.USER_LOGIN, () -> {
				return userService.userLogin(Department.CIRCLE_MW_PLANNER.getName())
						&& trafficShiftingService.openTsTrackPage(Department.CIRCLE_MW_PLANNER.getName());
			}, "Naviagte To Traffic Track View Page", "Failed to open Traffic Track View Page", null, user);

			// 3.2) RESOLVE THE PLAN AND CALCELED THE PLANS
			for (Map.Entry<String, String> plan : planMap.entrySet()) {
				String message = "Plan Resolve working";
				String messageCancel = "Plan Cancel working";

				runStep(TrafficServiceName.VERIFY_HOLD_AND_CANCEL, () -> {
					trafficShiftingService.openTsPlan(plan.getKey());
					boolean b = true;
					if (plan.getValue().equals("HOLD")) {
						// resolve it
						b = trafficShiftingService.retriveHold();
					} else {
						// cancel the plan
						b = trafficShiftingService.cancelPlan();
					}
					return b;
				}, plan.getValue().equals("HOLD") ? message : messageCancel,
						plan.getValue().equals("HOLD") ? "Failed to " + message : "Failed to" + messageCancel, null,
						user);
			}

			// TS Complete the plans;
			userService.logOut(driver, wait);

			// 4) FILL TS DONE FOR RESOLVE THE PLAN
			user = Department.CIRCLE_OPERATION_TEAM.getName();
			runStep(TrafficServiceName.USER_LOGIN, () -> {
				return userService.userLogin(Department.CIRCLE_OPERATION_TEAM.getName())
						&& trafficShiftingService.openTsTrackPage(Department.CIRCLE_OPERATION_TEAM.getName());
			}, "Naviagte To Traffic Track View Page", "Failed to open Traffic Track View Page", null, user);

			// 4.1) FILL YES TS'STATUS AND COMPLETE THE PLAN
			for (Map.Entry<String, String> plan : planMap.entrySet()) {
				if (plan.getValue().equals("HOLD")) {
					final String id = plan.getKey();

					runStep(TrafficServiceName.VERIFY_TS_DONE, () -> {
						return trafficShiftingService.openTsPlan(id) && trafficShiftingService.fillTsStatus("Yes");
					}, id + ": set fill Yes", id + ": Failed to fill Yes", null, user);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			createResultSheet.createResult("TS_PLAN_COM", data);
		}
	}

	private boolean runStep(TrafficServiceName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String user) {

		process.start(name.name());
		boolean result = step.get(); // run the actual process
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, user));
			log.info(name.name() + " -> " + time + " execute");
			return true; // continue next step
		} else {
			if (errors != null && !errors.isEmpty()) {
				// multiple error rows
				for (BulkUploadError err : errors) {
					String fullMsg = "Row " + err.getRowNumber() + " → " + err.getErrorMessage();
					data.add(createResultSheet.createResultData(name.name(), time, false, fullMsg, user));
				}
			} else {
				// single generic error
				data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, user));
			}
			log.info(name.name() + " -> " + time + " FAILED");
			finalizeAndStop(errorMsg); // stop flow immediately!
			return false;
		}
	}

	// IF TESTING FAILED THE STOP THE TESTING OR GET ANY TECHNICAL ISSUE.
	private void finalizeAndStop(String message) {
		createResultSheet.createResult("TS_PLAN_COM", data);
		throw new RuntimeException(message);
	}

}
