package com.automation.testing.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.BulkUploadValidationData;
import com.automation.testing.data.BulkUploadWrapper;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.enums.TestType;
import com.automation.testing.utile.NavigateLocatorUtile;
import com.automation.testing.utile.ProcessCalculationUtile;
import com.automation.testing.validation.LbValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class BulkUploadTestService {

	public static final Logger log = LoggerFactory.getLogger(BulkUploadTestService.class);

	@Autowired
	private BulkUploadService bulkUploadService;

	@Autowired
	private CreateResultSheet createResultSheet;

	@Autowired
	private UserService userService;

	@Autowired
	private WebDriverWait wait;
	
	@Autowired
	private WebDriver driver;

	@Autowired
	private DprPlanCompletionService dprPlanCompletionService;

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();
	private List<BulkUploadError> errorList = new ArrayList<>();

	private final String invalidLBPath = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\LB_ATTRIBUTES.json";

	// 1. TEST LB PLAN UPLOAD OR NOT
	public boolean testBulkUpload(BulkUploadValidationData bulkTestData) {
		boolean b = false;
		try {

			String user = Department.CIRCLE_MW_PLANNER.getName();
			// login
			runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(Department.CIRCLE_MW_PLANNER.getName()),
					"User login", "Failed to Login", null, user);
			log.info("User Login");

			// open Lb Upload Page.
			runStep(ProcessName.OPEN_LB_UPLOAD_PAGE, () -> NavigateLocatorUtile.navigateToLbPlanUpload(driver,wait),
					"User able to navigate LB Upload Page", "User not able to navigate Lb Upload Page", null, user);
			log.info("LB Upload Page Open");

			BulkUploadWrapper bulkUploadResult = uploadPlan(bulkTestData);

			// verify result
			runStep(ProcessName.TEST_STATUS, () -> {
				if ((bulkTestData.getTestType() != null && bulkTestData.getTestType().equals(TestType.POSITIVE_TEST))) {
					if (bulkUploadResult.getTotalNewPlans() > 0 && bulkUploadResult.getTotalInvalidPlans() == 0) {
						return true;
					} else {
						return false;
					}
				} else if ((bulkTestData.getTestType() != null
						&& bulkTestData.getTestType().equals(TestType.NEGATIVE_TEST))) {
					if (bulkUploadResult.getTotalNewPlans() == 0 && bulkUploadResult.getTotalInvalidPlans() > 0
							&& bulkUploadResult.getTotalUpdatePlans() == 0) {
						return true;
					} else {
						return false;
					}
				} else {
					log.info("ISSUE IN TEST CREDENTAILS");
					return false;
				}
			}, bulkTestData.getRemark(), "Failed " + bulkTestData.getRemark(), errorList, user);

			createResultSheet.createResult("BULK_UPLOAD_TEST", data);
			log.info("Test is complete, Excel is Generated.");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	// 2. UPLOAD THE INVALID SHEET AND VALIDATE VALIDATION & ERROR MESSAGE
	public void verifyAttributesValidationLB(BulkUploadValidationData bulkTestInput) {
		Map<Integer, String> expectedResult = createResultSheet.readAttributeValidation(invalidLBPath);
		log.info("Expected Result = " + expectedResult);

		// navigate the LB Upload Page.
		openLbUploadPage(Department.CIRCLE_MW_PLANNER.getName());

		// upload the sheet & fetch the result
		BulkUploadWrapper bulkUploadResult = uploadPlan(bulkTestInput);

		// verify the UI error with expected error message
		runStep(ProcessName.VERIFY_BULK_UPLOAD_INVALID_ATTRIBUTES,
				() -> LbValidation.verifyLBInvalidInput(bulkUploadResult, expectedResult), "All Validation Works",
				"Failed to Validate LB Attributes", null, Department.CIRCLE_MW_PLANNER.getName());

		createResultSheet.createResult("BULK_UPLOAD_TEST", data);

	}

	private void openLbUploadPage(String user) {
		runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(Department.CIRCLE_MW_PLANNER.getName()),
				"User login", "Failed to Login", null, user);
		log.info("User Login");

		// open Lb Upload Page.
		runStep(ProcessName.OPEN_LB_UPLOAD_PAGE, () -> NavigateLocatorUtile.navigateToLbPlanUpload(driver,wait),
				"User able to navigate LB Upload Page", "User not able to navigate Lb Upload Page", null,
				Department.CIRCLE_MW_PLANNER.getName());
		log.info("LB Upload Page Open");
	}

	// UPLOAD THE SHEET AND RETURN THE OUTPUT
	private BulkUploadWrapper uploadPlan(BulkUploadValidationData bulkInput) {
		// select the circle
		runStep(ProcessName.SELECT_CIRCLE, () -> bulkUploadService.selectCircle(bulkInput.getCircle()),
				"User Select The Circle", "Failed to select circle", null, Department.CIRCLE_MW_PLANNER.getName());
		log.info("User select the circle");

		// upload the sheet.
		runStep(ProcessName.PLAN_UPLOAD, () -> {
			boolean upload = bulkUploadService.uploadPlan(bulkInput.getSheetPath());
			upload &= (bulkUploadService.validatePlanUpload(errorList)
					|| testTypeVerify(bulkInput.getTestType(), errorList.size()));
			return upload;
		}, "PLAN UPLOADED", "Failed to PLAN UPLOAD", errorList, Department.CIRCLE_MW_PLANNER.getName());

		// verify bulk upload plan
		return bulkUploadService.getBulkUploadWrapper();
	}

	// DPR BULK UPLOAD TESTING
	public void verifyDPRBulkUpload(BulkUploadValidationData bulkInput) {

		// LOGIN
		userService.userLogin(Department.CIRCLE_DEPLOYMENT_TEAM.getName());

		// navigate To MIDS DPR Bulk Upload;
		NavigateLocatorUtile.navigateToDPRPlanUpload(driver,wait);

		BulkUploadWrapper bulkUploadResult = uploadPlan(bulkInput);

	}

	// verify result according to TestType.
	private boolean testTypeVerify(TestType type, int errorCount) {
		if (type.equals(TestType.POSITIVE_TEST) && errorCount == 0)
			return true;
		else if (type.equals(TestType.NEGATIVE_TEST) && errorCount > 0)
			return true;
		else
			return false;
	}

	// open bulk upload options of MW plan Tracking Page.
	public boolean openMWplanTrackingBulkUploadOptions(int idx) {
		boolean b = false;
		try {
			String openBulkUploadOptionPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-horizontal-layout/div";
			dprPlanCompletionService.clickOnElement(openBulkUploadOptionPath);

			String bulkUploadPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[4]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button["
					+ idx + "]";
			dprPlanCompletionService.clickOnElement(bulkUploadPath);

			uploadPlan(null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String user) {

		process.start(name.name());
		boolean result = step.get(); // run the actual process
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		if (result) {

			if (errors != null && !errors.isEmpty()) {
				// multiple error rows
				for (BulkUploadError err : errors) {
					String fullMsg = "Row " + err.getRowNumber() + " → " + err.getErrorMessage();
					data.add(createResultSheet.createResultData(name.name(), time, true, fullMsg, user));
				}
			} else {
				data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, user));
				log.info(name.name() + " -> " + time + " execute");
			}

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
			finalizeAndStop(); // stop flow immediately!
			return false;
		}
	}

	private void finalizeAndStop() {
		log.error("Process stopped due to failure. Generating result sheet...");
		createResultSheet.createResult("BULK_UPLOAD_TEST", data);
		throw new RuntimeException("Process aborted due to failure");
	}

}
