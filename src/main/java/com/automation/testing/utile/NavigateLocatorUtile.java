package com.automation.testing.utile;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.testing.enums.NavigateButtonPath;

public class NavigateLocatorUtile {

	public static boolean click(WebDriver driver, WebDriverWait wait, String xpath) {

		try {

			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

// wait visible
			wait.until(ExpectedConditions.visibilityOf(element));

// scroll center
			((JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);

// stabilize UI
			Thread.sleep(500);

// wait clickable
			wait.until(ExpectedConditions.elementToBeClickable(element));

			try {

// normal selenium click
				element.click();

			} catch (Exception ex) {

				try {

// js click fallback
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

				} catch (Exception jsEx) {

// actions fallback
					Actions actions = new Actions(driver);

					actions.moveToElement(element).pause(Duration.ofMillis(200)).click().perform();
				}
			}

			return true;

		} catch (StaleElementReferenceException e) {

			try {

// retry once
				WebElement retryElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});",
						retryElement);

				Thread.sleep(300);

				retryElement.click();

				return true;

			} catch (Exception ex) {

				ex.printStackTrace();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

	private static boolean navigateToTwoLevelTab(WebDriver driver, WebDriverWait wait, String parent, String child) {

		return click(driver, wait, parent) && click(driver, wait, child);
	}

	public static boolean navigateToLBReport(WebDriver driver, WebDriverWait wait) {

		return navigateToTwoLevelTab(driver, wait, NavigateButtonPath.LB_BUTTON.getPath(),
				NavigateButtonPath.MW_LB_BUTTON.getPath());
	}

	public static boolean navigateToLBReportUBR(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.URB_LB_REPORT.getPath());
	}

	public static boolean navigateToDPRReport(WebDriver driver, WebDriverWait wait) {

		return navigateToTwoLevelTab(driver, wait, NavigateButtonPath.DEPLOYMENT_REPORT.getPath(),
				NavigateButtonPath.DPR_REPORT.getPath());
	}

	public static boolean navigateToDPRReportOps(WebDriver driver, WebDriverWait wait) {

		return navigateToTwoLevelTab(driver, wait, NavigateButtonPath.DEPLOYMENT_REPORT_FOR_OPS.getPath(),
				NavigateButtonPath.DPR_REPORT.getPath());
	}

	public static boolean navigateToDeployAssignReport(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.DEPLOYMENT_ASSIGNMENT_REPORT.getPath());
	}

	public static boolean navigateToPRIssueReport(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.PRI_ISSUE_HISTORY.getPath());
	}

	public static boolean navigateToAssignmentReport(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.ASSIGNMENT_REPORT.getPath());
	}

	public static boolean navigateToPlanUpload(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.PLAN_ULOAD_BUTTON.getPath());
	}

	public static boolean navigateToDprPlanTrack(WebDriver driver, WebDriverWait wait) {

		return navigateToTwoLevelTab(driver, wait, NavigateButtonPath.MEDIA_PLANNING_BUTTON.getPath(),
				NavigateButtonPath.MW_PLAN_TRACKING.getPath());
	}

	public static boolean navigateToDprPlanTrackDINC(WebDriver driver, WebDriverWait wait) {

		return navigateToTwoLevelTab(driver, wait, NavigateButtonPath.MEDIA_PLANNING_BUTTON.getPath(),
				NavigateButtonPath.MW_PLAN_TRACKING_DINC.getPath());
	}

	public static boolean navigateToDprPlanTrackForDeploymentUser(WebDriver driver, WebDriverWait wait) {

		return navigateToTwoLevelTab(driver, wait, NavigateButtonPath.MEDIA_PLANNING_BUTTON.getPath(),
				NavigateButtonPath.UBR_PLAN_TRACKING.getPath());
	}

	public static boolean navigateToSoftAtReport(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.SOFT_AT_REPORT.getPath());
	}

	public static boolean navigateToLbPlanUpload(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.PLAN_ULOAD_BUTTON.getPath());
	}

	public static boolean navigateToDPRPlanUpload(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.PLAN_ULOAD_BUTTON.getPath());
	}

	public static boolean navigateToSoftAtReport(WebDriver driver, WebDriverWait wait, Integer idx) {

		String report_path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-tabs/vaadin-tab["
				+ idx + "]";

		return click(driver, wait, report_path);
	}

	public static boolean navigateToSoftAtUpload(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.SOFT_AT_BULK_UPLOAD.getPath());
	}

	public static boolean navigateToSoftAtUploadForDINC(WebDriver driver, WebDriverWait wait) {

		return click(driver, wait, NavigateButtonPath.SOFT_AT_BULK_UPLOAD_DINC.getPath());
	}

	public static boolean navigateToDepoymentReport(WebDriver driver, WebDriverWait wait, Integer idx) {

		return click(driver, wait, getCustomPathDeploymentReports(idx));
	}

	public static String getCustomPathDeploymentReports(int idx) {
		return "/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[3]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a["
				+ idx + "]";
	}

	public static boolean openAtTab(WebDriver driver, WebDriverWait wait, int idx) {

		String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-horizontal-layout[1]/vaadin-tabs/vaadin-tab["
				+ idx + "]";

		return click(driver, wait, path);
	}

	public static boolean navigateByDepAndDinc(WebDriver driver, WebDriverWait wait, int idx) {

		String path = "/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div[4]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a["
				+ idx + "]";

		return click(driver, wait, path);
	}

	public static boolean navigateToLocation(WebDriver driver, WebDriverWait wait, String path) {

		return click(driver, wait, path);
	}

}
