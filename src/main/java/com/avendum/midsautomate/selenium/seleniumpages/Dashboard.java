package com.avendum.midsautomate.selenium.seleniumpages;

import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class Dashboard {

    private static final Logger logger = Logger.getLogger(Dashboard.class.getName());
    public boolean launchPanIndiaPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try{
            WebElement panIndia = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-tabs/vaadin-tab[1]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", panIndia);
            //Here i find additional element in the Page to make it load complete page then perform other taks
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-board/vaadin-board-row[2]/vaadin-board-row/vaadin-horizontal-layout")));
            }catch (Exception e) {
            logger.info("Pan India Page is not working");
            return false;
        }
        return true;
    }

    public boolean launchCircleWisePage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(180));
        try{
            WebElement circleWise = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-tabs/vaadin-tab[2]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", circleWise);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-board/vaadin-board-row[3]/vaadin-board-row/vaadin-accordion/vaadin-accordion-panel")));

        }catch (Exception e) {
            logger.info("Circle Wise Page is not working");
            return false;
        }
        return true;
    }
}
