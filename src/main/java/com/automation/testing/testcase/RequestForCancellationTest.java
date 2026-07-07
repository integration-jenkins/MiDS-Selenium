package com.automation.testing.testcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.DprPlanTrackPath;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.DprPlanCompletionService;
import com.automation.testing.service.UserService;
import com.automation.testing.sourcecredentials.UserDetails;
import com.automation.testing.utile.DprPlanCompletionUtile;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class RequestForCancellationTest {

	private static final Logger log = LoggerFactory.getLogger(RequestForCancellationTest.class);

	private final UserService userService;
	private final WebDriverWait wait;
	private final WebDriver driver;
	private final DprPlanCompletionService dprPlanCompletionService;
	private final CreateResultSheet createResultSheet;

	@Autowired
	private DriverConfig driverConfig;

	public RequestForCancellationTest(UserService userService, WebDriverWait wait, WebDriver driver,
			DprPlanCompletionService dprPlanCompletionService, CreateResultSheet createResultSheet) {
		this.userService = userService;
		this.wait = wait;
		this.driver = driver;
		this.dprPlanCompletionService = dprPlanCompletionService;
		this.createResultSheet = createResultSheet;
	}

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();
	private List<String> userList = List.of(Department.DEPLOYMENT_USER.getName(), Department.MW_PLANNER_USER.getName(),
			Department.OPERATION_USER.getName(), Department.DINC_PARTNER_USER.getName(),
			Department.CIRCLE_OPERATION_TEAM.getName(), Department.CIRCLE_DINC_PARTNER.getName());

	public void run() {
		String[] planId = { "MW-N-KK-09032026-6163", "MW-N-AP-04062025-9626", "MW-N-AP-18092023-51091",
				"MW-N-MAH-03092025-6359", "MW-N-HAR-03042023-2170", "MW-N-KK-02032026-4397" };

		try {

			// 1. USER LOGIN AND FILL RFC
			for (int i = 0; i < planId.length; i++) {
				String user = userList.get(i);
				final String id = planId[i];
				// USER LOGIN
				runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(user), "User Login", "Failed to Login",
						user);
				// OPEN PLAN BY PLAN ID
				runStep(ProcessName.OPEN_DPR_PLAN, () -> dprPlanCompletionService.searchDprPlanByPlanId(id),
						"User able to open Plans", "User Not able to openn plan for plan ID " + id, user);

				// REQUEST FOR CANCELLATION
				runStep(ProcessName.REQUEST_FOR_CANCELLATION, () -> {
					return MidsUtile.clickElement(wait, DprPlanTrackPath.CANCAEL_CHEK_BOX_BUTTON.getPath())
							&& fillRemarks();
				}, "User able to apply for Request for cancellation", "Failed for Req. for cancellation", user);

				userService.logOut(driver, wait);
			}

			// 2. DEPLOYMENT LOGIN
			runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(Department.CIRCLE_DEPLOYMENT_TEAM.getName()),
					"User Login", "Failed to Login", Department.CIRCLE_DEPLOYMENT_TEAM.getName());

			for (String plaId : planId) {
				runStep(ProcessName.CANCEL_THE_PLAN, () -> {
					try {
						boolean b = true;
						// ASSIGN PLAN TO DEPLOYMENT ADMIN
						b &= dprPlanCompletionService.selectPlanByPlanId(plaId);
						b &= dprPlanCompletionService.assignToUser(Department.CIRCLE_DEPLOYMENT_TEAM.getName());
						driverConfig.refreshDriver(driver);
						driverConfig.waitForIdle(driver);

						// RFC THE PLAN
						updatePlan("RFC");
						driverConfig.refreshDriver(driver);
						driverConfig.waitForIdle(driver);
						return b;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return false;
					}
				}, "Plan Canceled", "Failed to plan canceled", Department.CIRCLE_DEPLOYMENT_TEAM.getName());

				driverConfig.refreshDriver(driver);
				driverConfig.waitForIdle(driver);
			}

			userService.logOut(driver, wait);

			// 3. Planner cancel the Plan
			runStep(ProcessName.USER_LOGIN, () -> userService.userLogin(Department.MW_PLANNER_USER.getName()),
					"User Login", "Failed to Login", Department.MW_PLANNER_USER.getName());

			for (String plaId : planId) {
				runStep(ProcessName.CANCEL_THE_PLAN, () -> {
					try {
						boolean b = true;
//						// ASSIGN PLAN TO DEPLOYMENT ADMIN
						b &= dprPlanCompletionService.selectPlanByPlanId(plaId);
//						b &= dprPlanCompletionService.assignToUser(Department.CIRCLE_DEPLOYMENT_TEAM.getName());
						driverConfig.refreshDriver(driver);
//						driverConfig.waitForIdle(driver);

						// RFC THE PLAN
						updatePlan("cancel");
						driverConfig.refreshDriver(driver);
						driverConfig.waitForIdle(driver);
						return b;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return false;
					}
				}, "Plan Canceled", "Failed to plan canceled", Department.MW_PLANNER_USER.getName());

				driverConfig.refreshDriver(driver);
				driverConfig.waitForIdle(driver);
			}

			userService.logOut(driver, wait);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			createResultSheet.createResult("NewDepPlanCancellationTest", data);
		}

	}

	private boolean updatePlan(String status) {
		boolean b = true;
		try {
			String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-combo-box:nth-child(5)";
			String cssSelectorForHost2 = "#input";
			WebElement element = MidsUtile.findShadowElement(driver, cssSelectorForHost1, cssSelectorForHost2,
					"input[placeholder='Change Plan Status']");

			Thread.sleep(300);
			element.sendKeys(status);
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);
			driverConfig.waitForIdle(driver);

			String cssSelectorForHost11 = "body > vaadin-dialog-overlay:nth-child(8) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-combo-box:nth-child(1)";
			WebElement elm1 = MidsUtile.findShadowElement(driver, cssSelectorForHost11, cssSelectorForHost2,
					"input[placeholder='Select a reason']");
			elm1.sendKeys("LOS ISSUE");
			Thread.sleep(500);
			elm1.sendKeys(Keys.ENTER);

			String confirm = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-button";

			Thread.sleep(1000);
			String cssSelectorForHost12 = "body > vaadin-dialog-overlay:nth-child(8) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-text-field:nth-child(2)";
			WebElement elm2 = MidsUtile.findShadowElement(driver, cssSelectorForHost12,
					"input[placeholder='Fill Additional Details']");
			elm2.sendKeys("remove plan");
			Thread.sleep(300);
			elm2.sendKeys(Keys.ENTER);

			MidsUtile.clickElement(wait, confirm);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return b;
	}

	private boolean fillRemarks() {
		boolean b = true;
		try {
			// Fill Plan Cancel
			String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(2)";
			String cssSelectorForHost2 = "#input";
			String inputSelectior = "input[role='combobox']";
			WebElement element = MidsUtile.findShadowElement(driver, cssSelectorForHost1, cssSelectorForHost2,
					inputSelectior);
			element.sendKeys("Yes");
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);

			// REQUEST FOR CANCELLATION
			String cssSelectorForHost11 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(3)";
			WebElement element2 = MidsUtile.findShadowElement(driver, cssSelectorForHost11, cssSelectorForHost2,
					inputSelectior);
			element2.sendKeys("LOS ISSUE");
			Thread.sleep(300);
			element2.sendKeys(Keys.ENTER);

			// Add.. remark
			cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child(5)";
			WebElement element3 = MidsUtile.findShadowElement(driver, cssSelectorForHost1, "input[part='value']");
			element3.sendKeys("Remove plan");
			Thread.sleep(300);
			element3.sendKeys(Keys.ENTER);

			try {
				// fill Material Remarks
				driverConfig.waitForIdle(driver);
				Thread.sleep(2000);
				cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(7)";
				WebElement element4 = MidsUtile.findShadowElement(driver, cssSelectorForHost1, cssSelectorForHost2,
						inputSelectior);
				element4.sendKeys("Yes");
				Thread.sleep(300);
				element4.sendKeys(Keys.ENTER);

				cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child(8)";
				WebElement element5 = MidsUtile.findShadowElement(driver, cssSelectorForHost1, "input[part='value']");
				element5.sendKeys("Ok");
				Thread.sleep(300);
				element5.sendKeys(Keys.ENTER);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			dprPlanCompletionService.saveButton();

			driverConfig.waitForIdle(driver);
			driverConfig.refreshDriver(driver);

			b &= dprPlanCompletionService.getCurrentPlanStatus().contains("Request For Cancellation");

			dprPlanCompletionService.backButton();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return b;
	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg, String user) {

		process.start(name.name());
		boolean result = step.get(); // run the actual process
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, user));
			log.info(name.name() + " -> " + time + " execute");
			return true; // continue next step
		} else {
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, user));

			log.info(name.name() + " -> " + time + " FAILED");
			finalizeAndStop(); // stop flow immediately!
			return false;
		}
	}

	private void finalizeAndStop() {
		log.error("Process stopped due to failure. Generating result sheet...");
		createResultSheet.createResult("NewDepPlanCancellationTest", data);
		throw new RuntimeException("Process aborted due to failure");
	}

}
