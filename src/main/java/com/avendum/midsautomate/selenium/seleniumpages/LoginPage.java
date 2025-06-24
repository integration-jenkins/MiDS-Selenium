package com.avendum.midsautomate.selenium.seleniumpages;

import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import com.avendum.midsautomate.selenium.seleniumconfig.MyWebDriverManager;
import com.avendum.midsautomate.selenium.seleniumconfig.SeleniumConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class LoginPage {

    private static final Logger logger = Logger.getLogger(LoginPage.class.getName());
    private  WebDriver driver;
    public LoginPage(){
        MyWebDriverManager webDriverManager = new MyWebDriverManager();
        webDriverManager.setup();
        this.driver = webDriverManager.getDriver();
        driver.manage().window().maximize();
        SeleniumConfig seleniumConfig=new SeleniumConfig();
        String url= seleniumConfig.getAppUrl();
        logger.info("App URL: " + url);
        driver.get(url);
    }
public void login(String username, String password) throws InterruptedException {
    Thread.sleep(2000);
    driver.findElement(By.name("username")).sendKeys(username);
    driver.findElement(By.name("password")).sendKeys(password);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
    loginButton.click();
    logger.info("Login Successful");
}

    //separate login Page for other user accordingly
    public void login(WebDriver driver,String username, String password) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
        loginButton.click();
        logger.info("Login Successful");
    }
}