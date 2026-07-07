package com.automation.testing.testcase;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.automation.testing.enums.DprPlanStatus;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.service.BulkUploadService;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.DprPlanCompletionService;
import com.automation.testing.service.UserService;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.NavigateLocatorUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class DprPlanCompletionTest {

	private static final Logger log = LoggerFactory.getLogger(DprPlanCompletionTest.class);

	private final UserService userService;
	private final BulkUploadService bulkUploadService;
	private final WebDriverWait wait;
	private final WebDriver driver;
	private final DprPlanCompletionService dprPlanCompletionService;
	private final CreateResultSheet createResultSheet;

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();
	private List<BulkUploadError> errorList = new ArrayList<>();

	public DprPlanCompletionTest(UserService userService, WebDriverWait wait, BulkUploadService bulkUploadService,
			DriverConfig driverConfig, WebDriver driver, DprPlanCompletionService dprPlanCompletionService,
			CreateResultSheet createResultSheet) {
		this.userService = userService;
		this.wait = wait;
		this.bulkUploadService = bulkUploadService;
		this.driver = driver;
		this.dprPlanCompletionService = dprPlanCompletionService;
		this.createResultSheet = createResultSheet;
	}

	public void run() {
		log.info("Start DPR Plan : " + Timestamp.from(Instant.now()));

		String circle = "JK";
		String path = "D:\\Automation_result\\test_upload_sheet\\LB_SHEET.xlsx";

		List<String> linkIdList = Arrays.asList("AutoTestKYC10-AutoTestKYC20", "AutoTestKYC30-AutoTestKYC40");

		// LOGIN BY MW PLANNER
		String user = Department.CIRCLE_MW_PLANNER.getName();
		userService.userLogin(Department.CIRCLE_MW_PLANNER.getName());
		NavigateLocatorUtile.navigateToPlanUpload(driver,wait);

		// 1. LB PLAN UPLOAD
		if (!runStep(ProcessName.LB_PLAN_UPLOAD, () -> {
			bulkUploadService.selectCircle(circle);
			bulkUploadService.uploadPlan(path);
			return bulkUploadService.validatePlanUpload(errorList);
		}, "Plan Uploaded Successfully", "Plan Upload Failed", errorList, user))
			return;

		// Navigate
		userService.logOut(driver, wait);

		// login by Deployment User
		userService.userLogin(Department.CIRCLE_DEPLOYMENT_TEAM.getName());
		user = Department.CIRCLE_DEPLOYMENT_TEAM.getName();

		// 2. search the plan
		for (String linkId : linkIdList) {
			runStep(ProcessName.OPEN_DPR_PLAN, () -> {
				return dprPlanCompletionService.searchDprPlanByLinkId(linkId);
			}, linkId + ": Plan Found ", linkId + ": Not Found ", null, user);

			// verify status update or not
			runStep(ProcessName.STATUS_VERIFY, () -> {
				return dprPlanCompletionService.verifyCurrectStatus(DprPlanStatus.SR_PENDING.getStatus());
			}, "Status Updated.",
					"Failed to update status Current Status should be " + DprPlanStatus.SR_PENDING.getStatus(), null,
					user);

			// 3. OPEN PLAN
			runStep(ProcessName.SR_TO_RFAI_PENDING, () -> {
				dprPlanCompletionService.fillBasicDetsils();
				return dprPlanCompletionService.srToRafiProcess();
			}, "SR To RAFAI Completed", "SR To RAFAI Failed", null, user);

			log.info("Current Status = " + dprPlanCompletionService.getCurrentPlanStatus());
			runStep(ProcessName.STATUS_VERIFY, () -> {
				return dprPlanCompletionService.verifyCurrectStatus(DprPlanStatus.MO_PENDING.getStatus());
			}, "Status Updated.",
					"Failed to update status Current Status should be " + DprPlanStatus.MO_PENDING.getStatus(), null,
					user);

			// 4. MATERIAL ORDER
			runStep(ProcessName.MO_PENDING, () -> dprPlanCompletionService.materialOrderProcess(), "MO Completed",
					"MO Failed", null, user);
			log.info("Current Status = " + dprPlanCompletionService.getCurrentPlanStatus());

			// 5. INSTALL & COMMISSION
			runStep(ProcessName.STATUS_VERIFY, () -> {
				return dprPlanCompletionService.verifyCurrectStatus(DprPlanStatus.I_AND_C_PENDING.getStatus());
			}, "Status Updated.",
					"Failed to update status Current Status should be " + DprPlanStatus.I_AND_C_PENDING.getStatus(),
					null, user);

			runStep(ProcessName.INSTALL_AND_COMM_PENDING, () -> dprPlanCompletionService.installAndCommProcess(),
					"Install & Comm Done", "Install & Comm Failed", null, user);
			log.info("Current Status = " + dprPlanCompletionService.getCurrentPlanStatus());

			// 6. AT Raise
			runStep(ProcessName.STATUS_VERIFY, () -> {
				return dprPlanCompletionService.verifyCurrectStatus(DprPlanStatus.PHT_SOFT_AT_PENDING.getStatus());
			}, "Status Updated.",
					"Failed to update status Current Status should be " + DprPlanStatus.PHT_SOFT_AT_PENDING.getStatus(),
					null, user);

			runStep(ProcessName.AT_RAISE, () -> dprPlanCompletionService.phyAndSoftATProcess(), "AT Raised",
					"AT Raise Failed", null, user);
			log.info("Current Status = " + dprPlanCompletionService.getCurrentPlanStatus());

			// back and navigate to next plan
			dprPlanCompletionService.backButton();
		}

		// 7. Logout and login MS Partner
		userService.logOut(driver, wait);
		userService.userLogin(Department.CIRCLE_MS_PARTNER.getName());

		user = Department.CIRCLE_MS_PARTNER.getName();

		for (String linkId : linkIdList) {
			runStep(ProcessName.OPEN_DPR_PLAN, () -> {
				return dprPlanCompletionService.searchDprPlanByLinkId(linkId);
			}, linkId + ": Plan Found ", linkId + ": Not Found After AT Raise, Plan not visible for MS Partner", null,
					user);

			// 8. AT Accept
			runStep(ProcessName.STATUS_VERIFY, () -> {
				return dprPlanCompletionService.verifyCurrectStatus(DprPlanStatus.PHY_SOFT_AT_RAISED.getStatus());
			}, "Status Updated.",
					"Failed to update status Current Status should be " + DprPlanStatus.PHY_SOFT_AT_RAISED.getStatus(),
					null, user);

			runStep(ProcessName.AT_ACCEPT, () -> {
				return dprPlanCompletionService.acceptAtProcess();
			}, "AT Accepted", "AT Accept Failed", null, user);
			log.info("Current Status = " + dprPlanCompletionService.getCurrentPlanStatus());
			runStep(ProcessName.STATUS_VERIFY, () -> {
				return dprPlanCompletionService.verifyCurrectStatus(DprPlanStatus.AT_ACCEPTED.getStatus());
			}, "Status Updated.",
					"Failed to update status Current Status should be " + DprPlanStatus.AT_ACCEPTED.getStatus(), null,
					user);
			dprPlanCompletionService.backButton();
		}

		userService.logOut(driver, wait);

		// 9. Login O&M
		userService.userLogin(Department.CIRCLE_OPERATION_TEAM.getName());

		for (String linkId : linkIdList) {
			// 10. TS Complete
			runStep(ProcessName.TS_COMPLETE, () -> {
				dprPlanCompletionService.searchDprPlanByLinkId(linkId);
				return dprPlanCompletionService.completeTSProcess();
			}, "TS Completed", "TS Failed", null, user);
			log.info("Current Status = " + dprPlanCompletionService.getCurrentPlanStatus());
			runStep(ProcessName.STATUS_VERIFY, () -> {
				return dprPlanCompletionService.verifyCurrectStatus(DprPlanStatus.TS_DONE.getStatus());
			}, "Status Updated.",
					"Failed to update status Current Status should be " + DprPlanStatus.TS_DONE.getStatus(), null,
					user);
			dprPlanCompletionService.backButton();
		}
		userService.logOut(driver, wait);

		createResultSheet.createResult("DPR_PLAN_COMPLETION", data);

		log.info("End DPR plan at " + Timestamp.from(Instant.now()));
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
			finalizeAndStop(); // stop flow immediately!
			return false;
		}
	}

	private void finalizeAndStop() {
		log.error("Process stopped due to failure. Generating result sheet...");
		createResultSheet.createResult("DPR_PLAN_COMPLETION", data);
		throw new RuntimeException("Process aborted due to failure");
	}

}
