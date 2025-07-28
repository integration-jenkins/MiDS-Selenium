package com.avendum.midsautomate.selenium.seleniumpages;

import com.avendum.midsautomate.controller.BasicTestReportController;
import com.avendum.midsautomate.model.BasicTestReport;
import com.avendum.midsautomate.selenium.seleniumconfig.DriverSetup;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.logging.Logger;

@Lazy
@Slf4j
@Service
@Scope("prototype")
public class LoginPage {
    @Autowired
    @Lazy
    private DriverSetup driverSetup;
    private static final Logger logger = Logger.getLogger(LoginPage.class.getName());

    private  WebDriver driver;
    private final String instanceId = UUID.randomUUID().toString();

    public WebDriver login(String username, String password) {
        logger.info("Starting login process for username");
        WebDriver driver = getWebDriver();// Triggers DriverSetup initialization
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
            js.executeScript("arguments[0].value = '" + username + "';", usernameField);
            js.executeScript("arguments[0].value = '" + password + "';", passwordField);
            logger.info("Username, password entered, and login button clicked successfully");
            WebElement loginButton = driver.findElement(By.cssSelector("vaadin-button[part='vaadin-login-submit']"));
            js.executeScript("arguments[0].click();", loginButton);
            logger.info("Login button clicked successfully using JavaScript Executor.");
            return driver;
        } catch (Exception e) {
            logger.info("Failed to login "+e);
            return null;
        }
    }

//    public boolean login(WebDriver driver, String username, String password) {
//        log.info("Starting login with provided WebDriver for username: {}", username);
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
//        try {
//            JavascriptExecutor js = (JavascriptExecutor) driver;
//            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
//            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
//            js.executeScript("arguments[0].value = '" + username + "';", usernameField);
//            js.executeScript("arguments[0].value = '" + password + "';", passwordField);
//            WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
//            js.executeScript("arguments[0].click();", loginButton);
//            log.info("Login button clicked successfully using JavaScriptExecutor.");
//            return true;
//        } catch (Exception e) {
//            log.error("Failed to login with provided WebDriver: {}", e.getMessage(), e);
//            return false;
//        }
//    }

    public boolean otpLoginPage(String otp,WebDriver driver) {

        logger.info("Starting OTP login process with OTP");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            SearchContext shadowRoot = (SearchContext) js.executeScript("return arguments[0].shadowRoot;",
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-text-field[colspan='2']"))));
            WebElement inputField = shadowRoot.findElement(By.cssSelector("input[part='value']"));
            inputField.sendKeys(otp);
            logger.info("OTP entered successfully.");
            js.executeScript("arguments[0].click();",
                    driver.findElement(By.cssSelector("vaadin-button[role='button'][colspan='2']")));
            logger.info("OTP submit button clicked successfully.");
            return true;
        } catch (Exception e) {
            logger.info("Failed to enter OTP or click submit button: "+ e.getMessage()+" "+e);
            return false;
        }
    }

//    public WebDriver getLoginDriver() {
//        return driver;
//    }

    private WebDriver getWebDriver() {
        if (driver == null) {
            logger.info("Retrieving WebDriver from DriverSetup");
            driver = driverSetup.getDriver();
        } else {
            logger.info("Using cached WebDriver");
        }
        return driver;
    }
    public void cleanup() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed in LoginPage");
            driver = null;
        }
    }
}