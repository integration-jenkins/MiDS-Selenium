package com.automation.testing.traffic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.BulkUploadWrapper;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.TrafficServiceName;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.TrafficShiftingService;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.MapperJsonObjectUtile;
import com.automation.testing.utile.ProcessCalculationUtile;
import com.automation.testing.validation.TrafficShiftingValidation;

@Component
public class TrafficShiftingBulkUploadTest {

	private static final Logger log = LoggerFactory.getLogger(TrafficShiftingBulkUploadTest.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final UserService userService;
	private final DriverConfig driverConfig;
	private final CreateResultSheet createResultSheet;
	private final TrafficShiftingService trafficShiftingService;
	private final BulkUploadService bulkUploadService;

	public TrafficShiftingBulkUploadTest(WebDriver driver, WebDriverWait wait, UserService userService,
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
	private List<BulkUploadError> errorList = new ArrayList<>();
	private BulkUploadWrapper bulkWrapper;
	private List<String> result = new ArrayList<>();

	private Map<TrafficServiceName, Map<String, String>> attributeMap = TrafficShiftingValidation
			.getBulkUploadValidation();
	private final String BASE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\ts_test_sheet\\";
	private String circle = "DEL";
	private final Map<TrafficServiceName, String> testMap = Map.of(TrafficServiceName.BASIC_TS_VALIDATION,
			"BASIC_VAL.xlsx", TrafficServiceName.DNC_VALIDATIONS, "DCN_TS_VAL.xlsx",
			TrafficServiceName.TS_2G_VALIDATIONS, "2G_TS_VAL.xlsx", TrafficServiceName.TS_4G_VALIDATIONS,
			"4G_TS_VAL.xlsx", TrafficServiceName.TS_5G_VALIDATIONS, "5G_TS_VAL.xlsx");

	public void run() {

		try {
			final String user = Department.CIRCLE_MW_PLANNER.getName();

			runStep(TrafficServiceName.USER_LOGIN, () -> userService.userLogin(user), "User Login",
					"Failed to User Login", null, null, user);

			runStep(TrafficServiceName.TRAFFIC_UPLOAD, () -> trafficShiftingService.openTsUpload(user),
					"Navigate To Traffic Upload", "Failed to Navigate Traffic Upload", null, null, user);

			for (Map.Entry<TrafficServiceName, String> test : testMap.entrySet()) {
				runStep(test.getKey(), () -> {
					boolean b = true;
					String file_path = BASE_PATH + test.getValue();
					String error_path = BASE_PATH + test.getValue().replace("xlsx", "json");
					try {

						b = bulkUploadService.uploadSheetWindow(circle, file_path, errorList);
						result.clear();
						result.addAll(TrafficShiftingValidation.verifyTsUpload(errorList, error_path));
						log.info("Result = " + result);

						driverConfig.refreshDriver(driver);

						return b;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}, "Plan Upload Successfully", "Failed to Upload Plan", errorList, result, user);
				errorList.clear();
			}

			userService.logOut(driver, wait);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			createResultSheet.createResultBulkUpload("TS_BULK_TEST", data);
		}

	}

	private boolean runStep(TrafficServiceName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, List<String> result, String user) {

		process.start(name.name());
		boolean b = step.get(); // upload executed
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		String time = process.getExecutionTime(name.name());
		log.info("Step Result = " + result);
		Map<String, String> map = attributeMap.get(name);
		if (b) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, user, "", ""));
			log.info(name.name() + " -> " + time + " PASSED");
			return true;
		}

		if (errors == null || errors.isEmpty()) {
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, user, "", ""));
			log.info(name.name() + " -> " + time + " FAILED (No Error Details)");
			return false;
		}

		Map<String, String> errorMap = toErrorMap(errors);
		Set<String> failedRows = new HashSet<>();

		if (result != null && !result.isEmpty()) {
			for (String res : result) {
				String row = extractRowNumber(res);
				if (row != null) {
					failedRows.add(row);
					String[] list = map.get(row).split(Pattern.quote("||"));
					data.add(createResultSheet.createResultData(name.name(), time, false, res, user, list[0], list[1]));
				}
			}
		}

		for (Map.Entry<String, String> entry : errorMap.entrySet()) {
			if (!failedRows.contains(entry.getKey())) {
				String[] list = map.get(entry.getKey()).split(Pattern.quote("||"));
				data.add(createResultSheet.createResultData(name.name(), time, true,
						"Row " + entry.getKey() + " → Validation Passed (" + entry.getValue() + ")", user, list[0],
						list[1]));
			}
		}

		log.info(name.name() + " -> " + time + " PARTIAL FAIL");
		return true;
	}

	private static String extractRowNumber(String resultMsg) {
		Pattern pattern = Pattern.compile("row\\s+(\\d+)");
		Matcher matcher = pattern.matcher(resultMsg);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static Map<String, String> toErrorMap(List<BulkUploadError> errorList) {
		Map<String, String> map = new HashMap<>();
		for (BulkUploadError e : errorList) {
			map.put(e.getRowNumber(), e.getErrorMessage());
		}
		return map;
	}

}
