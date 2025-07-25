package com.avendum.midsautomate.selenium.seleniumpages;

import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class Login extends Base {

    private static final Logger logger = Logger.getLogger(Login.class.getName());
   public void Login(String username, String password) throws InterruptedException {
       // Initialize WebDriver
       Base base = new Base();
       logger.info("Initializing WebDriver...");
       setup();
       WebDriver driver = getDriver();
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
       logger.info("WebDriver initialized successfully.");
       // Enter credentials
       logger.info("Entering credentials: Username is " + username + ", Password is " + password);
       try {
           //Method 1
           JavascriptExecutor js = (JavascriptExecutor) driver;
           WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
           WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
           js.executeScript("arguments[0].value = '" + username + "';", usernameField);
           js.executeScript("arguments[0].value = '" + password + "';", passwordField);
           logger.info("Username and Password entered.");
           logger.info("------------------------------------------------------------------------------------------------------------------------------------------------------");
       } catch (Exception e) {
           logger.info("Enable to send username or password using JavaScript Executor."+e);
           //Method 2
           try{
               WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
               usernameField.sendKeys(username);
               logger.info("Username entered.");
               WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.name("password")));
               passwordField.sendKeys(password);
               logger.info("Password entered.");
           }catch (Exception ee){
               logger.info("Enable to send username or password."+ee);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/vaadin-login-overlay-wrapper/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-text-field/input")));
        element.sendKeys(username);
               WebElement element2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/vaadin-login-overlay-wrapper/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-password-field/input")));
               element2.sendKeys(password);
           }
       }
       try {
           JavascriptExecutor js = (JavascriptExecutor) driver;
           WebElement loginButton = driver.findElement(By.cssSelector("vaadin-button[part='vaadin-login-submit']"));
           js.executeScript("arguments[0].click();", loginButton);
           logger.info("Login button clicked successfully using JavaScript Executor.");

       } catch (Exception e) {
           logger.info("Failed to click login button even with JavaScript Executor. "+ e);
           try {
               logger.info("Waiting for login button to be clickable...");
               WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
               loginButton.click();
               logger.info("Login button clicked successfully.");
           } catch (Exception ee) {
               logger.info("Failed to click login button using standard Selenium. Trying JavaScript Executor..."+ ee);
           }
       }
   }
   public void OptEnter(String otp){
       logger.info("Entering OTP: " + otp);
       try{
       WebDriver driver = getDriver();
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
       SearchContext shadow = driver.findElement(By.cssSelector("vaadin-text-field[colspan='2']")).getShadowRoot();
       shadow.findElement(By.cssSelector("input[part='value']")).sendKeys(otp);
       logger.info("Otp entered.");
       try{
       SearchContext shadow1 = driver.findElement(By.cssSelector("vaadin-button[role='button'][colspan='2']")).getShadowRoot();
       shadow1.findElement(By.cssSelector("#button")).click();
       }catch (RuntimeException e) {
           try{
           logger.info("OTP Button not found.");
           logger.info(e.getMessage());
           JavascriptExecutor js = (JavascriptExecutor) driver;
           SearchContext shadowRoot = (SearchContext) js.executeScript("return arguments[0].shadowRoot;", driver.findElement(By.cssSelector("vaadin-text-field[colspan='2']")));
           WebElement inputField = shadowRoot.findElement(By.cssSelector("input[part='value']"));
           inputField.sendKeys(otp);
           logger.info("Otp entered.");
           js.executeScript("arguments[0].click();", driver.findElement(By.cssSelector("vaadin-button[role='button'][colspan='2']")));
            }catch (RuntimeException e1) {
                logger.info("OTP Button not found.");
                logger.info(e1.getMessage());
            }
       }
       }catch (RuntimeException e) {
           logger.info("OTP Field not found.");
           logger.info(e.getMessage());
       }
   }
    //separate login Page for other user accordingly
   public void Login(WebDriver driver, String username, String password) throws InterruptedException {
       logger.info("Starting login process...");
       Thread.sleep(2000);
       logger.info("Entering credentials: Username is " + username + ", Password is " + password);
       driver.findElement(By.name("username")).sendKeys(username);
       driver.findElement(By.name("password")).sendKeys(password);
       logger.info("Waiting for login button to be clickable...");
       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
       WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
       loginButton.click();
       logger.info("Login button clicked. Login successful.");
   }
}