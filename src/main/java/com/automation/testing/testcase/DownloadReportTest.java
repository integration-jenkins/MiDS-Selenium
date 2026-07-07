package com.automation.testing.testcase;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.ReportExeutionData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.service.ExportSheetService;
import com.automation.testing.service.ReportTrackingService;
import com.automation.testing.service.UserService;
import com.automation.testing.sourcecredentials.UserDetails;

@Component
public class DownloadReportTest {

	private static final Logger log = LoggerFactory.getLogger(DownloadReportTest.class);

	private final ExportSheetService exportSheetService;
	private final ReportTrackingService reportTrackingService;
	private final UserService userService;
	private final WebDriver driver;
	private final WebDriverWait wait;
	private final DriverConfig driverConfig;

	private Map<ProcessName, ReportExeutionData> reportTrackingMap = new ConcurrentHashMap<>();

	public DownloadReportTest(ExportSheetService exportSheetService, ReportTrackingService reportTrackingService,
			UserService userService, WebDriverWait wait, WebDriver driver, DriverConfig driverConfig) {
		this.exportSheetService = exportSheetService;
		this.reportTrackingService = reportTrackingService;
		this.userService = userService;
		this.driver = driver;
		this.wait = wait;
		this.driverConfig = driverConfig;
	}

//	public void run() {
//
//		log.info("Start download test");
//
//		List<String> users = List.of(
//				Department.CIRCLE_MW_PLANNER.getName(),
//				Department.CIRCLE_DEPLOYMENT_TEAM.getName()
//				,Department.CIRCLE_DINC_PARTNER.getName(),
//				Department.CIRCLE_OPERATION_TEAM.getName(),
//				Department.CIRCLE_MS_PARTNER.getName()
//				);
//
//		for (String user : users) {
//
//			reportTrackingMap.clear();
//
//			userService.userLogin(user);
//
//			reportTrackingService.bind(reportTrackingMap);
//			reportTrackingService.start(); // START VERIFY THREAD
//
//			exportSheetService.executeAllReports(driver,wait,user, true, reportTrackingMap);
//
//			reportTrackingService.pause(); // PAUSE VERIFY THREAD
//
//			userService.logOut(driver, wait);
//
//			reportTrackingService.removeDownloadFiles();
//			driverConfig.refreshDriver(driver);
//		}
//
//		reportTrackingService.stop();
//	}

}
