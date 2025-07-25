package com.avendum.midsautomate.selenium.utils;
import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import com.avendum.midsautomate.selenium.seleniumpages.DPRAutomate;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class PanelTraverser {
    private static final Logger logger = Logger.getLogger(PanelTraverser.class.getName());

    //Exception on page might occur if it does then we have to move to back
    public void exceptionThere(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[1]/div[1]/div/img")));
        } catch (Exception e) {
            logger.info("Could not detect the MIDS Logo on the page. Navigating back...");
            driver.navigate().back();
        }
    }



    //Dashboard
    public void navigateToDashboard(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement dashboard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[1]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dashboard);
    }

    //Deplouyment Dashboard
    public void navigateToDeployDashboard(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployDashboard = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[2]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployDashboard);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/div/vaadin-board/vaadin-board-row[1]/vaadin-board-row/vaadin-horizontal-layout[1]/label[1]")));
    }

    //RAN Mw planning
    public void navigateToRANMWPlanning(WebDriver driver) throws InterruptedException {
        try{
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mediaPlanningIcon);
        WebElement ranmwPlanTracking = driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]"));
        Thread.sleep(500);
        ranmwPlanTracking.click();
        Thread.sleep(500);
        mediaPlanningIcon.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button[3]")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-button:nth-child(2)")));
        }catch (Exception e){
            logger.info("Ran mw PlanTracking icon not iterable");
        }

    }
    //MW Plan Tracking Page
    public void navigateToMWPlanTrackingPage(WebDriver driver) throws InterruptedException {
        // Click on the MW Plan Tracking link
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mediaPlanningIcon);
        WebElement mwPlanTracking = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.app-menu-item[href='home'] span")));
        if (mwPlanTracking.isDisplayed() && mwPlanTracking.isEnabled()) {
            mwPlanTracking.click();
        } else {
            logger.info("Element is not interactable.");
        }
        mediaPlanningIcon.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-button:nth-child(1)")));
    }

    //MW LB Report -
    public void navigateToLBreport(WebDriver driver){
        try{
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement LBReportIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", LBReportIcon);
        WebElement sub_Lb=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        sub_Lb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", LBReportIcon);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vcf-multiselect-combo-box")));
        }catch (Exception e){
            logger.info("LB ReportIcon is not Iterable");
        }
    }

    //UBR LB Report -
    public void navigateToUBRLBReport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement UBRReportIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", UBRReportIcon);
        WebElement subLb=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subLb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", UBRReportIcon);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[2]")));
    }

    //POP Info Report-
    public void navigateToPOPInfo(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement pop = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[5]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pop);
        WebElement subLb=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[5]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        subLb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pop);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[427]/vaadin-grid-sorter")));

    }
    //ATOM RA Summary-
    public void navigateToAtomSummary(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement pop = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[5]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pop);
        WebElement subLb=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[5]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subLb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pop);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[350]/vaadin-grid-sorter")));

    }

    //WAN IP Information-
    public void navigateToWANIP(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement pop = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[5]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pop);
        WebElement subLb=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[5]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]")));
        subLb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pop);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[2]/vaadin-grid-sorter")));

    }
    //DPR Report-
    public void navigateToDPRreport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[3376]/vaadin-grid-sorter/flow-component-renderer/vaadin-horizontal-layout/span")));

    }

    //Soft AT report-
    public void navigateToSOFTAT(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[1]")));

    }

    //Deployment Assignment-
    public void navigateToDeployAssignment(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]"));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[801]/vaadin-grid-sorter")));

    }

    // PRI Issue-
    public void navigateToPRIIssue(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[4]/span[1]"));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[401]/vaadin-grid-sorter")));


    }

    //PRI Email Reporting-
    public void navigateToPRIReporting(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[5]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/h3")));
    }
    //Assignment Report (Take lots of time in loading)
    public void navigateToAssignmentReport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[6]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[177]")));

    }

    //LB recon-
    public void navigateToMWLBRecon(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[7]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[7]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[301]/vaadin-grid-sorter")));

    }

    // LB Parameter-
    public void navigateToLBParameter(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[7]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[7]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[1]/vaadin-grid-sorter")));

    }
    //HOP Frequency Report-
    public void navigateToHOPFrequencyReport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[7]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[7]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[127]/vaadin-grid-sorter")));

    }
    //RFC Report-
    public void navigateToRFC(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[8]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/h3")));

    }

    //Plan Upload-
    public void navigateToPlanUpload(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[9]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[1]")));

    }

    //NEP Dismantle-
    public void navigateToNepDismantle(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[10]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[10]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[726]/vaadin-grid-sorter")));

    }
    //Dismantle Material-
    public void navigateToDismantleMaterial(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[10]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[10]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[26]/vaadin-grid-sorter")));

    }
    //Mids Nep Dismantle-(Same Stucture as above one)
    public void navigateToMidsNepDismantle(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[10]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[10]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[1]/vaadin-grid-sorter")));

    }
    //Soft AT Upload-
    public void navigateToSoftATUpload(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[11]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[2]")));

    }
    //Mids DPR Upload-
    public void navigateToMidsDPRUpload(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[12]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload")));

    }

    //Frequency Detail upload-
    public void navigateToFrequencyDetailUpload(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[13]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[13]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload")));

    }

    //Frequency Detail report-
    public void navigateToFrequencyDetailReport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[13]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[13]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/h3")));

    }

    //Dismantel Track-
    public void navigateToDismantleTrack(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[14]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[14]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]")));

    }

    //Dismantle Report-
    public void navigateToDismantleReport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[14]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[14]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[104]/vaadin-grid-sorter")));

    }

    //Dismantle Upload-
    public void navigateToDismantleUpload(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[14]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[14]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload")));

    }
    //Change Assign User-
    public void navigateToChangeAssignUser(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[15]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout")));

    }
    //Create OEM Vendor-
    public void navigateToCreateOEMVendor(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[16]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-horizontal-layout")));

    }

    //Stock Dashboard-
    public void navigateToStockDashboard(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[17]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[17]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout")));
    }

    //Order Summary report-
    public void navigateToOrderSummaryReport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[17]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[17]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[926]/vaadin-grid-sorter")));
    }

    //Stock report-
    public void navigateToStockReport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[17]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[17]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[1]/vaadin-grid-sorter")));

    }

    //Item Code mapping-
    public void navigateToItemCodeMapping(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[17]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[17]/iron-collapse-layout/vaadin-vertical-layout/a[4]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[2]/vaadin-grid/vaadin-grid-cell-content[226]/vaadin-grid-sorter")));

    }

    //MW Plan Delete-
    public void navigateToMWPlanDelete(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[18]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload")));

    }

    //Traffic Upload(Not working)
    public void navigateToTrafficUpload(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[19]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[19]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[2]")));

    }

    //Traffic Report-
    public void navigateToTrafficReport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[19]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[19]/iron-collapse-layout/vaadin-vertical-layout/a[2]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[2]/vaadin-grid-sorter")));

    }

    //Traffic Track-
    public void navigateToTrafficTrack(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[19]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[19]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]")));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-button[1]")));

    }

    //Central Remark upload
    public void navigateToCentralRemarkUpload(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[20]/a/span[1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload")));
    }
}
