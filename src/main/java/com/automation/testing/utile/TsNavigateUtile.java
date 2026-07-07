package com.automation.testing.utile;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.testing.enums.Department;
import com.automation.testing.enums.TrafficShiftingPath;

public class TsNavigateUtile {

	private static final Map<String, Integer> depMap = Map.of(Department.CIRCLE_MW_PLANNER.getName(), 14,
			Department.CIRCLE_OPERATION_TEAM.getName(), 9, Department.CIRCLE_DINC_PARTNER.getName(), 10,
			Department.CIRCLE_DEPLOYMENT_TEAM.getName(), 10, Department.CIRCLE_MS_PARTNER.getName(), 8);

	private static boolean navigate(WebDriverWait wait, String path) {
		try {
			Thread.sleep(300);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(path)));
			element.click();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean navigate(WebDriver driver, WebDriverWait wait, String xpath) {

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

	// open TS options by each department
	public static boolean navigateToTsPage(WebDriver driver, WebDriverWait wait, String user) {
		String path = "/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div["
				+ depMap.get(user) + "]";
		return navigate(driver, wait, path);
	}

	// open TS Reports
	public static boolean navigateToReport(WebDriver driver, WebDriverWait wait, String user, String reportIdx) {
		if (!user.equals(Department.CIRCLE_MW_PLANNER.getName())) {
			reportIdx = String.valueOf(Integer.parseInt(reportIdx) - 1);
		}
		String path = "/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div["
				+ depMap.get(user) + "]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[" + reportIdx + "]";
		return navigate(driver, wait, path);
	}

	public static boolean navigateToTrackViewBulkOptions(WebDriver driver, WebDriverWait wait, int idx) {
		String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button["
				+ idx + "]";
		return navigate(wait,
				"/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-horizontal-layout/div")
				&& navigate(driver, wait, path);
	}

}
