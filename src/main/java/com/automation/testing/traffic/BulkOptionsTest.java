package com.automation.testing.traffic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.automation.testing.enums.TrafficServiceName;
import com.automation.testing.enums.TrafficShiftingPath;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.ProcessCalculationUtile;
import com.automation.testing.utile.TsNavigateUtile;
import com.automation.testing.validation.TrafficShiftingValidation;

@Component
public class BulkOptionsTest {

	private static final Logger log = LoggerFactory.getLogger(BulkOptionsTest.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final UserService userService;
	private final DriverConfig driverConfig;
	private final CreateResultSheet createResultSheet;
	private final BulkUploadService bulkUploadService;

	public BulkOptionsTest(WebDriver driver, WebDriverWait wait, UserService userService, DriverConfig driverConfig,
			CreateResultSheet createResultSheet, BulkUploadService bulkUploadService) {
		this.driver = driver;
		this.wait = wait;
		this.userService = userService;
		this.driverConfig = driverConfig;
		this.createResultSheet = createResultSheet;
		this.bulkUploadService = bulkUploadService;
	}

	private static Map<String, List<TrafficServiceName>> userMap = new LinkedHashMap<>();
	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();

	private Map<TrafficServiceName, Map<String, String>> attribute = TrafficShiftingValidation.getBulkOptionTest();

	private final String BASE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\ts_upload_sheet\\";

	private Map<TrafficServiceName, String> mp = Map.of(TrafficServiceName.BULK_ASSIGNMENT, "TS_BULK_ASSIGNMENT",
			TrafficServiceName.BULK_REOPEN, "TS_BULK_REOPEN", TrafficServiceName.PLAN_CANCELLATION, "TS_CANCELLATION",
			TrafficServiceName.PLAN_DELETE, "TS_DELETE", TrafficServiceName.HOLD_RETRIEVE, "TS_HOLD_RETRIEVE",
			TrafficServiceName.HOLD_UPDATE_STATUS, "TS_HOLD_UPLOAD");

	static {
		userMap.put(Department.CIRCLE_MW_PLANNER.getName(),
				Arrays.asList(TrafficServiceName.BULK_ASSIGNMENT, TrafficServiceName.HOLD_RETRIEVE,
						TrafficServiceName.PLAN_DELETE, TrafficServiceName.PLAN_CANCELLATION,
						TrafficServiceName.HOLD_UPDATE_STATUS, TrafficServiceName.BULK_REOPEN));
		userMap.put(Department.CIRCLE_DEPLOYMENT_TEAM.getName(), Arrays.asList(TrafficServiceName.HOLD_RETRIEVE,
				TrafficServiceName.PLAN_CANCELLATION, TrafficServiceName.HOLD_UPDATE_STATUS));
		userMap.put(Department.CIRCLE_OPERATION_TEAM.getName(), Arrays.asList(TrafficServiceName.HOLD_RETRIEVE,
				TrafficServiceName.PLAN_CANCELLATION, TrafficServiceName.HOLD_UPDATE_STATUS));
	}

	public void run() {

		try {
			for (Map.Entry<String, List<TrafficServiceName>> i : userMap.entrySet()) {
				String user = i.getKey();
				List<TrafficServiceName> test = i.getValue();
				Integer idx = 2;

				runStep(TrafficServiceName.USER_LOGIN, () -> userService.userLogin(user), "User Login",
						"Issue in Login", null, user, null);

				runStep(TrafficServiceName.NAVIGATE_TS_TARCK_VIEW, () -> {
					return TsNavigateUtile.navigateToTsPage(driver, wait, user) && TsNavigateUtile
							.navigateToReport(driver, wait, user, TrafficShiftingPath.TRAFFIC_TRACK_VIEW.getPath());
				}, "User able to Navigate TS Track View", "User not able to navigate TS track view", null, user, null);

				for (TrafficServiceName j : test) {
					uploadAndValidate(j, user, idx);
					driverConfig.refreshDriver(driver);
					driverConfig.waitForIdle(driver);
					idx++;
				}
				userService.logOut(driver, wait);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			createResultSheet.createResultBulkUpload("TS_BULK_OPTIONS_TEST", data);
		}

	}

	private boolean uploadAndValidate(TrafficServiceName name, String user, Integer idx) {
		try {

			List<BulkUploadError> error = new ArrayList<>();
			Map<String, String> list = attribute.get(name);
			String path = BASE_PATH + mp.get(name) + ".xlsx";
			runStep(name, () -> {
				boolean b = true;
				b &= TsNavigateUtile.navigateToTrackViewBulkOptions(driver, wait, idx);
				driverConfig.waitForIdle(driver);
				b &= bulkUploadService.uploadSheet(getCircle(name), path, error);
				return b;
			}, "Plan Uploaded", "Issue in Plan Uploaded", error, user, list);

			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	private String getCircle(TrafficServiceName test) {
		switch (test) {
		case BULK_ASSIGNMENT:
		case PLAN_CANCELLATION:
		case HOLD_RETRIEVE:
			return "JK";
		case BULK_REOPEN:
		case HOLD_UPDATE_STATUS:
			return "DEL";

		default:
			return "";
		}
	}

	private void runStep(TrafficServiceName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String dep, Map<String, String> attribute) {

		process.start(name.name());
		boolean result = step.get(); // run the actual process
		process.end(name.name());

		String time = process.getExecutionTime(name.name());
		try {
			if (result && (errors == null || errors.isEmpty())) {
				data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, dep, "", ""));
				log.info(name.name() + " -> " + time + " execute");
			} else {
				if (errors != null && !errors.isEmpty()) {
					// multiple error rows
					for (BulkUploadError err : errors) {
						String fullMsg = "Row " + err.getRowNumber() + " → " + err.getErrorMessage();
						String list[] = attribute.get(err.getRowNumber()).split(Pattern.quote("||"));
						data.add(createResultSheet.createResultData(name.name(), time, true, fullMsg, dep,
								list != null ? list[0] : "", list != null ? list[1] : ""));
					}
				} else {
					// single generic error
					data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, dep, "ALL COLUMNS",
							"FAILED TO PROCESS"));
				}
				log.info(name.name() + " -> " + time + " FAILED");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, dep, "ALL COLUMNS",
					"FAILED TO PROCESS"));
		}
	}

}
