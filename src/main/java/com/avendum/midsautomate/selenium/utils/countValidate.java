package com.avendum.midsautomate.selenium.utils;
import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class countValidate {

    private static final Logger logger = Logger.getLogger(countValidate.class.getName());
    public void countValidate(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        mediaPlanningIcon.click();
        logger.info("Media Planning Icon Clicked");
        WebElement mwPlanTracking = driver.findElement(By.cssSelector("a.app-menu-item[href='home'] span"));
        mwPlanTracking.click();
        int Mwcount=count(driver,wait);
        logger.info("MW Plan Tracking Count: "+Mwcount);
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        deployReport.click();
        Thread.sleep(1000);
        WebElement subDb= driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]"));
        subDb.click();
        Thread.sleep(10000);
        int DRCount=count(driver,wait);
        logger.info("Deploy Report Count: "+DRCount);
        Thread.sleep(1000);
        WebElement deployAss=driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]"));
        deployAss.click();
        int DeployCount=count(driver,wait);
        logger.info("Deployment Assignment Count: "+DeployCount);
        WebElement lbReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/div/a/span")));
        lbReport.click();
        Thread.sleep(1000);
        WebElement subLb=driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]"));
        subLb.click();
        int LBCount=count(driver,wait);
        logger.info("LB Report Count: "+LBCount);
        if(Mwcount==DRCount && Mwcount==LBCount && Mwcount==DeployCount) {
            logger.info("Counts are same");
        }else{
            logger.info("Counts are not same");
        }
    }
    public  int count(WebDriver driver,WebDriverWait wait){
        WebElement countP=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/span")));
        String value=countP.getText();
        String[] parts = value.split(": ");
        int no = Integer.parseInt(parts[1]);
        return no;
    }
}
