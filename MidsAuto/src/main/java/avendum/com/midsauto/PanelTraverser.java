package avendum.com.midsauto;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PanelTraverser {


    //MW Plan Tracking Page
    public void navigateToMWPlanTrackingPage(WebDriver driver) throws InterruptedException {
        // Click on the MW Plan Tracking link
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mediaPlanningIcon);
        System.out.println("Media Planning Icon Clicked");
        WebElement mwPlanTracking = driver.findElement(By.cssSelector("a.app-menu-item[href='home'] span"));
        mwPlanTracking.click();
        System.out.println("MW Plan Tracking Clicked");
        mediaPlanningIcon.click();//closed the media planning icon so that it does not impact rest all the tests
    }

    public void navigateToLBreport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement LBReportIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", LBReportIcon);
        WebElement subLb=driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]"));
        subLb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", LBReportIcon);
    }

    public void navigateToDPRreport(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
        WebElement subDb= driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]"));
        subDb.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deployReport);
    }
    public void navigateToUBRLBReport(WebDriver driver){

    }

    public void navigateToRANMW(WebDriver driver){

    }
    public void navigateToPOPInfo(WebDriver driver){

    }

    public void navigateToAtomSummary(WebDriver driver){

    }
    public void navigateToWANIP(WebDriver driver){

    }

    public void navigateToSOFTAT(WebDriver driver){

    }

    public void navigateToDeployAssignment(WebDriver driver){

    }

    public void navigateToPRIIssue(WebDriver driver){

    }

    public void navigateToPRIReporting(WebDriver driver){

    }

    public void navigateToAssignmentReport(WebDriver driver){

    }

    public void navigateToMWLBRecon(WebDriver driver){

    }

    public void navigateToLBParameter(WebDriver driver){

    }
    public void navigateToHOPFrequencyReport(WebDriver driver){

    }

    public void navigateToRFC(WebDriver driver){

    }

    public void navigateToPlanUpload(WebDriver driver){

    }

    public void navigateToNepDismantle(WebDriver driver){

    }

    public void navigateToMWPlanTracking(WebDriver driver){

    }
    public void navigateToDismantleMaterial(WebDriver driver){

    }
    public void navigateToMidsNepDismantle(WebDriver driver){

    }
    public void navigateToSoftATUpload(WebDriver driver){

    }

    public void navigateToDeployreport(WebDriver driver){

    }

}
