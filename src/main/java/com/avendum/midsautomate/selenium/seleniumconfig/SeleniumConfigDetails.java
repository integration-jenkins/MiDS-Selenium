//package com.avendum.midsautomate.selenium.seleniumconfig;
//
//import lombok.Data;
//import org.springframework.stereotype.Service;
//
//import java.util.logging.Logger;
//
//import org.springframework.beans.factory.annotation.Value;
//
//import javax.annotation.PostConstruct;
//
//@Service
//@Data
//public class SeleniumConfigDetails {
//    private static final Logger logger = Logger.getLogger(SeleniumConfigDetails.class.getName());
//
//    @Value("${selenium.app.url}")
//    private String appUrl;
//
//    @Value("${selenium.system.driver}")
//    private String systemDriver;
//
//    @Value("${selenium.window.driver.path}")
//    private String windowDriverPath;
//
//    @Value("${selenium.linux.driver.path}")
//    private String linuxDriverPath;
//
//    @Value("${selenium.window.browser.binary}")
//    private String windowBrowserBinary;
//
//    @Value("${selenium.linux.browser.binary}")
//    private String linuxBrowserBinary;
//
//    @Value("${selenium.image.path}")
//    private String imagePath;
//
//    @PostConstruct
//    public void validateProperties() {
//        logger.info("Validating SeleniumConfig properties...");
//        validateProperty(appUrl, "selenium.app.url");
//        validateProperty(systemDriver, "selenium.system.driver");
//        validateProperty(windowDriverPath, "selenium.window.driver.path");
//        validateProperty(linuxDriverPath, "selenium.linux.driver.path");
//        validateProperty(windowBrowserBinary, "selenium.window.browser.binary");
//        validateProperty(linuxBrowserBinary, "selenium.linux.browser.binary");
//        validateProperty(imagePath, "selenium.image.path");
//        logger.info("SeleniumConfig initialized successfully with appUrl: " + appUrl + ", systemDriver: " + systemDriver);
//    }
//
//    private void validateProperty(String value, String propertyName) {
//        if (value == null || value.trim().isEmpty()) {
//            logger.severe(propertyName + " is not set or empty in application.properties");
//            throw new IllegalStateException(propertyName + " must be configured in application.properties");
//        }
//    }
//
//
//
//    public String getDriverType() {
//        return systemDriver;
//    }
//
//
//
//    public String getWindowBinaryPath() {
//        return windowBrowserBinary;
//    }
//
//    public String getLinuxBinaryPath() {
//        return linuxBrowserBinary;
//    }
//
//}