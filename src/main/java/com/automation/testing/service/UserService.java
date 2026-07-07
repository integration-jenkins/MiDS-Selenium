package com.automation.testing.service;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automation.testing.config.DriverConfig;
import com.automation.testing.sourcecredentials.UserDetails;
import com.automation.testing.utile.MidsUtile;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private WebDriver driver;

	@Autowired
	private WebDriverWait wait;

	@Autowired
	private DriverConfig driverConfig;

	public synchronized boolean userLogin(String department) {
		try {

			Thread.sleep(2000);

			log.info(department + " User Try to Login");
			List<String> userLoginDetails = UserDetails.getUserDetails(department);

			String userName = userLoginDetails.get(0);
			String password = userLoginDetails.get(1);

			WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
			usernameField.sendKeys(userName);

			WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.name("password")));
			passwordField.sendKeys(password);

			WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//*[@id=\"vaadinLoginForm\"]/vaadin-login-form-wrapper/form/vaadin-button")));
			webElement.click();

			log.info("OTP sent. Waiting for user input...");
			System.out.print("Enter OTP : ");

			Scanner sc = new Scanner(System.in);
			String otp = sc.nextLine();

			for (int i = 0; i < otp.length(); i++) {
				String path = "/html/body/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-form-layout/vaadin-horizontal-layout/vaadin-text-field["
						+ (i + 1) + "]";
				WebElement elm = MidsUtile.getElement(wait, path);
				Thread.sleep(300);
				elm.sendKeys(otp.charAt(i) + "");
			}

			String cofPath = "/html/body/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-form-layout/vaadin-button";
			WebElement elm1 = MidsUtile.getElement(wait, cofPath);
			Thread.sleep(300);
			elm1.click();

			driverConfig.waitForIdle(driver);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean logOut(WebDriver driver, WebDriverWait wait) {
		try {

			SearchContext shadow0 = driver.findElement(By.cssSelector("vaadin-menu-bar[role='menubar']"))
					.getShadowRoot();
			SearchContext shadow1 = shadow0
					.findElement(By.cssSelector("vaadin-context-menu-item[theme='menu-bar-item']")).getShadowRoot();
			WebElement slotEl = shadow1.findElement(By.cssSelector("slot"));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", slotEl);

			By logoutMenuSelector = By.cssSelector(
					"vaadin-context-menu-overlay vaadin-context-menu-list-box vaadin-context-menu-item:nth-child(2)");
			wait.until(ExpectedConditions.presenceOfElementLocated(logoutMenuSelector));

			WebElement logoutMenu = driver.findElement(logoutMenuSelector);
			SearchContext shadowLogout = logoutMenu.getShadowRoot();
			WebElement logoutBtn = shadowLogout.findElement(By.cssSelector("div[part='content']"));

			((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutBtn);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkLoginLogoutFunction(String department) {
		boolean isPass = false;

		try {
			isPass = userLogin(department);
			if (isPass) {
				log.info("User login success Department: " + department + ")");
			} else {
				log.info("Issue in Login");
			}

			isPass = logOut(driver, wait);
			if (isPass) {
				log.info("User  Logout.");
			} else {
				log.info("Issue in Logout");
			}

		} catch (Exception e) {
			log.error("Login failed for department: " + department, e);
		}

		return isPass;
	}

	public synchronized boolean userLogin(String department, WebDriver driver, WebDriverWait wait) {
		try {

			Thread.sleep(2000);

			log.info(department + " User Try to Login");
			List<String> userLoginDetails = UserDetails.getUserDetails(department);

			String userName = userLoginDetails.get(0);
			String password = userLoginDetails.get(1);

			WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
			usernameField.sendKeys(userName);

			WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.name("password")));
			passwordField.sendKeys(password);

			WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//*[@id=\"vaadinLoginForm\"]/vaadin-login-form-wrapper/form/vaadin-button")));
			webElement.click();

			synchronized (webElement) {
				log.info("OTP sent. Waiting for user input...");
				System.out.print("Enter OTP : ");

				Scanner sc = new Scanner(System.in);
				String otp = sc.nextLine();

				for (int i = 0; i < otp.length(); i++) {
					String path = "/html/body/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-form-layout/vaadin-horizontal-layout/vaadin-text-field["
							+ (i + 1) + "]";
					WebElement elm = MidsUtile.getElement(wait, path);
					Thread.sleep(300);
					elm.sendKeys(otp.charAt(i) + "");
				}

			}

			String cofPath = "/html/body/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-form-layout/vaadin-button";
			WebElement elm1 = MidsUtile.getElement(wait, cofPath);
			Thread.sleep(300);
			elm1.click();
			System.out.println("User fill the OTP" + department);

			driverConfig.waitForIdle(driver);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
