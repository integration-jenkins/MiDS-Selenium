package com.automation.testing.traffic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.automation.testing.enums.TrafficServiceName;
import com.automation.testing.enums.TrafficShiftingPath;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.UserService;
import com.automation.testing.sourcecredentials.UserDetails;
import com.automation.testing.testcase.DownloadReportTest;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.NavigateLocatorUtile;
import com.automation.testing.utile.ProcessCalculationUtile;
import com.automation.testing.utile.TsNavigateUtile;

@Component
public class DownloadTrafficShiftingReports {

	private static final Logger log = LoggerFactory.getLogger(DownloadReportTest.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final UserService userService;
	private final DriverConfig driverConfig;
	private final CreateResultSheet createResultSheet;

	public DownloadTrafficShiftingReports(WebDriver driver, WebDriverWait wait, UserService userService,
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
		List<String> userList = UserDetails.getUserDepListForTS();
		try {

			for (String user : userList) {
				// user login
				log.info(user + " user login");

				runStep(TrafficServiceName.USER_LOGIN, () -> userService.userLogin(user), "User able to login",
						"Issue In login", user);

				executeTsReports(user);

				createResultSheet.createResultForDownloadReport("TS_Download_report", data);
				// logout;
				userService.logOut(driver, wait);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.info("Downloading Process Intrupted");
		} finally {
			createResultSheet.createResultForDownloadReport("TS_Download_report", data);
			log.info("Record is generated");
		}
	}

	private void executeTsReports(String user) {
		// 1) OPEN TRAFFIC REPORT done
		runStep(TrafficServiceName.OPEN_TRAFFIC_REPORT,
				() -> TsNavigateUtile.navigateToTsPage(driver, wait, user) && TsNavigateUtile.navigateToReport(driver,
						wait, user, TrafficShiftingPath.TRAFFIC_REPORT.getPath()),
				"Traffic Report Open", "Failed to open Traffic Report", user);

		// 1.1) DOWNLOAD TRAFFIC REPORT done
		runStep(TrafficServiceName.DOWNLOAD_TRAFFIC_REPORT, () -> {
			return MidsUtile.clickElement(wait, TrafficShiftingPath.DOWNLOAD_TRAFFIC_REPORT.getPath());
		}, "Traffic Report Download", "Failed to download Traffic Report", user);

		// 2) OPEN TRAFFIC ASSIGNMENT HISTORY
		runStep(TrafficServiceName.OPEN_TRAFFIC_ASSIGNMENT_HISTORY,
				() -> TsNavigateUtile.navigateToReport(driver, wait, user,
						TrafficShiftingPath.TS_ASSIGNMENT_HISTORY.getPath()),
				"Traffic Assignment History Report Open", "Failed to Open  Traffic Assignment History Report", user);

		// 2.1) DOWNLOAD TRAFFIC ASSIGNMENT HISTORY
		runStep(TrafficServiceName.DOWNLOAD_TRAFFIC_ASSIGNMENT_HISTORY,
				() -> MidsUtile.clickElement(wait, TrafficShiftingPath.ASSIGNMENT_REPORT_DOW_PATH.getPath()),
				"Traffic Assignment History Repor Report Download",
				"Failed to download Traffic Assignment History Repor Report", user);

		// 3) OPEN TRAFFIC SHIFTING DELETION REPORT
		runStep(TrafficServiceName.OPEN_TRAFFIC_SHIFTING_DELETION,
				() -> TsNavigateUtile.navigateToReport(driver, wait, user, TrafficShiftingPath.TS_DELECTION.getPath()),
				"Traffic DELETION Report Open", "Failed to Open Traffic DELETION Report", user);

		// 3.1) DOWNLOAD TRAFFIC SHIFTING DELETION REPORT
		runStep(TrafficServiceName.DOWNLOAD_TRAFFIC_SHIFTING_DELETION,
				() -> MidsUtile.clickElement(wait, TrafficShiftingPath.TRAFFIC_AND_DEL_REPORT_DOW_PATH.getPath()),
				"Traffic DELETION Report Download", "Failed to Download Traffic DELETION Report", user);

		// 4) OPEN TRAFFIC TRACK VIEW REPORT
		runStep(TrafficServiceName.OPEN_TRAFFIC_TRACK_VIEW,
				() -> TsNavigateUtile.navigateToReport(driver, wait, user,
						TrafficShiftingPath.TRAFFIC_TRACK_VIEW.getPath()),
				"Traffic TRACK View Report Open", "Failed to Open Traffic Track View Report", user);

		// 4.1) DOWNLOAD TRAFFIC TRACK VIEW REPORT
		runStep(TrafficServiceName.DOWNLOAD_TRAFFIC_TRACK_VIEW,
				() -> TsNavigateUtile.navigateToTrackViewBulkOptions(driver, wait, 1),
				"Traffic TRACK View Report Download", "Failed to Download Traffic TRACK View Report", user);
	}

	private Integer getReportCount(String path) {
		try {
			WebElement element = MidsUtile.getElement(wait, path);
			if (element != null) {
				return Integer.parseInt(element.getText().replaceAll("\\D+", ""));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}

	private boolean runStep(TrafficServiceName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			String department) {

		try {

			process.start(name.name());
			boolean result = step.get(); // run the actual process
			driverConfig.waitForIdle(driver);
			process.end(name.name());

			String time = process.getExecutionTime(name.name());
			Integer reportCount = -2;

			if (result) {
				if (name.name().equals(TrafficServiceName.OPEN_TRAFFIC_ASSIGNMENT_HISTORY.name())
						|| name.name().equals(TrafficServiceName.DOWNLOAD_TRAFFIC_ASSIGNMENT_HISTORY.name())) {
					reportCount = getReportCount(TrafficShiftingPath.ASSIGNMENT_REPORT_COUNT_PATH.getPath());
					if (reportCount == -1)
						result = false;
				} else if (name.name().equals(TrafficServiceName.USER_LOGIN.name())) {
					reportCount = 0;
				} else if (name.name().equals(TrafficServiceName.OPEN_TRAFFIC_TRACK_VIEW.name())
						|| name.name().equals(TrafficServiceName.DOWNLOAD_TRAFFIC_TRACK_VIEW.name())) {
					reportCount = getReportCount(TrafficShiftingPath.TRAFIC_TRACK_VIEW_COUNT_PATH.getPath());
					if (reportCount == -1)
						result = false;
				} else {
					reportCount = getReportCount(TrafficShiftingPath.DEL_REPORT_COUNT_PATH.getPath());
					if (reportCount == -1)
						result = false;
				}
				log.info(name.name() + " -> " + reportCount);
				if (reportCount == -1) {
					driver.navigate().back();
					driverConfig.waitForIdle(driver);
					log.info("Some Issue Navigate BACK");
					TsNavigateUtile.navigateToTsPage(driver, wait, department);
				}
			}

			if (result) {
				data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, department,
						reportCount));
				log.info(name.name() + " -> " + time + " execute");
				return true; // continue next step
			} else {
				data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, department,
						reportCount));
				log.info(name.name() + " -> " + time + " FAILED");
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private boolean runStep(TrafficServiceName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			String department, ProcessCalculationUtile process, List<ProcessData> data, WebDriver driver) {

		try {

			process.start(name.name());
			boolean result = step.get(); // run the actual process
			driverConfig.waitForIdle(driver);
			process.end(name.name());

			String time = process.getExecutionTime(name.name());
			Integer reportCount = -2;

			if (result) {
				if (name.name().equals(TrafficServiceName.OPEN_TRAFFIC_ASSIGNMENT_HISTORY.name())
						|| name.name().equals(TrafficServiceName.DOWNLOAD_TRAFFIC_ASSIGNMENT_HISTORY.name())) {
					reportCount = getReportCount(TrafficShiftingPath.ASSIGNMENT_REPORT_COUNT_PATH.getPath());
					if (reportCount == -1)
						result = false;
				} else if (name.name().equals(TrafficServiceName.USER_LOGIN.name())) {
					reportCount = 0;
				} else if (name.name().equals(TrafficServiceName.OPEN_TRAFFIC_TRACK_VIEW.name())
						|| name.name().equals(TrafficServiceName.DOWNLOAD_TRAFFIC_TRACK_VIEW.name())) {
					reportCount = getReportCount(TrafficShiftingPath.TRAFIC_TRACK_VIEW_COUNT_PATH.getPath());
					if (reportCount == -1)
						result = false;
				} else {
					reportCount = getReportCount(TrafficShiftingPath.DEL_REPORT_COUNT_PATH.getPath());
					if (reportCount == -1)
						result = false;
				}
				log.info(name.name() + " -> " + reportCount);
				if (reportCount == -1) {
					driver.navigate().back();
					driverConfig.waitForIdle(driver);
					log.info("Some Issue Navigate BACK");
					TsNavigateUtile.navigateToTsPage(driver, wait, department);
				}
			}

			if (result) {
				data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, department,
						reportCount));
				log.info(name.name() + " -> " + time + " execute");
				return true; // continue next step
			} else {
				data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, department,
						reportCount));
				log.info(name.name() + " -> " + time + " FAILED");
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

}
