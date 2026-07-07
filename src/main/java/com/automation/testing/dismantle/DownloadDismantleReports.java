package com.automation.testing.dismantle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.DismantleName;
import com.automation.testing.enums.DismantlePath;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.UserService;
import com.automation.testing.sourcecredentials.UserDetails;
import com.automation.testing.utile.DismantleNavigationUtile;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class DownloadDismantleReports {

	private static final Logger log = LoggerFactory.getLogger(DownloadDismantleReports.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final UserService userService;
	private final DriverConfig driverConfig;
	private final CreateResultSheet createResultSheet;

	public DownloadDismantleReports(WebDriver driver, WebDriverWait wait, UserService userService,
			DriverConfig driverConfig, CreateResultSheet createResultSheet) {
		this.driver = driver;
		this.wait = wait;
		this.userService = userService;
		this.driverConfig = driverConfig;
		this.createResultSheet = createResultSheet;
	}

	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private List<ProcessData> data = new ArrayList<>();

	public void run() {

		List<String> userList = UserDetails.getUserDepListForDismantle();

		try {
			for (String users : userList) {

				log.info("Logined User :" + users);
				runStep(DismantleName.USER_LOGIN, () -> userService.userLogin(users), "User able to Login",
						"Failed to user login", users);

				runStep(DismantleName.TRACK, () -> {
					boolean b = true;
					b &= DismantleNavigationUtile.openDismantleInfo(driver, wait, users);
					b &= DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.TRACK);
					return b;
				}, "Dismantle Track Report Open", "Failed to open Dismantle Track Report", users);

				runStep(DismantleName.DOWNLOAD_TRACK_REPORT, () -> {
					boolean b = true;
					b &= MidsUtile.clickElement(wait, DismantlePath.DISMNATLE_OPTIONS.getPath());
					b &= MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_TRACK_PAGE_REPORT.getPath());
					return b;
				}, "Dismantle Track Report Open", "Failed to open Dismantle Track Report", users);

				runStep(DismantleName.DATA_REPORT,
						() -> DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.DATA_REPORT),
						"Dismantle Data Report Open", "Failed to open Dismantle Data Report", users);

				runStep(DismantleName.DOWNLOAD_DATA_REPORT,
						() -> MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_DATA_REPORT.getPath()),
						"Dismantle Data Report Open", "Failed to open Dismantle Data Report", users);

				runStep(DismantleName.HISTORY,
						() -> DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.HISTORY),
						"Dismantle History Report Open", "Failed to open Dismantle History Report", users);

				runStep(DismantleName.DOWNLOAD_HISTORY,
						() -> MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_HISTORY_AND_DELETE_HISTORY.getPath()),
						"Dismantle History Report Open", "Failed to Download Dismantle History Report", users);

				runStep(DismantleName.DELETE_HISTORY,
						() -> DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.DELETE_HISTORY),
						"Dismantle Delete History Report Open", "Failed to open Dismantle Delete History Report",
						users);
				runStep(DismantleName.DOWNLOAD_DELETE_HISTORY,
						() -> MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_HISTORY_AND_DELETE_HISTORY.getPath()),
						"Dismantle delete History Report Download",
						"Failed to Download Dismantle delete History Report", users);

				runStep(DismantleName.PLAN_JUN,
						() -> DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.PLAN_JUN),
						"Dismantle Plan Jouney Report Open", "Failed to open Dismantle Plan Jouney Report", users);

				runStep(DismantleName.DOWNLOAD_PLAN_JUN,
						() -> MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_PLAN_JUN.getPath()),
						"Dismantle Plan Jouney Report Download", "Failed to Download Dismantle Jouney Report", users);

				userService.logOut(driver, wait);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			createResultSheet.createResultForDownloadReport("DISMANTLE_REPORTS", data);
		}

	}

	public void verifyDismantleReports(String users, WebDriver driver, WebDriverWait wait,
			ProcessCalculationUtile processCalculationUtile, List<ProcessData> data, ReentrantLock lock) {
		boolean verified = true;
		try {
			runStep(DismantleName.TRACK, () -> {
				boolean b = true;
				b &= DismantleNavigationUtile.openDismantleInfo(driver, wait, users);
				b &= DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.TRACK);
				return b;
			}, "Dismantle Track Report Open", "Failed to open Dismantle Track Report", users, processCalculationUtile,
					data, driver);

			runStep(DismantleName.DOWNLOAD_TRACK_REPORT, () -> {
				boolean b = true;
				b &= MidsUtile.clickElement(wait, DismantlePath.DISMNATLE_OPTIONS.getPath());
				b &= MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_TRACK_PAGE_REPORT.getPath());
				return b;
			}, "Dismantle Track Report Open", "Failed to open Dismantle Track Report", users, processCalculationUtile,
					data, driver);

			runStep(DismantleName.DATA_REPORT,
					() -> DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.DATA_REPORT),
					"Dismantle Data Report Open", "Failed to open Dismantle Data Report", users,
					processCalculationUtile, data, driver);

			runStep(DismantleName.DOWNLOAD_DATA_REPORT,
					() -> MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_DATA_REPORT.getPath()),
					"Dismantle Data Report Open", "Failed to open Dismantle Data Report", users,
					processCalculationUtile, data, driver);

			runStep(DismantleName.HISTORY,
					() -> DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.HISTORY),
					"Dismantle History Report Open", "Failed to open Dismantle History Report", users,
					processCalculationUtile, data, driver);

			runStep(DismantleName.DOWNLOAD_HISTORY,
					() -> MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_HISTORY_AND_DELETE_HISTORY.getPath()),
					"Dismantle History Report Open", "Failed to Download Dismantle History Report", users,
					processCalculationUtile, data, driver);

			runStep(DismantleName.DELETE_HISTORY,
					() -> DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.DELETE_HISTORY),
					"Dismantle Delete History Report Open", "Failed to open Dismantle Delete History Report", users,
					processCalculationUtile, data, driver);
			runStep(DismantleName.DOWNLOAD_DELETE_HISTORY,
					() -> MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_HISTORY_AND_DELETE_HISTORY.getPath()),
					"Dismantle delete History Report Download", "Failed to Download Dismantle delete History Report",
					users, processCalculationUtile, data, driver);

			runStep(DismantleName.PLAN_JUN,
					() -> DismantleNavigationUtile.openReport(driver, wait, users, DismantleName.PLAN_JUN),
					"Dismantle Plan Jouney Report Open", "Failed to open Dismantle Plan Jouney Report", users,
					processCalculationUtile, data, driver);

			runStep(DismantleName.DOWNLOAD_PLAN_JUN,
					() -> MidsUtile.clickElement(wait, DismantlePath.DOWNLOAD_PLAN_JUN.getPath()),
					"Dismantle Plan Jouney Report Download", "Failed to Download Dismantle Jouney Report", users,
					processCalculationUtile, data, driver);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			verified = false;
		} finally {
			lock.lock();
			try {
				System.out.println(users + " Dismantle Done");
				if (verified) {
					createResultSheet.createResultForDownloadReport("DISMANTLE_Report" + users, data);
				}
			} catch (Exception e) {
				// TODO: handle exceptione.
				e.printStackTrace();
				Thread.currentThread().interrupt();
			} finally {
				lock.unlock();
			}
		}
	}

	private boolean runStep(DismantleName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			String department) {

		process.start(name.name());
		boolean result = step.get();
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		String time = process.getExecutionTime(name.name());
		Integer reportCount = -10;

		try {

			if (result) {
				if (DismantleName.TRACK.equals(name) || DismantleName.DOWNLOAD_TRACK_REPORT.equals(name)
						|| DismantleName.PLAN_JUN.equals(name) || DismantleName.DOWNLOAD_PLAN_JUN.equals(name)) {
					reportCount = getReportCount(DismantlePath.TRACK_AND_PLAN_JUN_COUNT.getPath());
				} else if (DismantleName.DATA_REPORT.equals(name) || DismantleName.DOWNLOAD_DATA_REPORT.equals(name)) {
					reportCount = getReportCount(DismantlePath.DATA_REPORT_COUNT.getPath());
				} else if (DismantleName.HISTORY.equals(name) || DismantleName.DOWNLOAD_HISTORY.equals(name)
						|| DismantleName.DELETE_HISTORY.equals(name)
						|| DismantleName.DOWNLOAD_DELETE_HISTORY.equals(name)) {
					reportCount = getReportCount(DismantlePath.HISTORY_AND_DELETE_COUNT.getPath());
				} else {
					log.info("Not Match Report, Count not get");
				}
			}

			if (reportCount == -1) {
				driver.navigate().back();
				driverConfig.waitForIdle(driver);
				DismantleNavigationUtile.openDismantleInfo(driver, wait, department);
				log.info("Navigate To back");
				result = false;
			}
		} catch (Exception e) {
			log.info("Issue to fetch Counts");
			e.printStackTrace();
		}

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, department, reportCount));
			log.info(name.name() + " -> " + time + " execute");
			return true;
		} else {
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, department, reportCount));
			log.info(name.name() + " -> " + time + " FAILED");
			return false;
		}
	}

	private boolean runStep(DismantleName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			String department, ProcessCalculationUtile processCalculationUtile, List<ProcessData> data,
			WebDriver driver) {

		process.start(name.name());
		boolean result = step.get();
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		String time = process.getExecutionTime(name.name());
		Integer reportCount = -10;

		try {

			if (result) {
				if (DismantleName.TRACK.equals(name) || DismantleName.DOWNLOAD_TRACK_REPORT.equals(name)
						|| DismantleName.PLAN_JUN.equals(name) || DismantleName.DOWNLOAD_PLAN_JUN.equals(name)) {
					reportCount = getReportCount(DismantlePath.TRACK_AND_PLAN_JUN_COUNT.getPath());
				} else if (DismantleName.DATA_REPORT.equals(name) || DismantleName.DOWNLOAD_DATA_REPORT.equals(name)) {
					reportCount = getReportCount(DismantlePath.DATA_REPORT_COUNT.getPath());
				} else if (DismantleName.HISTORY.equals(name) || DismantleName.DOWNLOAD_HISTORY.equals(name)
						|| DismantleName.DELETE_HISTORY.equals(name)
						|| DismantleName.DOWNLOAD_DELETE_HISTORY.equals(name)) {
					reportCount = getReportCount(DismantlePath.HISTORY_AND_DELETE_COUNT.getPath());
				} else {
					log.info("Not Match Report, Count not get");
				}
			}

			if (reportCount == -1) {
				driver.navigate().back();
				driverConfig.waitForIdle(driver);
				DismantleNavigationUtile.openDismantleInfo(driver, wait, department);
				log.info("Navigate To back");
				result = false;
			}
		} catch (Exception e) {
			log.info("Issue to fetch Counts");
			e.printStackTrace();
		}

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, department, reportCount));
			log.info(name.name() + " -> " + time + " execute");
			return true;
		} else {
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, department, reportCount));
			log.info(name.name() + " -> " + time + " FAILED");
			return false;
		}
	}

	private Integer getReportCount(String path) {
		try {
			WebElement element = MidsUtile.getElement(wait, path);
			if (element != null) {
				return Integer.parseInt(element.getText().replaceAll("\\D+", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

}
