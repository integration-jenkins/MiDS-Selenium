package com.automation.testing.testcase;

import java.util.ArrayList;
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
import com.automation.testing.enums.ProcessName;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.BulkUploadTestService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.NavigateLocatorUtile;
import com.automation.testing.utile.ProcessCalculationUtile;
import com.automation.testing.validation.LbValidation;
import com.automation.testing.validation.TrafficShiftingValidation;

@Component
public class LbBulkUploadTest {

	private static final Logger log = LoggerFactory.getLogger(LbBulkUploadTest.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final UserService userService;
	private final DriverConfig driverConfig;
	private final CreateResultSheet createResultSheet;
	private final BulkUploadService bulkUploadService;

	public LbBulkUploadTest(WebDriver driver, WebDriverWait wait, UserService userService, DriverConfig driverConfig,
			CreateResultSheet createResultSheet, BulkUploadService bulkUploadService) {
		this.driver = driver;
		this.wait = wait;
		this.userService = userService;
		this.driverConfig = driverConfig;
		this.createResultSheet = createResultSheet;
		this.bulkUploadService = bulkUploadService;
	}

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();
	private List<BulkUploadError> errorList = new ArrayList<>();
	private List<String> result = new ArrayList<>();

	private final String BASE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\lb_sheet\\";
	private String circle = "JK";

	private final Map<ProcessName, String> testMap = Map.of(
			ProcessName.LB_ATTRIBUTE_SET_A, "LB_TEST1", ProcessName.LB_ATTRIBUTE_SET_B, "LB_TEST2",
			ProcessName.LB_ATTRIBUTE_SET_C, "LB_TEST3", ProcessName.LB_ATTRIBUTE_SET_D, "LB_TEST4",
			ProcessName.LB_ATTRIBUTE_SET_E, "LB_TEST5", ProcessName.LB_ATTRIBUTE_SET_F, "LB_TEST6",
			ProcessName.LB_ATTRIBUTE_SET_G, "LB_Test7", ProcessName.LB_ATTRIBUTE_SET_CIRCLE, "LB_Test8");

	private Map<ProcessName, Map<String, String>> validation = LbValidation.getLBAttributeTestList();

	public void run() {

		try {
			final String user = Department.CIRCLE_MW_PLANNER.getName();

			// USER LOGIN
			runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(user), "User Login", "Failed to User Login",
					null, null, user);

			// NAVIGATE TO LB UPLOAD PAGE
			runStep(ProcessName.LB_PLAN_UPLOAD, () -> NavigateLocatorUtile.navigateToLbPlanUpload(driver,wait),
					"Navigate To LB Upload Page", "Failed to Navigate LB Upload Page", null, null, user);

			// UPLOAD EVERY LB SHEET & VALIDATE RESULT.
			for (Map.Entry<ProcessName, String> test : testMap.entrySet()) {
				runStep(test.getKey(), () -> {
					boolean b = true;
					String file_path = BASE_PATH + test.getValue() + ".xlsx";
					String error_path = BASE_PATH + test.getValue() + ".json";
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
			createResultSheet.createResultBulkUpload("LB_Bulk_upload", data);
		}

	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, List<String> result, String user) {

		process.start(name.name());
		boolean b = step.get(); // upload executed
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		String time = process.getExecutionTime(name.name());
		String attributeName = "";
		String attributeRemark = "";
		Map<String, String> validationMap = null;
		if (validation.get(name) != null) {
			validationMap = validation.get(name);
		}

		if (b) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, user, attributeName,
					attributeRemark));
			log.info(name.name() + " -> " + time + " PASSED");
			return true;
		}

		if (errors == null || errors.isEmpty()) {
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, user, attributeName,
					attributeRemark));
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
					String key = "ROW " + row;
					attributeName = validationMap.get(key).split(Pattern.quote("|*|"))[0];
					attributeRemark = validationMap.get(key).split(Pattern.quote("|*|"))[1];
					data.add(createResultSheet.createResultData(name.name(), time, false, res, user, attributeName,
							attributeRemark));
				}
			}
		}

		for (Map.Entry<String, String> entry : errorMap.entrySet()) {
			if (!failedRows.contains(entry.getKey())) {
				String key = "ROW " + entry.getKey();
				attributeName = validationMap.get(key).split(Pattern.quote("|*|"))[0];
				attributeRemark = validationMap.get(key).split(Pattern.quote("|*|"))[1];
				data.add(createResultSheet.createResultData(name.name(), time, true,
						"Row " + entry.getKey() + " → Validation Passed (" + entry.getValue() + ")", user,
						attributeName, attributeRemark));
			}
		}

		log.info(name.name() + " -> " + time + " PARTIAL FAIL");
		return false;
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
