package com.avendum.midsautomate.testCode;

import java.time.Duration;
import java.util.*;

import static com.avendum.midsautomate.util.DismantleUtility.userLogin;
import static com.avendum.midsautomate.util.TrafficShiftingUtility.*;
import static java.lang.Math.abs;

import com.avendum.midsautomate.enums.DismantleXPath;
import com.avendum.midsautomate.enums.TrafficShiftingXPath;
import com.avendum.midsautomate.selenium.dto.BulkUploadSheetData;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainTest {
    public static void main(String[] args) {

        String url = DismantleXPath.projectUrl;
        String driverPath = "D:\\seleniumTesting\\SeleniumDismantleTesting\\driver\\geckodriver.exe";
        String binaryPath = "C:\\Users\\Kartik Lohate\\AppData\\Local\\Mozilla Firefox\\firefox.exe";

        System.setProperty("webdriver.gecko.driver", driverPath); // set Browser driver
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(binaryPath); // set used browser
        WebDriver webDriver = new FirefoxDriver(firefoxOptions);
        webDriver.get(url);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));

        userLogin(wait,"jk_plan","test@12345");
        System.out.println("change password = "+changePassword(webDriver));

    }


    public static boolean changePassword(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Open the menu (first click)
            Thread.sleep(4000);
            SearchContext shadow0 = driver.findElement(By.cssSelector("vaadin-menu-bar[role='menubar']")).getShadowRoot();
            SearchContext shadow1 = shadow0.findElement(By.cssSelector("vaadin-context-menu-item[theme='menu-bar-item']")).getShadowRoot();
            WebElement slotEl = shadow1.findElement(By.cssSelector("slot"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", slotEl);

            // click the change password button
            String changePassword = "//*[@id=\"overlay\"]/vaadin-context-menu-list-box/vaadin-context-menu-item[1]";
            WebElement changePasswordElm = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(changePassword)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", changePasswordElm);

            // fill current password
            Thread.sleep(1000);
            SearchContext shadow = driver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(8) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(2) > vaadin-password-field:nth-child(1)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement elm = shadow.findElement(By.cssSelector("input[type='password']"));
            elm.clear();
            elm.sendKeys("test@1234");

            // fill new password
            Thread.sleep(1000);
            SearchContext shadow11 = driver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(8) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(2) > vaadin-password-field:nth-child(2)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement passElm = shadow11.findElement(By.cssSelector("input[type='password']"));
            passElm.clear();
            passElm.sendKeys("test@1234");

            // click the save button
//            String savePath = "//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/vaadin-form-layout/vaadin-button[1]";
            String savePath = "/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-form-layout/vaadin-button[1]";
            WebElement saveElm = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(savePath)));
            saveElm.click();


            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static HashMap<String, List<String>> giveAllUsers() {
        HashMap<String, List<String>> dismantleUser = new LinkedHashMap<>();
        dismantleUser.put("Circle MW Planner", new ArrayList<>(Arrays.asList("jk_plan", "test@1234")));
        dismantleUser.put("Circle Operation Team", new ArrayList<>(Arrays.asList("jk_ops_dum", "test@1234")));
        dismantleUser.put("Circle Deployment Team", new ArrayList<>(Arrays.asList("jk_deploy", "test@1234")));
        dismantleUser.put("Circle I&C Partner", new ArrayList<>(Arrays.asList("jk_dummy", "test@1234")));
        return dismantleUser;
    }
}
