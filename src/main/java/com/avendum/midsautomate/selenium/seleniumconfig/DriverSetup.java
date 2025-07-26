package com.avendum.midsautomate.selenium.seleniumconfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.logging.Logger;
import java.io.File;

@Service
@Data
@Slf4j
@Lazy
@Scope("prototype")
public class DriverSetup {
        private WebDriver driver; // Non-static instance variable
        private boolean initialized = false;
//        @Autowired
//        private SeleniumConfigDetails seleniumConfig;

    public DriverSetup() {
        log.debug("DriverSetup bean created, no WebDriver initialization triggered.");
    }


        @PostConstruct
        public synchronized void setup() {
//            if (initialized) {
//                log.debug("WebDriver already initialized for this instance.");
//                return;
//            }
            SeleniumConfig seleniumConfig=new SeleniumConfig();
            String driverType = seleniumConfig.getDriverType();
            String driverName, browserBinary;

            log.info("Setting up WebDriver...");
            if (driverType != null && driverType.equalsIgnoreCase("windows")) {
                driverName = seleniumConfig.getWindowDriverPath();
                browserBinary = seleniumConfig.getWindowBinaryPath();
                log.info("Detected Windows environment. Driver path: " + driverName + ", Browser binary: " + browserBinary);
            } else {
                driverName = seleniumConfig.getLinuxDriverPath();
                browserBinary = seleniumConfig.getLiniuxBinaryPath();
                log.info("Detected Linux environment. Driver path: " + driverName + ", Browser binary: " + browserBinary);
            }

            File driverFile = new File(driverName);
            if (!driverFile.exists()) {
                log.info("GeckoDriver not found at: " + driverName);
                throw new RuntimeException("GeckoDriver not found at: " + driverName);
            }

            try {
                System.setProperty("webdriver.gecko.driver", driverName);
                log.info("GeckoDriver property set successfully.");
            } catch (Exception e) {
                log.error("Failed to set GeckoDriver property: " + e.getMessage());
                throw new RuntimeException("Failed to set GeckoDriver property", e);
            }

            System.setProperty("java.net.preferIPv4Stack", "true");
            log.info("IPv4 stack preference set.");

            try {
                FirefoxOptions options = new FirefoxOptions();
                options.setBinary(browserBinary);
                 options.addArguments("--headless");
                // options.addArguments("--window-size=1920,1080");
                // options.addArguments("--no-sandbox");
                // options.addArguments("--disable-dev-shm-usage");
                // options.addArguments("--disable-gpu");
                // options.addArguments("--disable-extensions");
                // options.addArguments("--remote-allow-origins=*");
                log.info("Initializing FirefoxDriver...");
                driver = new FirefoxDriver(options);
                driver.manage().window().maximize();
                log.info("FirefoxDriver initialized and window maximized.");

                String url = seleniumConfig.getAppUrl();
                log.info("Navigating to App URL: " + url);
                driver.get(url);
//                initialized = true;
                log.info("WebDriver setup completed successfully.");
            } catch (Exception e) {
                log.error("Failed to initialize FirefoxDriver: " + e.getMessage());
                throw new RuntimeException("Failed to initialize FirefoxDriver", e);
            }
        }

        @PreDestroy
        public void tearDown() {
            if (driver != null) {
                driver.quit();
                log.info("WebDriver closed successfully.");
                driver = null;
                initialized = false;
            } else {
                log.info("WebDriver is null, nothing to close.");
            }
        }

        @Lazy
        public synchronized  WebDriver getDriver() {

            if (driver == null ) {
                log.info("WebDriver is null, triggering lazy initialization.");
                setup();
            }
            if (driver == null) {
                log.warn("WebDriver is null after initialization attempt.");
            }
            return driver;
        }

        public synchronized WebDriver getNewDriver() {

            SeleniumConfig seleniumConfig=new SeleniumConfig();
            String driverType = seleniumConfig.getDriverType(); // Either windows or linux
            String driverName, browserBinary;

            log.info("Initializing new WebDriver...");
            if (driverType != null && driverType.equalsIgnoreCase("windows")) {
                driverName = seleniumConfig.getWindowDriverPath();
                browserBinary = seleniumConfig.getWindowBinaryPath();
                log.info("Detected Windows environment. Driver path: " + driverName + ", Browser binary: " + browserBinary);
            } else {
                driverName = seleniumConfig.getLinuxDriverPath();
                browserBinary = seleniumConfig.getLiniuxBinaryPath();
                log.info("Detected Linux environment. Driver path: " + driverName + ", Browser binary: " + browserBinary);
            }

            File driverFile = new File(driverName);
            if (!driverFile.exists()) {
                log.info("GeckoDriver not found at: " + driverName);
                return null;
            }

            try {
                System.setProperty("webdriver.gecko.driver", driverName);
                log.info("GeckoDriver property set successfully.");
            } catch (Exception e) {
                log.error("Failed to set GeckoDriver property: " + e.getMessage());
                return null;
            }

            System.setProperty("java.net.preferIPv4Stack", "true");
            log.info("IPv4 stack preference set.");

            try {
                FirefoxOptions options = new FirefoxOptions();
                options.setBinary(browserBinary);
                 options.addArguments("--headless");
                log.info("Initializing FirefoxDriver...");
                WebDriver newDriver = new FirefoxDriver(options);
                newDriver.manage().window().maximize();
                log.info("FirefoxDriver initialized and window maximized.");

                String url = seleniumConfig.getAppUrl();
                log.info("Navigating to App URL: " + url);
                newDriver.get(url);
                log.info("New WebDriver setup completed successfully.");
                return newDriver;
            } catch (Exception e) {
                log.error("Failed to initialize FirefoxDriver: " + e.getMessage());
                return null;
            }
        }
    }