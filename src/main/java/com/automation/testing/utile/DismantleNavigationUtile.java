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
import com.automation.testing.enums.DismantleName;
import com.automation.testing.enums.DismantlePath;

public class DismantleNavigationUtile {

	private static final Map<String, Integer> depMap = Map.of(Department.CIRCLE_MW_PLANNER.getName(), 12,
			Department.CIRCLE_OPERATION_TEAM.getName(), 7, Department.CIRCLE_DINC_PARTNER.getName(), 9,
			Department.CIRCLE_DEPLOYMENT_TEAM.getName(), 8);

	private static final Map<DismantleName, Integer> reportMap = Map.of(DismantleName.TRACK, 1,
			DismantleName.DATA_REPORT, 2, DismantleName.DOWNLOAD_SRN, 3, DismantleName.HISTORY, 4,
			DismantleName.DELETE_HISTORY, 5, DismantleName.PLAN_JUN, 6, DismantleName.UPLOAD, 7);

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

	public static boolean openDismantleInfo(WebDriver driver, WebDriverWait wait, String user) {
		String path = "/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div["
				+ depMap.get(user) + "]";
		return navigate(driver, wait, path);
	}

	public static boolean openReport(WebDriver driver, WebDriverWait wait, String user, DismantleName name) {
		String path = "/html[1]/body[1]/div[3]/app-layout-left-hybrid[1]/div[2]/div[1]/vaadin-vertical-layout[1]/div[1]/vaadin-vertical-layout[1]/div["
				+ depMap.get(user) + "]/iron-collapse-layout[1]/vaadin-vertical-layout[1]/a[" + reportMap.get(name)
				+ "]";
		return navigate(driver, wait, path);
	}

	public static boolean openBulkOptions(Integer idx, WebDriverWait wait) {
		boolean b = true;
		b &= MidsUtile.clickElement(wait, DismantlePath.DISMNATLE_OPTIONS.getPath());
		String path = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-horizontal-layout/vaadin-horizontal-layout/vaadin-button["
				+ idx + "]";
		b &= MidsUtile.clickElement(wait, path);
		return b;
	}

}
