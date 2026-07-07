package com.automation.testing.utile;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.net.UrlChecker.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automation.testing.enums.DprPlanTrackPath;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MidsUtile {

	private static final Logger log = LoggerFactory.getLogger(MidsUtile.class);

	public static Map<String, String> readSoftAtData(String path) {
		Map<String, String> map = new HashMap<>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			map = objectMapper.readValue(new File(path), new TypeReference<Map<String, String>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Issue To read Json");
		}
		return map;
	}

	public static boolean clickElement(WebDriverWait wait, String path) {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(path)));
			element.click();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean clickElement(WebElement element) {
		try {
			element.click();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static WebElement getElement(WebDriverWait wait, String path) {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(path)));
			return element;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public static WebElement getElementInTable(WebDriver driver, WebDriverWait wait, String xpath) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		long lastScrollTop = -1;

		while (true) {
			try {
				// Try to find element
				WebElement element = driver.findElement(By.xpath(xpath));

				// Scroll element into view (center)
				js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);

				// Wait until clickable
				return wait.until(ExpectedConditions.elementToBeClickable(element));

			} catch (Exception e) {
				// Scroll the table or page
				js.executeScript("window.scrollBy(0, 400);");

				long currentScrollTop = (long) js
						.executeScript("return document.documentElement.scrollTop || document.body.scrollTop;");

				// Stop if no more scrolling possible
				if (currentScrollTop == lastScrollTop) {
					throw new RuntimeException("Element not found after scrolling: " + xpath);
				}
				lastScrollTop = currentScrollTop;
			}
		}
	}

	public static WebElement findShadowElement(WebDriver driver, String... selectors) {

		// selectors[0] = first shadow host
		// selectors[1] = next shadow host (if any)
		// selectors[last] = final element inside deepest shadow root

		SearchContext context = driver;

		// Loop through all hosts except the last selector (final element)
		for (int i = 0; i < selectors.length - 1; i++) {
			WebElement host = context.findElement(By.cssSelector(selectors[i]));
			context = host.getShadowRoot(); // enter shadow DOM
		}

		// Return final element inside deepest shadow DOM
		return context.findElement(By.cssSelector(selectors[selectors.length - 1]));
	}

	public static String getDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		return date.format(formatter);
	}

	public static Integer getReportCount(WebDriverWait wait, String path) {
		try {
			WebElement element = getElement(wait, path);
			if (element != null) {
				return Integer.parseInt(element.getText().replaceAll("\\D+", ""));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}

	public static boolean forceClick(WebDriver driver, WebDriverWait wait, String path) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = getElement(wait, path);
			js.executeScript("arguments[0].click();", element);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public static boolean filterPlans(WebDriver driver, WebDriverWait wait, String path, String val) {
		try {
			WebElement elm = getElementInTable(driver, wait, path);
			elm.sendKeys(val);
			Thread.sleep(300);
			elm.sendKeys(Keys.ENTER);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public static String getNotifications(WebDriver driver, WebDriverWait wait) {
		try {
			WebElement messageElement = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//vaadin-notification-card//vaadin-horizontal-layout/div")));

			return messageElement.getText().trim();

		} catch (Exception e) {
			// Print the actual error so you know what went wrong
			System.err.println("Failed to read notification: " + e.getMessage());
			return "";
		}
	}

}
