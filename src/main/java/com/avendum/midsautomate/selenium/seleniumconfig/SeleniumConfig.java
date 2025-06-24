package com.avendum.midsautomate.selenium.seleniumconfig;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class SeleniumConfig {
    private String appUrl;
    private static final Logger logger = Logger.getLogger(SeleniumConfig.class.getName());
    public SeleniumConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Unable to find application.properties");
                return;
            }
            prop.load(input);
            this.appUrl = prop.getProperty("app.url");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getAppUrl() {
        String myUrl = "";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Unable to find application.properties");
                return "Unable to Find application.properties";
            }
            prop.load(input);
            myUrl= prop.getProperty("app.url");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(myUrl==null || myUrl.length()<2){
            logger.info("Unable to fetch url from application property");
            return "http://10.175.75.150:8095/MWDT_CLIENT";
        }
        return myUrl;
    }
    public String getDriverType(){
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Unable to find application.properties");
                return "Unable to Find application.properties";
            }
            prop.load(input);
            return prop.getProperty("system.driver");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "failed";
    }

    public String getWindowDriverPath(){
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Unable to find application.properties");
                return "Unable to Find application.properties";
            }
            prop.load(input);
            return prop.getProperty("window.driver.path");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "failed";
    }
    public String getLinuxDriverPath(){
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Unable to find application.properties");
                return "Unable to Find application.properties";
            }
            prop.load(input);
            return prop.getProperty("linux.driver.path");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "failed";
    }

    public String getWindowBinaryPath() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Unable to find application.properties");
                return "Unable to Find application.properties";
            }
            prop.load(input);
            return prop.getProperty("window.browser.binary");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "failed";
    }
    public String getLiniuxBinaryPath() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Unable to find application.properties");
                return "Unable to Find application.properties";
            }
            prop.load(input);
            return prop.getProperty("linux.browser.binary");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "failed";
    }

    public String getImagePath(){
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Unable to find application.properties");
                return "Unable to Find application.properties";
            }
            prop.load(input);
            return prop.getProperty("image.path");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "failed";
    }
}