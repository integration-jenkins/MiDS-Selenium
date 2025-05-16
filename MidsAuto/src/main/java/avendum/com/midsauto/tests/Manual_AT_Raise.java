package avendum.com.midsauto.tests;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Manual_AT_Raise {
    public static void MWPlanner_Raise(WebDriver driver,String planId,String service) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        mediaPlanningIcon.click();
        System.out.println("Media Planning Icon Clicked");
        WebElement mwPlanTracking = driver.findElement(By.cssSelector("a.app-menu-item[href='home'] span"));
        mwPlanTracking.click();
        System.out.println("MW Plan Tracking Clicked");
        WebElement ATButton =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-button[5]")));
        ATButton.click();
        System.out.println("AT Button Clicked");
        WebElement shadow_host = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[1072]/flow-component-renderer/div/vaadin-text-field")));
        SearchContext myShd = (SearchContext) shadow_host.getShadowRoot();
        WebElement inputElement = myShd.findElement(By.cssSelector("input"));
        inputElement.sendKeys(planId);
        System.out.println("Plan Id Entered");
        Thread.sleep(5000);
        WebElement shadow_host1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[2]/flow-component-renderer/vaadin-button")));
        SearchContext myShd1 = (SearchContext) shadow_host1.getShadowRoot();
        WebElement inputElement1 = myShd1.findElement(By.cssSelector("#button"));
        inputElement1.click();
        System.out.println("Search Button Clicked");
        WebElement ATButton2=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[5]")));
        ATButton2.click();
        System.out.println("AT clicked");
        if(service.equals("Phy")){
            WebElement phy=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-radio-group/vaadin-radio-button[1]/span")));
            phy.click();
            System.out.println("Phy clicked");
        }else if(service.equals("Soft")){
            WebElement soft=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-radio-group/vaadin-radio-button[2]")));
            soft.click();
            System.out.println("Soft clicked");
        }
        Thread.sleep(1000);
        SearchContext shadow = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-button:nth-child(2)")).getShadowRoot();
        Thread.sleep(1000);
        shadow.findElement(By.cssSelector("#button")).click();

//        Thread.sleep(1000);
//        SearchContext save = driver.findElement(By.cssSelector("vaadin-button[role='button'][theme='primary']")).getShadowRoot();
//        Thread.sleep(1000);
//        save.findElement(By.cssSelector("#button")).click();

    }
}
