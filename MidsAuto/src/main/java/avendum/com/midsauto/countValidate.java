package avendum.com.midsauto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class countValidate {
    public static void countValidate(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement mediaPlanningIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
        mediaPlanningIcon.click();
        System.out.println("Media Planning Icon Clicked");
        WebElement mwPlanTracking = driver.findElement(By.cssSelector("a.app-menu-item[href='home'] span"));
        mwPlanTracking.click();
        int Mwcount=count(driver,wait);
        System.out.println("MW Plan Tracking Count: "+Mwcount);
        WebElement deployReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/div/a/span")));
        deployReport.click();
        Thread.sleep(1000);
        WebElement subDb= driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]"));
        subDb.click();
        Thread.sleep(10000);
        int DRCount=count(driver,wait);
        System.out.println("Deploy Report Count: "+DRCount);
        Thread.sleep(1000);
        WebElement deployAss=driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[6]/iron-collapse-layout/vaadin-vertical-layout/a[3]/span[1]"));
        deployAss.click();
        int DeployCount=count(driver,wait);
        System.out.println("Deployment Assignment Count: "+DeployCount);
        WebElement lbReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/div/a/span")));
        lbReport.click();
        Thread.sleep(1000);
        WebElement subLb=driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[4]/iron-collapse-layout/vaadin-vertical-layout/a[1]/span[1]"));
        subLb.click();
        int LBCount=count(driver,wait);
        System.out.println("LB Report Count: "+LBCount);
        if(Mwcount==DRCount && Mwcount==LBCount && Mwcount==DeployCount) {
            System.out.println("Counts are same");
        }else{
            System.out.println("Counts are not same");
        }


    }
    public static int count(WebDriver driver,WebDriverWait wait){
        WebElement countP=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/span")));
        String value=countP.getText();
        String[] parts = value.split(": ");
        int no = Integer.parseInt(parts[1]);
        return no;
    }
}
