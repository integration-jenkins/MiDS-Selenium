package com.avendum.midsautomate.selenium.seleniumpages;

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

@Lazy
@Slf4j
@Service
@Scope("prototype")
public class LoginPage {
    @Autowired
    @Lazy
    private DriverSetup driverSetup;

    private  WebDriver driver;
    private final String instanceId = UUID.randomUUID().toString();
    public LoginPage() {
        log.debug("LoginPage bean created, instanceId: {}, no WebDriver initialization triggered.", instanceId);
    }

    public WebDriver login(String username, String password) {
        log.info("Starting login process for username: {}, instanceId: {}", username, instanceId);
        WebDriver driver = getWebDriver();// Triggers DriverSetup initialization
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
            js.executeScript("arguments[0].value = '" + username + "';", usernameField);
            js.executeScript("arguments[0].value = '" + password + "';", passwordField);
            log.info("Username, password entered, and login button clicked successfully for instanceId: {}.", instanceId);
            WebElement loginButton = driver.findElement(By.cssSelector("vaadin-button[part='vaadin-login-submit']"));
            js.executeScript("arguments[0].click();", loginButton);
            log.info("Login button clicked successfully using JavaScript Executor.");
            return driver;
        } catch (Exception e) {
            log.error("Failed to login for instanceId: {}: {}", instanceId, e.getMessage(), e);
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
        log.info("Starting OTP login process with OTP: {}", otp);// Triggers DriverSetup initialization
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            SearchContext shadowRoot = (SearchContext) js.executeScript("return arguments[0].shadowRoot;",
                    driver.findElement(By.cssSelector("vaadin-text-field[colspan='2']")));
            WebElement inputField = shadowRoot.findElement(By.cssSelector("input[part='value']"));
            inputField.sendKeys(otp);
            log.info("OTP entered successfully.");
            js.executeScript("arguments[0].click();",
                    driver.findElement(By.cssSelector("vaadin-button[role='button'][colspan='2']")));
            log.info("OTP submit button clicked successfully.");
            return true;
        } catch (Exception e) {
            log.error("Failed to enter OTP or click submit button: {}", e.getMessage(), e);
            return false;
        }
    }

//    public WebDriver getLoginDriver() {
//        return driver;
//    }

    private WebDriver getWebDriver() {
        if (driver == null) {
            log.info("Retrieving WebDriver from DriverSetup for instanceId: {}.", instanceId);
            driver = driverSetup.getDriver();
        } else {
            log.debug("Using cached WebDriver for instanceId: {}.", instanceId);
        }
        return driver;
    }
    public void cleanup() {
        if (driver != null) {
            driver.quit();
            log.info("WebDriver closed in LoginPage for instanceId: {}.", instanceId);
            driver = null;
        }
    }
}