package com.automation.testing.testcase;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
import com.automation.testing.utile.ProcessCalculationUtile;

@Component
public class VerifyNewDeploymentReports {

	private static final Logger log = LoggerFactory.getLogger(VerifyNewDeploymentReports.class);

	private final ExportSheetService exportSheetService;
	private final UserService userService;
	private final DriverConfig driverConfig;

	public VerifyNewDeploymentReports(ExportSheetService exportSheetService,
			ReportTrackingService reportTrackingService, UserService userService, WebDriverWait wait, WebDriver driver,
			DriverConfig driverConfig) {
		this.exportSheetService = exportSheetService;
		this.userService = userService;
		this.driverConfig = driverConfig;
	}

	private final ExecutorService executorService = Executors.newFixedThreadPool(5);
	private ProcessCalculationUtile process = new ProcessCalculationUtile();

	private List<String> users = List.of(Department.CIRCLE_MW_PLANNER.getName(),
			Department.CIRCLE_DEPLOYMENT_TEAM.getName(), Department.CIRCLE_DINC_PARTNER.getName(),
			Department.CIRCLE_OPERATION_TEAM.getName(), Department.CIRCLE_MS_PARTNER.getName());

	public void run() {
		log.info("New Deployment verification Start ");
		process.start("REPPORT_VERIFY");
		ReentrantLock lock = new ReentrantLock();

		for (String user : users) {
			executorService.submit(() -> {
				WebDriver driver = driverConfig.createNewBrowser();
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

				lock.lock();
				try {
					log.info(user + " start to verify reports");
					userService.userLogin(user, driver, wait);
				} catch (Exception e) {
					// TODO: handle exception
					Thread.currentThread().interrupt();
				} finally {
					lock.unlock();
				}

				exportSheetService.executeAllReports(driver, wait, user, true, lock);

				userService.logOut(driver, wait);
				driver.close();
				log.info(user + " Finished there work");
			});
		}

		executorService.shutdown();
		try {
			executorService.awaitTermination(30, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		process.end("REPPORT_VERIFY");

		log.info("End = " + process.getExecutionTime("REPPORT_VERIFY"));

	}

}
