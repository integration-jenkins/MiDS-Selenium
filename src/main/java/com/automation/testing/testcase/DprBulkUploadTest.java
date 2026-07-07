package com.automation.testing.testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.BulkUploadError;
import com.automation.testing.data.BulkUploadValidationData;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.DprPlanTrackPath;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.enums.TestType;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.BulkUploadTestService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.DprPlanCompletionService;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.DprPlanCompletionUtile;
import com.automation.testing.utile.NavigateLocatorUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class DprBulkUploadTest {

	private static final Logger log = LoggerFactory.getLogger(DprBulkUploadTest.class);

	@Autowired
	private BulkUploadService bulkUploadService;

	@Autowired
	private DprPlanCompletionService dprPlanCompletionService;

	@Autowired
	private CreateResultSheet createResultSheet;

	@Autowired
	private UserService userService;

	@Autowired
	private WebDriverWait wait;

	@Autowired
	private WebDriver driver;

	@Autowired
	private DriverConfig driverConfig;

	@Autowired
	BulkUploadTestService bulkUploadTestService;

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();
	private List<BulkUploadError> errorList = new ArrayList<>();

	public void run() {

		String DPR_FILE_PATH = "D:\\testingProject\\testing\\src\\main\\resources\\project_file\\MIDS_DPR_REPORT.xlsx";
		String circle = "AP";
		String linkId = "SRS031-COW069";
		BulkUploadValidationData bulkInput = new BulkUploadValidationData();

		bulkInput.setCircle(circle);
		bulkInput.setPlanId(linkId);
		bulkInput.setSheetPath(DPR_FILE_PATH);
		bulkInput.setTestType(TestType.POSITIVE_TEST);
		bulkInput.setRemark("test the Bulk Upload functionality");

		bulkUploadTestService.verifyDPRBulkUpload(bulkInput);

		// open Plan Tracking Page.
		NavigateLocatorUtile.navigateToDprPlanTrackForDeploymentUser(driver,wait);

		String user = Department.CIRCLE_DEPLOYMENT_TEAM.getName();

		runStep(ProcessName.OPEN_DPR_PLAN, () -> {
			boolean b = dprPlanCompletionService.searchDprPlanByLinkId(linkId);
			driverConfig.waitForIdle(driver);
			return b;
		}, "Plan Open for " + linkId + " link id", "Plan Not Open for " + linkId + " link id", null, user);

		// verify data of dpr plan.
		runStep(ProcessName.VERIFY_PLAN_STATUS, () -> {
			String planStatus = dprPlanCompletionService.getCurrentPlanStatus();
			if (planStatus != null && !planStatus.isEmpty())
				return planStatus.contains("PHY+SOFT AT PENDING");
			else
				return false;
		}, "Plan status is updated", "Failed To update plan status", null, user);

		verifyDprData(user);

		createResultSheet.createResult("DPR_BULK_TEST", data);

	}

	private void verifyDprData(String user) {
		// verify TOCO VENDOR
		runStep(ProcessName.TOCO_VENDOR, () -> DprPlanCompletionUtile.verifyTocoVendor(driver),
				"Toco Vendor (BASIC DETAILS)", "Toco Vendor (BASIC DETAILS) Not Filled", null, user);

		// verify SR TO RAFI DETAILS
		runStep(ProcessName.SR_TO_RFAI_PENDING, () -> {
			dprPlanCompletionService.clickOnElement(DprPlanTrackPath.SR_RAFI_TAB.getPath());
			boolean b = DprPlanCompletionUtile.verifySpDetails(driver);
			b &= DprPlanCompletionUtile.verifySOandSPDetails(driver);
			return b;
		}, "SR-RAFI details Filled", "SR-RAFI details Not Filled", null, user);

		// verify MO details
		runStep(ProcessName.MO_PENDING, () -> {
			dprPlanCompletionService.clickOnElement(DprPlanTrackPath.MATERIAL_ORDER_TAB.getPath());
			return DprPlanCompletionUtile.verifyMOData(driver);
		}, "MO Filled", "MO Not Filled", null, user);

		// verify INSTALL AND COMM Details
		runStep(ProcessName.INSTALL_AND_COMM_PENDING, () -> {
			dprPlanCompletionService.clickOnElement(DprPlanTrackPath.INSTALL_AND_COMM_TAB.getPath());
			return DprPlanCompletionUtile.verifyInstallAndCommData(driver);
		}, "INSTALL AND COMM Details Filled", "INSTALL AND COMM Details Not Filled", null, user);
	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			List<BulkUploadError> errors, String user) {

		process.start(name.name());
		boolean result = step.get(); // run the actual process
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
//			finalizeAndStop(); // stop flow immediately!

			return false;
		}
	}

	private void finalizeAndStop() {
		log.error("Process stopped due to failure. Generating result sheet...");
		createResultSheet.createResult("DPR_PLAN_COMPLETION", data);
		throw new RuntimeException("Process aborted due to failure");
	}

}
