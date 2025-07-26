package com.avendum.midsautomate.selenium.seleniumconfig;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.io.File;
import java.util.logging.Logger;

//Done
public class Base {
     static WebDriver driver;
    private static final Logger logger = Logger.getLogger(Base.class.getName());

    //    @BeforeClass
   public void setup() {
        SeleniumConfig seleniumConfig = new SeleniumConfig();
        String driverType = seleniumConfig.getDriverType(); // Two ways: either windows or linux
        String driverName, browserBinary;

        logger.info("Setting up WebDriver...");
        if (driverType != null && driverType.equalsIgnoreCase("windows")) {
            driverName = seleniumConfig.getWindowDriverPath();
            browserBinary = seleniumConfig.getWindowBinaryPath();
            logger.info("Detected Windows environment. Driver path: " + driverName + ", Browser binary: " + browserBinary);
        } else {
            driverName = seleniumConfig.getLinuxDriverPath();
            browserBinary = seleniumConfig.getLiniuxBinaryPath();
            logger.info("Detected Linux environment. Driver path: " + driverName + ", Browser binary: " + browserBinary);
        }

        File driverFile = new File(driverName);
        if (!driverFile.exists()) {
            logger.info("GeckoDriver not found at: " + driverName);
            return;
        }

        try {
            System.setProperty("webdriver.gecko.driver", driverName);
            logger.info("GeckoDriver property set successfully.");
        } catch (Exception e) {
            logger.info("Not able to detect the GeckoDriver: " + e.getMessage());
        }

        System.setProperty("java.net.preferIPv4Stack", "true");
        logger.info("IPv4 stack preference set.");

        // Firefox
        WebDriver driver;
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.setBinary(browserBinary);
            options.addArguments("--headless");
//            options.addArguments("--window-size=1920,1080");
//            options.addArguments("--no-sandbox");
//            options.addArguments("--disable-dev-shm-usage");
//            options.addArguments("--disable-gpu");
//            options.addArguments("--disable-extensions");
//            options.addArguments("--remote-allow-origins=*");
            logger.info("Initializing FirefoxDriver with headless mode...");
            driver = new FirefoxDriver(options);
            logger.info("FirefoxDriver initialized and window maximized.");

            String url = seleniumConfig.getAppUrl();
            logger.info("Navigating to App URL: " + url);
            driver.get(url);
            Base.driver = driver;
            logger.info("WebDriver setup completed successfully.");
        } catch (Exception e) {
            logger.info("Mistake in driver configuration with driver: " + e.getMessage());
        }
    }
//    @AfterClass
    public static   void  tearDown() {
        if(driver!=null){
            driver.quit();
        }else{
            logger.info("Driver is null");
        }
    }

    public void tearDown(WebDriver driver){
       if(driver!=null){
           driver.quit();
       }else{
           logger.info("Driver is null");
       }
    }


    public  static WebDriver getDriver() {
        return Base.driver;
    }
    //Here When i want separate other driver for using in program
    public WebDriver getNewDriver() {
        SeleniumConfig seleniumConfig = new SeleniumConfig();
        String driverType = seleniumConfig.getDriverType(); // Two ways: either windows or linux
        String driverName, browserBinary;

        logger.info("Initializing new WebDriver...");
        if (driverType != null && driverType.equalsIgnoreCase("windows")) {
            driverName = seleniumConfig.getWindowDriverPath();
            browserBinary = seleniumConfig.getWindowBinaryPath();
            logger.info("Detected Windows environment. Driver path: " + driverName + ", Browser binary: " + browserBinary);
        } else {
            driverName = seleniumConfig.getLinuxDriverPath();
            browserBinary = seleniumConfig.getLiniuxBinaryPath();
            logger.info("Detected Linux environment. Driver path: " + driverName + ", Browser binary: " + browserBinary);
        }
        File driverFile = new File(driverName);
        if (!driverFile.exists()) {
            logger.info("GeckoDriver not found at: " + driverName);
            return null;
        }

        try {
            System.setProperty("webdriver.gecko.driver", driverName);
            logger.info("GeckoDriver property set successfully.");
        } catch (Exception e) {
            logger.info("Not able to detect the ChromeDriver: " + e.getMessage());
            return null;
        }

        System.setProperty("java.net.preferIPv4Stack", "true");
        logger.info("IPv4 stack preference set.");

        // Firefox
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.setBinary(browserBinary);
            options.addArguments("--headless");

            logger.info("Initializing FirefoxDriver with headless mode...");
            WebDriver driver = new FirefoxDriver(options);
            driver.manage().window().maximize();
            logger.info("FirefoxDriver initialized and window maximized.");

            String url = seleniumConfig.getAppUrl();
            logger.info("Navigating to App URL: " + url);
            driver.get(url);
            logger.info("WebDriver setup completed successfully.");
            return driver;
        } catch (Exception e) {
            logger.info("Mistake in driver configuration with driver: " + e.getMessage());
        }

        logger.info("Returning WebDriver instance.");
        return null;
    }


}