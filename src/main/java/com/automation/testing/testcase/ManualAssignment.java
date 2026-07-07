package com.automation.testing.testcase;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.ProcessData;
import com.automation.testing.data.TestStatusData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.DprPlanTrackPath;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.enums.ReportCountPath;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.UserService;
import com.automation.testing.sourcecredentials.UserDetails;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class ManualAssignment {

	private static final Logger log = LoggerFactory.getLogger(ManualAssignment.class);

	private final UserService userService;
	private final DriverConfig driverConfig;
	private final CreateResultSheet createResultSheet;

	public ManualAssignment(UserService userService, DriverConfig driverConfig, CreateResultSheet createResultSheet) {
		this.userService = userService;
		this.driverConfig = driverConfig;
		this.createResultSheet = createResultSheet;
	}

	private static Map<String, List<String>> assignList = new ConcurrentHashMap<>();
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	private List<CompletableFuture<Void>> futures = new ArrayList<>();

	private List<ProcessName> nameList = Arrays.asList(ProcessName.PLANNER_ASSIGNMENT,
			ProcessName.DEPLOYMENT_ASSIGNMENT, ProcessName.DINC_ASSIGNMENT, ProcessName.OPERATION_ASSIGNMENT,
			ProcessName.MS_ASSIGNMENT);

	private Map<String, List<String>> userStages = Map.of(Department.CIRCLE_MW_PLANNER.getName(),
			Arrays.asList("SO PENDING", "SP PENDING", "SR PENDING",
//			"AT ACCEPTED",
					"HOP ALIGNMENT PENDING/PHY-AT ACCEPTED", "HOP ALIGNMENT PENDING/PHY-AT PENDING",
					"HOP ALIGNMENT PENDING/PHY-AT RAISED", "I&C PENDING", "MO PENDING", "PHY+SOFT AT PENDING",
					"PHY-AT ACCEPTED/SOFT-AT PENDING", "PHY-AT ACCEPTED/SOFT-AT RAISED",
					"PHY-AT ACCEPTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT ACCEPTED",
					"PHY-AT PENDING/SOFT-AT RAISED", "PHY-AT PENDING/SOFT-AT REJECTED",
					"PHY-AT RAISED/SOFT-AT ACCEPTED", "PHY-AT RAISED/SOFT-AT PENDING", "PHY-AT RAISED/SOFT-AT RAISED",
					"PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT REJECTED/SOFT-AT PENDING",
					"PHY-AT REJECTED/SOFT-AT REJECTED", "Request For Cancellation", "RFAI PENDING",
					"TS PLAN RELEASE PENDING", "UPGRADE COMPLETED"),

			Department.CIRCLE_DEPLOYMENT_TEAM.getName(),
			Arrays.asList("HOP ALIGNMENT PENDING/PHY-AT ACCEPTED", "HOP ALIGNMENT PENDING/PHY-AT PENDING",
					"HOP ALIGNMENT PENDING/PHY-AT RAISED", "I&C PENDING", "MO PENDING", "PHY+SOFT AT PENDING",
					"PHY-AT ACCEPTED/SOFT-AT PENDING", "PHY-AT ACCEPTED/SOFT-AT RAISED",
					"PHY-AT PENDING/SOFT-AT ACCEPTED", "PHY-AT PENDING/SOFT-AT RAISED",
					"PHY-AT PENDING/SOFT-AT REJECTED", "PHY-AT RAISED/SOFT-AT PENDING",
					"PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT REJECTED/SOFT-AT PENDING",
					"PHY-AT REJECTED/SOFT-AT REJECTED", "RFAI PENDING", "SO PENDING", "SP PENDING", "SR PENDING"),

			Department.CIRCLE_OPERATION_TEAM.getName(), Arrays.asList("AT ACCEPTED", "UPGRADE PENDING"),

			Department.CIRCLE_DINC_PARTNER.getName(),
			Arrays.asList("HOP ALIGNMENT PENDING/PHY-AT ACCEPTED", "HOP ALIGNMENT PENDING/PHY-AT PENDING",
					"HOP ALIGNMENT PENDING/PHY-AT RAISED", "I&C PENDING", "MO PENDING", "PHY+SOFT AT PENDING",
					"PHY-AT ACCEPTED/SOFT-AT PENDING", "PHY-AT ACCEPTED/SOFT-AT RAISED",
					"PHY-AT PENDING/SOFT-AT ACCEPTED", "PHY-AT PENDING/SOFT-AT RAISED",
					"PHY-AT PENDING/SOFT-AT REJECTED", "PHY-AT RAISED/SOFT-AT PENDING",
					"PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT REJECTED/SOFT-AT PENDING",
					"PHY-AT REJECTED/SOFT-AT REJECTED", "RFAI PENDING", "SO PENDING", "SP PENDING", "SR PENDING"),

			Department.CIRCLE_MS_PARTNER.getName(),
			Arrays.asList("HOP Alignment Pending/PHY-AT Raised", "PHY-AT Raised/SOFT-AT Pending",
					"PHY-AT Accepted/SOFT-AT Pending", "PHY-AT Pending/SOFT-AT Raised", "PHY-AT Raised/SOFT-AT Raised",
					"PHY-AT Raised/SOFT-AT Rejected", "PHY-AT Rejected/SOFT-AT Raised",
					"PHY-AT Accepted/SOFT-AT Raised", "AT Accepted"));

	static {
		assignList.put(Department.CIRCLE_MW_PLANNER.getName(), Arrays.asList(Department.CIRCLE_MW_PLANNER.getName(),
				Department.CIRCLE_DEPLOYMENT_TEAM.getName(), Department.CIRCLE_DINC_PARTNER.getName()));

		assignList.put(Department.CIRCLE_DEPLOYMENT_TEAM.getName(),
				Arrays.asList(Department.CIRCLE_DEPLOYMENT_TEAM.getName(), Department.CIRCLE_DINC_PARTNER.getName()));

		assignList.put(Department.CIRCLE_DINC_PARTNER.getName(),
				Arrays.asList(Department.CIRCLE_DINC_PARTNER.getName()));

		assignList.put(Department.CIRCLE_OPERATION_TEAM.getName(),
				Arrays.asList(Department.CIRCLE_OPERATION_TEAM.getName()));

		assignList.put(Department.CIRCLE_MS_PARTNER.getName(), Arrays.asList(Department.CIRCLE_MS_PARTNER.getName()));
	}

	public void run() {

		int idx = 0;
		for (Map.Entry<String, List<String>> entry : assignList.entrySet()) {
			int currentIndex = idx++;
			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				WebDriver driver = driverConfig.createNewBrowser();
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

				ProcessCalculationUtile process = new ProcessCalculationUtile();
				List<ProcessData> data = new ArrayList<>();

				String user = entry.getKey();
				List<String> users = entry.getValue();

				try {
					runStep(ProcessName.USER_LOGIN,
							() -> new TestStatusData("", userService.userLogin(user, driver, wait), ""), "User Login",
							"Failed to User Login", user, "", process, driver, data);

					if (user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
						assignmentForMS(user, driver, wait, process, data);
					} else {
						assignment(nameList.get(currentIndex), user, users, driver, wait, process, data);
					}

					userService.logOut(driver, wait);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					createResultSheet.createResultForAssignment(user + "_ManualAssignment", data);
					driver.close();
					log.info(user + " Driver is closed & report Generated");
				}

			}, executorService);

			futures.add(future);

		}

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
	}

	private void assignment(ProcessName name, String user, List<String> userList, WebDriver driver, WebDriverWait wait,
			ProcessCalculationUtile process, List<ProcessData> data) {
		for (String currentAssUser : userList) {
			String passMesg = user + " able to assign plan to" + currentAssUser;
			String failedMeg = "Failed: plan assign " + user + " to " + currentAssUser;

			log.info(user + " assign plan to " + currentAssUser);

			for (String stage : userStages.get(user)) {
				runStep(name, () -> {
					boolean b = true;
					b &= assignPlan(user, stage, currentAssUser, driver, wait);
					driverConfig.waitForIdle(driver);
					String notif = MidsUtile.getNotifications(driver, wait);
					b &= !notif.contains("not be assigned as plan is not");
					System.out.println("Status = " + notif + " : " + b);
					return new TestStatusData(notif, b, stage);
				}, passMesg + " at Stage[" + stage + "]", failedMeg + " at Stage[" + stage + "]", user, currentAssUser,
						process, driver, data);
				log.info(user + " assign -> " + currentAssUser + " at Stage[" + stage + "]");
				driverConfig.refreshDriver(driver);
			}
		}
	}

	private boolean assignPlan(String user, String stage, String assignUser, WebDriver driver, WebDriverWait wait,
			String... metaData) {
		try {
			log.info(assignUser + " At " + stage);

			MidsUtile.filterPlans(driver, wait, DprPlanTrackPath.SEARCH_BY_PLAN_STAGE.getPath(), stage);
			driverConfig.waitForIdle(driver);

			Integer count = MidsUtile.getReportCount(wait, ReportCountPath.MW_PLANING_REPORT.getPath());
			if (count <= 0) {
				System.out.println("Plan not count for user " + user + " at stage " + stage);
				return false;
			}

			String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[1]";
			WebElement elm = MidsUtile.getElementInTable(driver, wait, path);
			elm.click();

			selectData(assignUser.equals(Department.CIRCLE_DINC_PARTNER.getName()) ? "Circle I&C Partner" : assignUser,
					DprPlanTrackPath.SELECT_DEPARTMENT.getPath(), driver, wait);

			boolean x = false;
			for (int i = 0; i < 5; i++) {
				try {
					Thread.sleep(300);
					WebElement elemnt = MidsUtile.findShadowElement(driver, "body > vaadin-dialog-overlay:nth-child(8)",
							"vaadin-button[role='button'][theme='primary']", "#button");
					MidsUtile.clickElement(elemnt);
					driverConfig.waitForIdle(driver);
					x = true;
					break;
				} catch (Exception e) {
					// TODO: handle exception
					log.info("Retry..." + i);
				}
			}

			if (!x)
				return false;

			if (user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
				selectData(metaData[0], DprPlanTrackPath.SELECT_AT_TYPE.getPath(), driver, wait);
				driverConfig.waitForIdle(driver);
			}

			Thread.sleep(3000);

			selectData(UserDetails.getAssignName(assignUser), DprPlanTrackPath.SELECT_USER.getPath(), driver, wait);

			WebElement elemnt1 = MidsUtile.findShadowElement(driver, "body > vaadin-dialog-overlay:nth-child(8)",
					"vaadin-button[role='button'][theme='primary']", "#button");
			MidsUtile.clickElement(elemnt1);
			driverConfig.waitForIdle(driver);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean selectData(String data, String path, WebDriver driver, WebDriverWait wait) {
		try {
			WebElement element = MidsUtile.getElement(wait, path);
			element.sendKeys(data);
			Thread.sleep(300);
			element.sendKeys(Keys.ENTER);
			driverConfig.waitForIdle(driver);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean assignmentForMS(String user, WebDriver driver, WebDriverWait wait, ProcessCalculationUtile process,
			List<ProcessData> data) {
		try {
			for (String stage : userStages.get(user)) {

				for (String atType : Arrays.asList("PHY", "SOFT")) {

					String passMeg = user + " able to assign plan to MS partner at " + atType + " AT [" + stage + "]";
					String failedMeg = "Failed to assign plan to MS partner at " + atType + " AT [" + stage + "]";

					runStep(ProcessName.MS_ASSIGNMENT, () -> {
						boolean b = true;
						b &= assignPlan(user, stage, user, driver, wait, atType);
						driverConfig.waitForIdle(driver);
						String notif = MidsUtile.getNotifications(driver, wait);
						b &= !notif.contains("not be assigned as plan is not");
						System.out.println("Status = " + notif + " : " + b);
						return new TestStatusData(notif, b, stage);
					}, passMeg, failedMeg, user, user, process, driver, data);

					driverConfig.refreshDriver(driver);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void runStep(ProcessName name, Supplier<TestStatusData> step, String successMsg, String errorMsg,
			String department, String assignedDep, ProcessCalculationUtile process, WebDriver driver,
			List<ProcessData> data) {

		process.start(name.name());
		TestStatusData result = step.get();
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		data.add(createResultSheet.createResultData(name.name(), time, result.isStatus() ? successMsg : errorMsg,
				result.isStatus(), department, assignedDep, result.getRemark() != null ? result.getRemark() : "",
				result.getStage()));

	}

}