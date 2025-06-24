package com.avendum.midsautomate.selenium.utils;

import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class BulkUPload {
    static WebDriver driver;

    private static final Logger logger = Logger.getLogger(BulkUPload.class.getName());
//    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

//    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("http://localhost:6086/");
    }

    public static void main(String[] args) throws InterruptedException {
        setUp();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        driver.findElement(By.name("username")).sendKeys("JK_MS_PT_1");
        driver.findElement(By.name("password")).sendKeys("adm@123");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
        loginButton.click();
        logger.info("Login Successful");

        WebElement bulkUploadButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[9]/a")));
        bulkUploadButton.click();

        SearchContext shadow0 = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-combo-box[tabindex='0']"))).getShadowRoot();
        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
        shadow1.findElement(By.cssSelector("input[placeholder='Select Circle']")).sendKeys("JK");
        logger.info("Circle Selected");

        SearchContext upload = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload"))).getShadowRoot();
        String path = "C:\\Users\\Bhanu\\aa.xlsx";
        WebElement file = upload.findElement(By.cssSelector("input[type='file']"));
        file.sendKeys(path);
        logger.info("File Uploaded");
        Thread.sleep(1000);
        SearchContext s0 = upload.findElement(By.cssSelector("vaadin-upload-file")).getShadowRoot();
        Thread.sleep(1000);
        logger.info("Ok");
//        SearchContext s1 = s0.findElement(By.xpath("vaadin-upload-file")).getShadowRoot();
//        Thread.sleep(1000);
//        s0.findElement(By.cssSelector("div[part='start-button']")).click();
        logger.info("Upload Buuuton Clicked");
        WebElement startButton = s0.findElement(By.cssSelector("div[part='start-button']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", startButton);
        WebDriverWait waitt = new WebDriverWait(driver, Duration.ofSeconds(3));
        waitt.until(ExpectedConditions.elementToBeClickable(startButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startButton);
        logger.info("Queue stage koi bye");
    }
}