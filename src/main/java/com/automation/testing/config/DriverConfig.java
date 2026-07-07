package com.automation.testing.config;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.Duration;

import javax.swing.JFrame;

@Configuration
public class DriverConfig {

	@Bean
	public WebDriver getDriver() {

		String driverPath = "D:\\automationdrivers\\drivers\\geckodriver.exe";
		String binaryPath = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";

		String projectUrl = "https://mids-test.airtel.com/"
//				"https://mids.airtel.com"
		;

		System.out.println("Running with Firefox Binary: " + binaryPath);
		System.setProperty("webdriver.gecko.driver", driverPath);

		FirefoxProfile profile = new FirefoxProfile();

		profile.setPreference("browser.download.folderList", 2);

		profile.setPreference("browser.download.dir", "D:\\Downloads");

		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/vnd.ms-excel, application/octet-stream, application/pdf");

		profile.setPreference("pdfjs.disabled", true);
		profile.setPreference("browser.download.manager.showWhenStarting", false);

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setBinary(binaryPath);
		firefoxOptions.setProfile(profile);

		WebDriver driver = new FirefoxDriver(firefoxOptions);
		driver.get(projectUrl);

		return driver;
	}

	@Bean
	public WebDriverWait getDriverWait(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			return wait;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void waitForIdle(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(600));
		wait.until(webDriver -> {
			JavascriptExecutor js = (JavascriptExecutor) webDriver;
			Object result = js.executeScript("if (window.Vaadin && window.Vaadin.Flow && window.Vaadin.Flow.clients) {"
					+ "  return Object.values(window.Vaadin.Flow.clients)"
					+ "    .every(client => client.isActive() === false);" + "} else {" + "  return true;" + "}");
			return Boolean.TRUE.equals(result);
		});
	}

	public void refreshDriver(WebDriver driver) {
		driver.navigate().refresh();
		waitForIdle(driver);
	}

	public WebDriver createNewBrowser() {

		String driverPath = "D:\\automationdrivers\\drivers\\geckodriver.exe";
		String binaryPath = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";

		String projectUrl = "https://mids-test.airtel.com/";

		System.out.println("Running with Firefox Binary: " + binaryPath);
		System.setProperty("webdriver.gecko.driver", driverPath);

		FirefoxProfile profile = new FirefoxProfile();

		profile.setPreference("browser.download.folderList", 2);

		profile.setPreference("browser.download.dir", "D:\\Downloads");

		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/vnd.ms-excel, application/octet-stream, application/pdf");

		// Disable save dialog
		profile.setPreference("pdfjs.disabled", true);
		profile.setPreference("browser.download.manager.showWhenStarting", false);

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setBinary(binaryPath);
		firefoxOptions.setProfile(profile);

		WebDriver driver = new FirefoxDriver(firefoxOptions);
		driver.get(projectUrl);

		return driver;
	}

}
