package com.automation.testing.testcase;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.data.ProcessData;
import com.automation.testing.dismantle.DownloadDismantleReports;
import com.automation.testing.enums.Department;
import com.automation.testing.enums.ProcessName;
import com.automation.testing.enums.TrafficServiceName;
import com.automation.testing.enums.TrafficShiftingPath;
import com.automation.testing.interfaces.CommanProcessName;
import com.automation.testing.service.CreateResultSheet;
import com.automation.testing.service.ExportSheetService;
import com.automation.testing.service.ReportTrackingService;
import com.automation.testing.service.UserService;
import com.automation.testing.sourcecredentials.UserDetails;
import com.automation.testing.traffic.DownloadTrafficShiftingReports;
import com.automation.testing.utile.MidsUtile;
import com.automation.testing.utile.ProcessCalculationUtile;
import com.automation.testing.utile.TsNavigateUtile;

@Component
public class VerifyAllPages {

	private static final Logger log = LoggerFactory.getLogger(VerifyNewDeploymentReports.class);

	private final UserService userService;
	private final DriverConfig driverConfig;
	private final ExportSheetService exportSheetService;
	private final CreateResultSheet createResultSheet;
	private final DownloadTrafficShiftingReports downloadTrafficShiftingReports;
	private final DownloadDismantleReports dismantleReports;

	public VerifyAllPages(ReportTrackingService reportTrackingService, UserService userService,
			DriverConfig driverConfig, ExportSheetService exportSheetService, CreateResultSheet createResultSheet,
			DownloadTrafficShiftingReports downloadTrafficShiftingReports, DownloadDismantleReports dismantleReports) {
		this.userService = userService;
		this.driverConfig = driverConfig;
		this.exportSheetService = exportSheetService;
		this.createResultSheet = createResultSheet;
		this.downloadTrafficShiftingReports = downloadTrafficShiftingReports;
		this.dismantleReports = dismantleReports;
	}

	private final ExecutorService executorService = Executors.newFixedThreadPool(5);
	private ProcessCalculationUtile process = new ProcessCalculationUtile();
	private final Integer FAILED = -100;

	private List<String> users = List.of(Department.CIRCLE_MW_PLANNER.getName(),
			Department.CIRCLE_DEPLOYMENT_TEAM.getName(), Department.CIRCLE_DINC_PARTNER.getName(),
			Department.CIRCLE_OPERATION_TEAM.getName(), Department.CIRCLE_MS_PARTNER.getName());

	public void run() {
		log.info("ALL verification Start ");
		process.start("REPPORT_VERIFY");
		ReentrantLock lock = new ReentrantLock();

		for (String user : users) {
			executorService.submit(() -> {
				WebDriver driver = driverConfig.createNewBrowser();
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
				List<ProcessData> data = new ArrayList<>();
				ProcessCalculationUtile process = new ProcessCalculationUtile();

				lock.lock();
				try {

					log.info(user + " start to verify reports");
					runStep(ProcessName.USER_LOGIN, () -> {
						boolean b = userService.userLogin(user, driver, wait);
						return b == true ? 0 : FAILED;
					}, "User Login", "Failed To Login", user, driver, process, data);

				} catch (Exception e) {
					Thread.currentThread().interrupt();
				} finally {
					lock.unlock();
				}

				exportSheetService.executeAllReports(driver, wait, user, true, lock);

				if (user.equals(Department.CIRCLE_MW_PLANNER.getName())) {
					popInfo(wait, driver, user, data);

					verifyNepDismantleReport(driver, wait, user, data);

					frequencyInfo(driver, wait, user, data);
				}

				verifyPages(wait, driver, user, data);

				lock.lock();
				try {
					createResultSheet.createResultForDownloadReport(user + "Verify_ALL_PAGES", data);
					log.info("Report Generated...");
				} catch (Exception e) {
					// TODO: handle exception
					Thread.currentThread().interrupt();
				} finally {
					lock.unlock();
				}

				userService.logOut(driver, wait);
				driver.close();
				log.info(user + " Finished there work");

			});

		}
		try {
			executorService.awaitTermination(30, TimeUnit.MINUTES);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		process.end("REPPORT_VERIFY");

		log.info("End = " + process.getExecutionTime("REPPORT_VERIFY"));

	}

	private List<String> getReportForUser(String dep) {
		switch (dep) {
		case "CIRCLE MW PLANNER":
			return Arrays.asList("RFC Report", "Plan Upload", "Soft AT Upload", "MIDS DPR Upload",
					"Change Assign User");

		case "CIRCLE DEPLOYMENT TEAM":
		case "CIRCLE MS PARTNER":
			return Arrays.asList("RFC Report", "Soft AT Upload", "MIDS DPR Upload", "Change Assign User");

		case "CIRCLE OPERATION TEAM":
		case "CIRCLE DINC PARTNER":
			return Arrays.asList("RFC Report", "Soft AT Upload", "MIDS DPR Upload");
		default:
			return Arrays.asList();
		}
	}

	private void verifyPages(WebDriverWait wait, WebDriver driver, String user, List<ProcessData> data) {
		try {

			for (String page : getReportForUser(user)) {

				runStep(ProcessName.PAGES, () -> {
					try {
						// Click POP Info Report
						WebElement pageElement = wait.until(ExpectedConditions
								.visibilityOfElementLocated(By.xpath("//span[normalize-space()='" + page + "']")));

						// Scroll + JS click (stable for overlay/menu issues)
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});",
								pageElement);

						((JavascriptExecutor) driver).executeScript("arguments[0].click();", pageElement);
						driverConfig.waitForIdle(driver);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						System.out.println("Report Not open " + page);
						driverConfig.refreshDriver(driver);
						return FAILED;
					}
					return 0;
				}, page + " is Verified", page + " Failed Issue", user, driver, process, data);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void frequencyInfo(WebDriver driver, WebDriverWait wait, String user, List<ProcessData> data) {

		runStep(ProcessName.FREQUENCY_DETAIL_REPORT, () -> {
			Integer counts = 0;
			try {
				wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Frequency Info']")))
						.click();
				driverConfig.waitForIdle(driver);

				// Click POP Info Report
				WebElement popInfo = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//span[normalize-space()='Frequency Detail Report']")));

				// Scroll + JS click (stable for overlay/menu issues)
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", popInfo);

				((JavascriptExecutor) driver).executeScript("arguments[0].click();", popInfo);
				driverConfig.waitForIdle(driver);

				String download = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button[1]";
				String count = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/span";

				counts = verifyExportAndCount(wait, count, download);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return FAILED;
			}
			return counts;
		}, "FREQUENCY DETAIL REPORT verified", "Issue in FREQUENCY DETAIL REPORT", user, driver, process, data);

	}

	private Integer verifyExportAndCount(WebDriverWait wait, String countPath, String exportPath) {
		MidsUtile.clickElement(wait, exportPath);
		WebElement elm = MidsUtile.getElement(wait, countPath);
		return getReportCount(elm.getText());
	}

	private boolean popInfo(WebDriverWait wait, WebDriver driver, String user, List<ProcessData> data) {
		try {
			runStep(ProcessName.POP_INFO_PAGE, () -> {
				Integer cout = 0;
				try {
					// Click POP Related Report
					wait.until(ExpectedConditions
							.elementToBeClickable(By.xpath("//span[normalize-space()='POP Related Report']"))).click();
					driverConfig.waitForIdle(driver);

					// Click POP Info Report
					WebElement popInfo = wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath("//span[normalize-space()='POP Info Report']")));

					// Scroll + JS click (stable for overlay/menu issues)
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});",
							popInfo);

					((JavascriptExecutor) driver).executeScript("arguments[0].click();", popInfo);

					driverConfig.waitForIdle(driver);

					// Wait and click download link
					WebElement download = wait.until(
							ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href,'Pop%20Info.xls')]")));

					download.click();
					driverConfig.waitForIdle(driver);
					String countPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/span";
					WebElement elm = MidsUtile.getElement(wait, countPath);
					cout = getReportCount(elm.getText());
					System.out.print("Pop count = " + cout);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return FAILED;
				}
				return cout;
			}, "Pop Info Verified", "Issue in Pop Info Page", user, driver, process, data);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean verifyNepDismantleReport(WebDriver driver, WebDriverWait wait, String user,
			List<ProcessData> data) {
		runStep(ProcessName.NEP_DISMANTLE_LINK_REPORT, () -> {
			Integer count = 0;
			try {
				// first report
				wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//span[normalize-space()='NEP Dismantle Reports']"))).click();
				driverConfig.waitForIdle(driver);

				// Click POP Info Report
				WebElement popInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//span[normalize-space()='NEP Dismantle Link Report Updated']")));

				// Scroll + JS click (stable for overlay/menu issues)
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", popInfo);

				((JavascriptExecutor) driver).executeScript("arguments[0].click();", popInfo);

				driverConfig.waitForIdle(driver);
				String countPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[3]/span";
				String download = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button";
				MidsUtile.clickElement(wait, download);

				driverConfig.waitForIdle(driver);
				WebElement elm = MidsUtile.getElement(wait, countPath);
				count = getReportCount(elm.getText());
				System.out.print("Nep count = " + count);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("Issue in First Report");
				return FAILED;
			}
			return count;
		}, "Verified NEP DISMANTLE LINK REPORT", "Failed in NEP DISMANTLE LINK REPORT", user, driver, process, data);

		runStep(ProcessName.DISMANTLE_MATERIAL_STATUE, () -> {
			Integer counts = 0;
			try {
				// Click second report
				WebElement popInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//span[normalize-space()='Dismantle Material Status Report']")));

				// Scroll + JS click (stable for overlay/menu issues)
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", popInfo);

				((JavascriptExecutor) driver).executeScript("arguments[0].click();", popInfo);

				driverConfig.waitForIdle(driver);
				String count = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/span";
				driverConfig.waitForIdle(driver);
				WebElement elm = MidsUtile.getElement(wait, count);
				counts = getReportCount(elm.getText());
				System.out.print("Nep count 2 = " + counts);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("Issue in second report");
				return FAILED;
			}
			return counts;
		}, "Verified DISMANTLE MATERIAL STATUE", "Issue in DISMANTLE MATERIAL STATUE", user, driver, process, data);

		return true;
	}

	private Integer getReportCount(String count) {
		try {
			return Integer.parseInt(count.replaceAll("\\D+", ""));
		} catch (Exception e) {
			log.error("Error getting report count", e);
		}
		return -1;
	}

	private boolean runStep(CommanProcessName name, Supplier<Integer> step, String successMsg, String errorMsg,
			String department, WebDriver driver, ProcessCalculationUtile process, List<ProcessData> data) {

		process.start(name.getValue(name));
		Integer count = step.get();
		driverConfig.waitForIdle(driver);
		process.end(name.getValue(name));

		String time = process.getExecutionTime(name.getValue(name));

		if (count != FAILED) {
			data.add(
					createResultSheet.createResultData(name.getValue(name), time, true, successMsg, department, count));
			log.info(name.getValue(name) + " -> " + time + " execute");
			return true;
		} else {
			data.add(createResultSheet.createResultData(name.getValue(name), time, false, errorMsg, department, count));
			log.info(name.getValue(name) + " -> " + time + " FAILED");
			return false;
		}
	}

}
