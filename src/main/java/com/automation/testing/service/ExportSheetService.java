package com.automation.testing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.ProcessData;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.ExportExcelPath;
import com.automation.testing.enums.NavigateButtonPath;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.enums.ReportCountPath;
import com.automation.testing.interfaces.ReportConstants;
import com.automation.testing.utile.NavigateLocatorUtile;
import com.automation.testing.utile.ProcessCalculationUtile;

@Service
public class ExportSheetService {

	public static final Logger log = LoggerFactory.getLogger(ExportSheetService.class);

	private final DriverConfig driverConfig;

	private final CreateResultSheet createResultSheet;

	public ExportSheetService(DriverConfig driverConfig, CreateResultSheet createResultSheet) {
		this.driverConfig = driverConfig;
		this.createResultSheet = createResultSheet;
	}

	private Map<ProcessName, String> reportName = Map.ofEntries(
			Map.entry(ProcessName.DOWNLOAD_PLAN_TRACKING_REPORT, ReportConstants.MW_PLAN_TRACKING),
			Map.entry(ProcessName.DOWNLOAD_MW_LB_REPORT, ReportConstants.LB_REPORT),
			Map.entry(ProcessName.DOWNLOAD_UBR_LB_REPORT, ReportConstants.UBR_REPORT),
			Map.entry(ProcessName.DOWNLOAD_DPR_REPORT, ReportConstants.MIDS_DPR),
			Map.entry(ProcessName.DOWNLOAD_DEPLOYMENT_ASSIGNMENT_REPORT, ReportConstants.DEPLOYMENT_ASSIGNMENT_HISTORY),
			Map.entry(ProcessName.DOWNLOAD_ASSIGNMENT_REPORT, ReportConstants.ASSIGNMENT_HISTORY),
			Map.entry(ProcessName.DOWNLOAD_PRI_ISSUE_HISTORY_REPORT, ReportConstants.PRI_ISSUE_DATA));

	public void executeAllReports(WebDriver driver, WebDriverWait wait, String user, boolean login,
			ReentrantLock lock) {
		// Local variables - NOT GLOBAL
		ProcessCalculationUtile process = new ProcessCalculationUtile();
		List<ProcessData> data = new ArrayList<>();
		Map<String, Integer> reportCountMap = new HashMap<>();

		try {
			// 1) LOGIN + MW PLAN TRACKING REPORT
			runOpenAndDownload(ProcessName.OPEN_PLAN_TRACKING_REPORT, () -> login, "MW Plan tracking report Open",
					"Failed MW Plan tracking report Open", ProcessName.DOWNLOAD_PLAN_TRACKING_REPORT,
					() -> exportDPRTrackRepotr(wait), "MW Plan tracking report downloaded",
					"Failed MW Plan tracking report downloaded", user, ReportCountPath.MW_PLANING_REPORT.getPath(),
					driver, wait, process, data, reportCountMap);

			Thread.sleep(2000);
			// 2) MW LB REPORT
			runOpenAndDownload(ProcessName.OPEN_MW_LB_REPORT, () -> {
				boolean b = true;
				if (user.equals(Department.CIRCLE_OPERATION_TEAM.getName())
						|| user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
					b &= NavigateLocatorUtile.navigateToLocation(driver, wait,
							NavigateButtonPath.LB_BUTTON_FOR_OPS.getPath());
					Thread.sleep(500);
					b &= NavigateLocatorUtile.navigateToLocation(driver, wait,
							NavigateButtonPath.MW_LB_BUTTON_OPS.getPath());
				} else {
					b = NavigateLocatorUtile.navigateToLBReport(driver, wait);
				}
				return b;
			}, "MW LB Report opened", "Failed to open MW LB Report", ProcessName.DOWNLOAD_MW_LB_REPORT,
					() -> downlaodLBReportMW(wait), "MW LB Report downloaded", "Failed to download MW LB Report", user,
					ReportCountPath.LB_REPORTS.getPath(), driver, wait, process, data, reportCountMap);
			Thread.sleep(2000);
			// 3) UBR LB REPORT
			runOpenAndDownload(ProcessName.OPEN_UBR_LB_REPORT, () -> {
				boolean b = false;
				if (user.equals(Department.CIRCLE_OPERATION_TEAM.getName())
						|| user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
					b = NavigateLocatorUtile.navigateToLocation(driver, wait,
							NavigateButtonPath.UBR_LB_BUTTON_OPS.getPath());
				} else {
					b = NavigateLocatorUtile.navigateToLBReportUBR(driver, wait);
				}
				return b;
			}, "UBR LB Report opened", "Failed to open UBR LB Report", ProcessName.DOWNLOAD_UBR_LB_REPORT,
					() -> downloadLBReportUBR(wait), "UBR LB Report downloaded", "Failed to download UBR LB Report",
					user, ReportCountPath.URB_LB_REPORT.getPath(), driver, wait, process, data, reportCountMap);
			Thread.sleep(2000);
			// 4) DPR REPORT
			runOpenAndDownload(ProcessName.OPEN_DPR_REPORT, () -> {
				boolean b = true;
				if (user.equals(Department.CIRCLE_OPERATION_TEAM.getName())
						|| user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
					b &= NavigateLocatorUtile.navigateToLocation(driver, wait,
							NavigateButtonPath.DEPLOYMENT_REPORT_FOR_OPS.getPath());
					Thread.sleep(500);
					b &= NavigateLocatorUtile.navigateToLocation(driver, wait,
							NavigateLocatorUtile.getCustomPathDeploymentReports(1));
				} else if (user.equals(Department.CIRCLE_MW_PLANNER.getName())) {
					b = NavigateLocatorUtile.navigateToDPRReport(driver, wait);
				} else {
					b &= NavigateLocatorUtile.navigateToLocation(driver, wait,
							NavigateButtonPath.DEPLOYMENT_REPORT_FOR_DINC.getPath());
					Thread.sleep(500);
					b &= NavigateLocatorUtile.navigateByDepAndDinc(driver, wait, 1);
				}
				return b;
			}, "DPR Report opened", "Failed to open DPR Report", ProcessName.DOWNLOAD_DPR_REPORT,
					() -> downloadDPRReport(wait), "DPR Report downloaded", "Failed to download DPR Report", user,
					ReportCountPath.DPR_AND_ASSIGNMENT_REPORT.getPath(), driver, wait, process, data, reportCountMap);

			// 5) DEPLOYMENT ASSIGNMENT REPORT
			if (!user.equals(Department.CIRCLE_OPERATION_TEAM.getName())
					|| user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
				runOpenAndDownload(ProcessName.OPEN_DEPLOYMENT_ASSIGNMENT_REPORT, () -> {
					boolean b = false;
					if (user.equals(Department.CIRCLE_OPERATION_TEAM.getName())
							|| user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
						b = NavigateLocatorUtile.navigateToLocation(driver, wait,
								NavigateLocatorUtile.getCustomPathDeploymentReports(3));
					} else if (user.equals(Department.CIRCLE_MW_PLANNER.getName())) {
						b = NavigateLocatorUtile.navigateToDeployAssignReport(driver, wait);
					} else {
						b = NavigateLocatorUtile.navigateByDepAndDinc(driver, wait, 3);
					}
					return b;
				}, "Deployment Assignment Report opened", "Failed to open Deployment Assignment Report",
						ProcessName.DOWNLOAD_DEPLOYMENT_ASSIGNMENT_REPORT, () -> downloadDeployAssignReport(wait),
						"Deployment Assignment Report downloaded", "Failed to download Deployment Assignment Report",
						user, ReportCountPath.DEPLOYMENT_AND_PRI_REPORT.getPath(), driver, wait, process, data,
						reportCountMap);
			}

			// 6) PRI ISSUE HISTORY REPORT
			if (!user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
				runOpenAndDownload(ProcessName.OPEN_PRI_ISSUE_HISTORY_REPORT, () -> {
					boolean b = false;
					if (user.equals(Department.CIRCLE_OPERATION_TEAM.getName())) {
						b = NavigateLocatorUtile.navigateToLocation(driver, wait,
								NavigateLocatorUtile.getCustomPathDeploymentReports(3));
					} else if (user.equals(Department.CIRCLE_MW_PLANNER.getName())) {
						b = NavigateLocatorUtile.navigateToPRIssueReport(driver, wait);
					} else {
						b = NavigateLocatorUtile.navigateByDepAndDinc(driver, wait, 4);
					}
					return b;
				}, "PRI Issue Report opened", "Failed to open PRI Issue Report",
						ProcessName.DOWNLOAD_PRI_ISSUE_HISTORY_REPORT, () -> downloadPRIusseHistoryReport(wait),
						"PRI Issue History Report downloaded", "Failed to download PRI Issue History Report", user,
						ReportCountPath.DEPLOYMENT_AND_PRI_REPORT.getPath(), driver, wait, process, data,
						reportCountMap);
			}

			// 7) ASSIGNMENT REPORT
			if (!user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
				runOpenAndDownload(ProcessName.OPEN_ASSIGNMENT_REPORT, () -> {
					boolean b = false;
					if (user.equals(Department.CIRCLE_OPERATION_TEAM.getName())) {
						b = NavigateLocatorUtile.navigateToLocation(driver, wait,
								NavigateLocatorUtile.getCustomPathDeploymentReports(4));
					} else if (user.equals(Department.CIRCLE_MW_PLANNER.getName())) {
						b = NavigateLocatorUtile.navigateToAssignmentReport(driver, wait);
					} else {
						b = NavigateLocatorUtile.navigateByDepAndDinc(driver, wait, 5);
					}
					return b;
				}, "Assignment Report opened", "Failed to open Assignment Report",
						ProcessName.DOWNLOAD_ASSIGNMENT_REPORT, () -> downloadDeployAssignReport(wait),
						"Assignment Report downloaded", "Failed to download Assignment Report", user,
						ReportCountPath.DPR_AND_ASSIGNMENT_REPORT.getPath(), driver, wait, process, data,
						reportCountMap);
			}

			// 8) SIFT AT REPORTS
			verifySoftAtReports(user, driver, wait, process, data, reportCountMap);

			try {
				// verify count of all reports
				final String errorReport = verifyReportCountValidOrNot(reportCountMap);
				runStep(ProcessName.DATA_FILLED, () -> errorReport.isEmpty(), "All Report Count is Valid",
						"[" + errorReport + "] has invalid count", user, driver, process, data);
				reportCountMap.clear();
			} catch (Exception e) {
				log.error(user+" : Error verifying report count", e);
			}

			Thread.sleep(500);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(user + " : FATAL ERROR OCCURRED WHILE EXECUTING ALL REPORTS", e);
		} finally {
			lock.lock();
			try {
				createResultSheet.createResultForDownloadReport("DOWNLOAD_NEW_DEP_REPORTS_" + user, data);
				log.info(user + " REPORT GENERATED");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Thread.currentThread().interrupt();
			} finally {
				lock.unlock();
			}
		}
	}

	public void verifySoftAtReports(String user, WebDriver driver, WebDriverWait wait, ProcessCalculationUtile process,
			List<ProcessData> data, Map<String, Integer> reportCountMap) {

		// SOFT AT REPORTS (ERICSSON)
		runOpenAndDownload(ProcessName.OPEN_ERICSSON_AT, () -> {
			boolean b = false;
			if (user.equals(Department.CIRCLE_OPERATION_TEAM.getName())
					|| user.equals(Department.CIRCLE_MS_PARTNER.getName())) {
				b = NavigateLocatorUtile.navigateToLocation(driver, wait,
						NavigateLocatorUtile.getCustomPathDeploymentReports(2));
			} else if (user.equals(Department.CIRCLE_MW_PLANNER.getName())) {
				b = NavigateLocatorUtile.navigateToSoftAtReport(driver, wait);
			} else {
				b = NavigateLocatorUtile.navigateByDepAndDinc(driver, wait, 2);
			}
			return b;
		}, "ERICSSON AT Report Open", "Failed to ERICSSON AT Report Open", ProcessName.DOWNLOAD_ERICSSON_AT,
				() -> downloadSoftAtReport(wait), "ERICSSON AT Report downloaded",
				"Failed to download ERICSSON AT Report", user, ReportCountPath.SOFT_AT_REPORT.getPath(), driver, wait,
				process, data, reportCountMap);

		// CERAGON AT
		runOpenAndDownload(ProcessName.OPEN_CERAGON_AT,
				() -> NavigateLocatorUtile.navigateToSoftAtReport(driver, wait, 2), "Ceragon AT Report Open",
				"Failed to Ceragon AT Report Open", ProcessName.DOWNLOAD_CERAGON_AT, () -> downloadSoftAtReport(wait),
				"Ceragon AT Report downloaded", "Failed to download Ceragon AT Report", user,
				ReportCountPath.SOFT_AT_REPORT.getPath(), driver, wait, process, data, reportCountMap);

		// HUAWEI AT
		runOpenAndDownload(ProcessName.OPEN_HUAWEI_AT,
				() -> NavigateLocatorUtile.navigateToSoftAtReport(driver, wait, 3), "HUAWEI AT Report Open",
				"Failed to HUAWEI AT Report Open", ProcessName.DOWNLOAD_HUAWEI_AT, () -> downloadSoftAtReport(wait),
				"HUAWEI AT Report downloaded", "Failed to download HUAWEI AT Report", user,
				ReportCountPath.SOFT_AT_REPORT.getPath(), driver, wait, process, data, reportCountMap);

		// AVIAT AT
		runOpenAndDownload(ProcessName.OPEN_AVIAT_AT,
				() -> NavigateLocatorUtile.navigateToSoftAtReport(driver, wait, 4), "AVIAT AT Report Open",
				"Failed to AVIAT AT Report Open", ProcessName.DOWNLOAD_AVIAT_AT, () -> downloadSoftAtReport(wait),
				"AVIAT AT Report downloaded", "Failed to download AVIAT AT Report", user,
				ReportCountPath.SOFT_AT_REPORT.getPath(), driver, wait, process, data, reportCountMap);

		// NOKIA
		runOpenAndDownload(ProcessName.OPEN_NOKIA_AT,
				() -> NavigateLocatorUtile.navigateToSoftAtReport(driver, wait, 5), "NOKIA AT Report Open",
				"Failed to NOKIA AT Report Open", ProcessName.DOWNLOAD_NOKIA_AT, () -> downloadSoftAtReport(wait),
				"NOKIA AT Report downloaded", "Failed to download NOKIA AT Report", user,
				ReportCountPath.SOFT_AT_REPORT.getPath(), driver, wait, process, data, reportCountMap);
	}

	private Integer getReportCount(String path, WebDriverWait wait) {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(path)));
			return Integer.parseInt(element.getText().replaceAll("\\D+", ""));
		} catch (Exception e) {
			log.error("Error getting report count", e);
		}
		return -1;
	}

	public int getIndexByRemark(String user, String reportName, List<ProcessData> data) {
		try {
			for (int idx = 0; idx < data.size(); idx++) {
				ProcessData i = data.get(idx);
				if (i.getUserDepartment().equals(user) && i.getName().equals(reportName)) {
					return idx;
				}
			}
		} catch (Exception e) {
			log.error("Error finding index by remark", e);
		}
		return -1;
	}

	private String verifyReportCountValidOrNot(Map<String, Integer> reportCountMap) {
		try {
			log.info("Count Map = " + reportCountMap);

			Integer dpr = reportCountMap.get("OPEN_DPR_REPORT");
			Integer lb = reportCountMap.get("OPEN_MW_LB_REPORT");
			Integer dep = reportCountMap.get("OPEN_DEPLOYMENT_ASSIGNMENT_REPORT");
			Integer plan = reportCountMap.get("OPEN_PLAN_TRACKING_REPORT");

			Map<String, Integer> map = Map.of("DPR_REPORT", dpr, "MW_LB_REPORT", lb, "DEPLOYMENT_ASSIGNMENT_REPORT",
					dep, "PLAN_TRACKING_REPORT", plan);

			// Remove null entries (safety)
			map = map.entrySet().stream().filter(e -> e.getValue() != null)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));

			if (map.isEmpty())
				return "";

			// Count frequency
			Map<Integer, Long> freq = map.values().stream()
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

			// Case 1: All same
			if (freq.size() == 1) {
				return "";
			}

			// Case 2: 3 same + 1 different
			for (Map.Entry<Integer, Long> entry : freq.entrySet()) {
				if (entry.getValue() == 1) {
					int invalidCount = entry.getKey();

					String invalidReport = map.entrySet().stream().filter(e -> e.getValue().equals(invalidCount))
							.map(Map.Entry::getKey).map(name -> name.replaceFirst("^OPEN_", "")).findFirst().orElse("");

					return invalidReport;
				}
			}

			// Case 3: All other → all invalid
			return map.keySet().stream().map(name -> name.replaceFirst("^OPEN_", "")).collect(Collectors.joining(", "));

		} catch (Exception e) {
			log.error("Error verifying report count");
			return "";
		}
	}

	private boolean exportDPRTrackRepotr(WebDriverWait wait) {
		boolean isDownload = false;
		String downloadButtonPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout[1]/vaadin-button[1]";

		try {
			WebElement download = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(downloadButtonPath)));
			download.click();
			isDownload = true;
		} catch (Exception e) {
			log.error("Error exporting DPR tracking report", e);
		}

		return isDownload;
	}

	private boolean clickOnButton(String path, WebDriverWait wait) {
		try {
			WebElement download = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(path)));
			download.click();
			return true;
		} catch (Exception e) {
			log.error("Error clicking button at path: " + path, e);
		}
		return false;
	}

	private boolean downlaodLBReportMW(WebDriverWait wait) {
		return clickOnButton(ExportExcelPath.MW_LB_REPORT_DOWNLOAD_PATH.getPath(), wait);
	}

	private boolean downloadLBReportUBR(WebDriverWait wait) {
		return clickOnButton(ExportExcelPath.UBR_BL_REPORT_DOWNLOAD_PATH.getPath(), wait);
	}

	private boolean downloadDPRReport(WebDriverWait wait) {
		return clickOnButton(ExportExcelPath.DPR_REPORT_DOWNLOAD_PATH.getPath(), wait);
	}

	private boolean downloadDeployAssignReport(WebDriverWait wait) {
		return clickOnButton(ExportExcelPath.DEPLOYMENT_ASSIGNMENT_REPORT_DOWNLOAD_PATH.getPath(), wait);
	}

	private boolean downloadPRIusseHistoryReport(WebDriverWait wait) {
		return clickOnButton(ExportExcelPath.PRI_ISSUE_HISTORY_DOWNLOAD_PATH.getPath(), wait);
	}

	private boolean downloadSoftAtReport(WebDriverWait wait) {
		return clickOnButton(ExportExcelPath.SOFT_AT_REPORT_DOWNLOAD.getPath(), wait);
	}

	private void runOpenAndDownload(ProcessName openProcess, Callable<Boolean> openAction, String openSuccessMsg,
			String openFailMsg, ProcessName downloadProcess, Callable<Boolean> downloadAction,
			String downloadSuccessMsg, String downloadFailMsg, String department, String countPath, WebDriver driver,
			WebDriverWait wait, ProcessCalculationUtile process, List<ProcessData> data,
			Map<String, Integer> reportCountMap) {

		// OPEN
		boolean openSuccess = runStep(openProcess, () -> {
			boolean result = false;
			try {
				result = openAction.call();
			} catch (Exception e) {
				log.error(department + " : Error opening report", e);
			}
			driverConfig.waitForIdle(driver);
			return result;
		}, openSuccessMsg, openFailMsg, department, countPath, driver, wait, process, data, reportCountMap);

		if (!openSuccess) {
			log.error(openFailMsg + " — SKIPPING DOWNLOAD");
			return;
		}

		// DOWNLOAD
		boolean downloadSuccess = runStep(downloadProcess, () -> {
			boolean result = false;
			try {
				result = downloadAction.call();
			} catch (Exception e) {
				log.error(department + " : Error downloading report", e);
			}
			driverConfig.waitForIdle(driver);
			return result;
		}, downloadSuccessMsg, downloadFailMsg, department, countPath, driver, wait, process, data, reportCountMap);

		if (!downloadSuccess) {
			log.error(downloadFailMsg);
		} else {
			// ADD REPORT FIND REQUEST
			if (reportName.get(downloadProcess) != null) {
				// ReportTrackingService.createRequest(downloadProcess,
				// reportName.get(downloadProcess),
				// reportTrackingMap);
			}
		}
	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			String department, String reportCountPath, WebDriver driver, WebDriverWait wait,
			ProcessCalculationUtile process, List<ProcessData> data, Map<String, Integer> reportCountMap) {

		process.start(name.name());
		boolean result = step.get();
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		Integer i = -1;
		if (result) {
			i = getReportCount(reportCountPath, wait);
			reportCountMap.put(name.toString(), i);
		}

		String time = process.getExecutionTime(name.name());

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, department, i));
			log.info(name.name() + " -> " + time + " execute");
			return true;
		} else {
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, department, i));
			log.info(name.name() + " -> " + time + " FAILED");
			return false;
		}
	}

	private boolean runStep(ProcessName name, Supplier<Boolean> step, String successMsg, String errorMsg,
			String department, WebDriver driver, ProcessCalculationUtile process, List<ProcessData> data) {

		process.start(name.name());
		boolean result = step.get();
		driverConfig.waitForIdle(driver);
		process.end(name.name());

		String time = process.getExecutionTime(name.name());

		if (result) {
			data.add(createResultSheet.createResultData(name.name(), time, true, successMsg, department, 0));
			log.info(name.name() + " -> " + time + " execute");
			return true;
		} else {
			data.add(createResultSheet.createResultData(name.name(), time, false, errorMsg, department, 0));
			log.info(name.name() + " -> " + time + " FAILED");
			return false;
		}
	}

}