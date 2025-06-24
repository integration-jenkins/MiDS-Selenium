//package com.avendum.midsautomate.selenium.seleniumconfig;
//
//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//
//import java.io.File;
//import java.io.IOException;
//
//public class BastTest {
//    public static void main(String[] args){
//        try{
//        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
//        }catch (Exception e){
//            System.out.println("Not able to detect the chrome driver");
//            return;
//        }
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-web-security");
//        options.addArguments("--allow-running-insecure-content");
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless");
//
//        WebDriver driver = new ChromeDriver(options);
//        driver.manage().window().maximize();
//
//        SeleniumConfig seleniumConfig = new SeleniumConfig();
//        String url = seleniumConfig.getAppUrl();
//        System.out.println("App URL: " + url);
//
//        driver.get(url);
//        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        try {
//            FileUtils.copyFile(screenshot, new File("screenshot.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        Base.driver = driver;
//    }
//}
