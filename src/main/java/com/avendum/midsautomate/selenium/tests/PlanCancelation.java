package com.avendum.midsautomate.selenium.tests;
import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class PlanCancelation {

    private static final Logger logger = Logger.getLogger(PlanCancelation.class.getName());
    public  void cancelPlan(WebDriver driver, String planId, String remark,String reason,String status) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        mediaPlanningIcon.click();
        logger.info("Media Planning Icon Clicked");
        WebElement mwPlanTracking = driver.findElement(By.cssSelector("a.app-menu-item[href='home'] span"));
        mwPlanTracking.click();
        logger.info("MW Plan Tracking Clicked");
        WebElement shadow_host = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[1072]/flow-component-renderer/div/vaadin-text-field")));
        SearchContext myShd = (SearchContext) shadow_host.getShadowRoot();
        WebElement inputElement = myShd.findElement(By.cssSelector("input"));
        inputElement.sendKeys(planId);
        logger.info("Plan Id Entered");
        Thread.sleep(5000);
        WebElement shadow_host1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[2]/flow-component-renderer/vaadin-button")));
        SearchContext myShd1 = (SearchContext) shadow_host1.getShadowRoot();
        WebElement inputElement1 = myShd1.findElement(By.cssSelector("#button"));
        inputElement1.click();
        logger.info("Search Button Clicked");
        Thread.sleep(1000);
        SearchContext cancelPanel=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-checkbox"))).getShadowRoot();
        cancelPanel.findElement(By.cssSelector("label")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("vaadin-text-field[class='planEditText']")).getShadowRoot().findElement(By.cssSelector("input[part='value']")).sendKeys(remark);
        Thread.sleep(1000);
        SearchContext shadow0 = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(2)")).getShadowRoot();
        Thread.sleep(1000);
        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(1000);
        shadow1.findElement(By.cssSelector("input[role='combobox']")).sendKeys(status);
        SearchContext reasonForCancelation = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(3)")).getShadowRoot();
        Thread.sleep(1000);
        SearchContext reasonForCancelationshadow1 = reasonForCancelation.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(1000);
        reasonForCancelationshadow1.findElement(By.cssSelector("input[role='combobox']")).sendKeys(reason);
        logger.info("Data filled");
        //        Thread.sleep(1000);
        SearchContext save = driver.findElement(By.cssSelector("vaadin-button[role='button'][theme='primary']")).getShadowRoot();
        Thread.sleep(1000);
        save.findElement(By.cssSelector("#button")).click();
    }

    public  void CanceledPlanRC(WebDriver driver, String planId,String service) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        mediaPlanningIcon.click();
        logger.info("Media Planning Icon Clicked");
        WebElement mwPlanTracking = driver.findElement(By.cssSelector("a.app-menu-item[href='home'] span"));
        mwPlanTracking.click();
        logger.info("MW Plan Tracking Clicked");
        WebElement shadow_host = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[1072]/flow-component-renderer/div/vaadin-text-field")));
        SearchContext myShd = (SearchContext) shadow_host.getShadowRoot();
        WebElement inputElement = myShd.findElement(By.cssSelector("input"));
        inputElement.sendKeys(planId);
        logger.info("Plan Id Entered");
        SearchContext shadow = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-grid:nth-child(5) > vaadin-grid-cell-content:nth-child(42) > vaadin-checkbox:nth-child(1)")).getShadowRoot();
        shadow.findElement(By.cssSelector("label")).click();
        SearchContext shadow0 = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-combo-box:nth-child(5)")).getShadowRoot();
        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
        WebElement inputField = shadow1.findElement(By.cssSelector("input[placeholder='Change Plan Status']"));
        inputField.sendKeys(service);
        inputField.sendKeys(Keys.ENTER);
    }
}
