package avendum.com.midsauto.tests;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Manual_AT_Acpt_Rjt  {
    public static void accept_Reject_MSPartner(WebDriver driver,String planId,String comment, String detailComment,String service,String status,String stageTab) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        mediaPlanningIcon.click();
        System.out.println("Media Planning Icon Clicked");
        WebElement mwPlanTracking = driver.findElement(By.cssSelector("a.app-menu-item[href='home'] span"));
        mwPlanTracking.click();
        System.out.println("MW Plan Tracking Clicked");
        try{
        WebElement ATButton =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-button["+stageTab+"]")));
        ATButton.click();}
        catch (Exception e){
            WebElement ATButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-button[" + stageTab + "]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ATButton);
        }
//        System.out.println("AT Button Clicked");
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
        SearchContext shadow0 = driver.findElement(By.cssSelector("vaadin-select[tabindex='0']")).getShadowRoot();
        Thread.sleep(1000);
        SearchContext shadow1 = shadow0.findElement(By.cssSelector("vaadin-select-text-field[tabindex='0']")).getShadowRoot();
        Thread.sleep(1000);
        WebElement com= shadow1.findElement(By.cssSelector("#vaadin-select-text-field-input-114"));
        com.click();
        Thread.sleep(2000);
        if(service.equals("Phy") && status.equals("Accept")){
            WebElement shadow = driver.findElement(By.cssSelector("vaadin-item[value='1']"));
            shadow.click();
            Thread.sleep(1000);
            System.out.println("Accepted Phy clicked");
            Thread.sleep(1000);
            SearchContext phyAcpt= driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-text-area:nth-child(1)")).getShadowRoot();
            Thread.sleep(1000);
            phyAcpt.findElement(By.cssSelector("textarea[part='value']")).sendKeys(comment);
            System.out.println("Comment Added");
        } else if (service.equals("Phy") && status.equals("Reject")) {
            WebElement shadow = driver.findElement(By.cssSelector("vaadin-item[value='2']"));
            shadow.click();
            Thread.sleep(1000);
            System.out.println("Reject Phy clicked");
            SearchContext so = driver.findElement(By.cssSelector("vaadin-combo-box[tabindex='0'][theme='small']")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext st = so.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            st.findElement(By.cssSelector("input[role='combobox']"));
            System.out.println("Reject Reason wrote Comment");
            Thread.sleep(1000);
            SearchContext detailphyReject = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(2) > vaadin-text-area:nth-child(1)")).getShadowRoot();
            Thread.sleep(1000);
            detailphyReject.findElement(By.cssSelector("textarea[part='value']")).sendKeys(detailComment);
        }else if(service.equals("Soft") && status.equals("Accept")){
            WebElement shadow = driver.findElement(By.cssSelector("vaadin-item[value='1']"));
            shadow.click();
            Thread.sleep(1000);
            System.out.println("Accepted Soft clicked");
            Thread.sleep(1000);
            SearchContext softAccpt = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-text-area:nth-child(4)")).getShadowRoot();
            Thread.sleep(1000);
            softAccpt.findElement(By.cssSelector("#vaadin-text-area-input-117")).sendKeys(comment);
            System.out.println("Comment Added");
        }else if(service.equals("Soft") && status.equals("Reject")){
            WebElement shadow = driver.findElement(By.cssSelector("vaadin-item[value='2']"));
            shadow.click();
            Thread.sleep(1000);
            System.out.println("Soft Reject clicked");
            Thread.sleep(1000);
//            SearchContext softRejectComment = driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[2]/vaadin-horizontal-layout[1]/vaadin-combo-box[2]")).getShadowRoot();
//            Thread.sleep(1000);
//            SearchContext softReject = shadow0.findElement(By.cssSelector("vaadin-text-field")).getShadowRoot();
//            Thread.sleep(1000);
//            softReject.findElement(By.cssSelector("input[role='combobox']")).sendKeys(comment);
            //This Element is inside 2 nested shadow DOM.
            SearchContext so = driver.findElement(By.cssSelector("vaadin-combo-box[tabindex='0'][theme='small']")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext st = so.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            st.findElement(By.cssSelector("input[role='combobox']"));
            Thread.sleep(1000);
            SearchContext detailCt = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(2) > vaadin-text-area:nth-child(2)")).getShadowRoot();
            Thread.sleep(1000);
            detailCt.findElement(By.cssSelector("textarea[part='value']")).sendKeys(detailComment);
        }

//        Thread.sleep(1000);
//        SearchContext save = driver.findElement(By.cssSelector("vaadin-button[role='button'][theme='primary']")).getShadowRoot();
//        Thread.sleep(1000);
//        save.findElement(By.cssSelector("#button")).click();
    }
}
