package com.avendum.midsautomate.selenium.seleniumconfig;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.logging.Logger;

public class MyWebDriverManager {
    private  WebDriver driver;

    private static final Logger logger = Logger.getLogger(MyWebDriverManager.class.getName());
    public  void setup() {
        SeleniumConfig seleniumConfig = new SeleniumConfig();
        String driverType=seleniumConfig.getDriverType();//Two ways either windows or linux
        String driverName,browserBinary;
        if (driverType != null && driverType.equalsIgnoreCase("windows")) {
            driverName = seleniumConfig.getWindowDriverPath();
            browserBinary=seleniumConfig.getWindowBinaryPath();
        } else {
            driverName = seleniumConfig.getLinuxDriverPath();
            browserBinary=seleniumConfig.getLiniuxBinaryPath();
        }
        try{
            System.setProperty("webdriver.gecko.driver", driverName);
        }catch (Exception e){
            logger.info("Not able to detect the GeckoDriver");
        }
        System.setProperty("java.net.preferIPv4Stack", "true");
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-web-security");
//        options.addArguments("--allow-running-insecure-content");
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless");
//        driver = new ChromeDriver(options);
        //Firefox
        try{
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(browserBinary);
//        options.addArguments("--disable-web-security");
//        options.addArguments("--allow-running-insecure-content");
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless");
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
    }catch (Exception e){
        logger.info("Mistake in driver configuration with driver: "+e);
    }
    }
    public  void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public  WebDriver getDriver() {
        return driver;
    }
    public  WebDriver getNewDriver() {
        SeleniumConfig seleniumConfig = new SeleniumConfig();
        String driverType=seleniumConfig.getDriverType();//Two ways either windows or linux
        String driverName,browserBinary;

        if (driverType != null && driverType.equalsIgnoreCase("windows")) {
            driverName = seleniumConfig.getWindowDriverPath();
            browserBinary=seleniumConfig.getWindowBinaryPath();
        } else {
            driverName = seleniumConfig.getLinuxDriverPath();
            browserBinary=seleniumConfig.getLiniuxBinaryPath();
        }
        try{
            System.setProperty("webdriver.gecko.driver", driverName);
        }catch (Exception e){
            logger.info("Not able to detect the GeckoDriver");
            return null;
        }
        System.setProperty("java.net.preferIPv4Stack", "true");

//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-web-security");
//        options.addArguments("--headless");
//        options.addArguments("--allow-running-insecure-content");
//        options.addArguments("--remote-allow-origins=*");
//        WebDriver driver = new ChromeDriver(options);

        //Firefox
        try{
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(browserBinary);
//        options.addArguments("--disable-web-security");
//        options.addArguments("--allow-running-insecure-content");
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless");
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
            return driver;
        }catch (Exception e){
            logger.info("Mistake in driver configuration with driver: "+e);
        }
       return  driver;
    }

}