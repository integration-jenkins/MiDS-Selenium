package avendum.com.midsauto;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PlanIdGetter {
    String SR_RFAI[] = {"SR Pending", "SP Pending", "SO Pending", "RFAI Pending"};
    String Material[] = {"MO Pending", "Material Delivery Pending"};
    String INC[] = {"HOP Alignment Pending/Phy-AT Pending", "HOP Alignment Pending/Phy-AT Raised", "HOP Alignment Pending/Phy-AT Rejected", "HOP Alignment Pending/Phy-AT Accept","I&C Pending"};
    String AT[] = {"PHY-AT RAISED/SOFT-AT PENDING", "PHY-AT REJECTED/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT RAISED", "AT ACCEPTED", "PHY-AT PENDING/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT PENDING", "Phy+Soft Pending","PHY+SOFT AT PENDING", "PHY-AT ACCEPTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT RAISED", "PHY-AT REJECTED/SOFT-AT PENDING", "PHY-AT RAISED/SOFT-AT ACCEPTED"};
    String Traffic[] = {"TS Completed"};

    String Cancel[] = {"Canceled","Request for Cancellation","Material Returned"};
    String softUpgrade[]={"Upgrade Pending","Upgrade Completed"};

    private int stage=-1;
    public String planId(String start) throws InterruptedException {
        WebDriver driver= Base.getDriver();

        if (belongsToArray(start, SR_RFAI)) {
            stage=1;
        } else if (belongsToArray(start, Material)) {
            stage= 2;
        } else if (belongsToArray(start, INC)) {
            stage= 4;
        } else if (belongsToArray(start, AT)) {
            stage= 5;
        } else if (belongsToArray(start, Traffic)) {
            stage= 6;
        }else if(belongsToArray(start,Cancel)){
            stage=3;
        }else if(belongsToArray(start,softUpgrade)) {
            stage = 7;
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement stages =wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-button["+stage+"]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stages);
        System.out.println(stage+" Stage Clicked");
//        Select the Correct Plan Stage (Not Worked)
        SearchContext planStatus = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(2) > vcf-multiselect-combo-box:nth-child(8)")).getShadowRoot();
        SearchContext planStatus1 = planStatus.findElement(By.cssSelector("#input")).getShadowRoot();
        WebElement planStatusInput = planStatus1.findElement(By.cssSelector("input[placeholder='Select Plan Status']"));
        planStatusInput.sendKeys(start);
        Thread.sleep(500);
        Actions actions = new Actions(driver);
        actions.moveToElement(planStatusInput).click().sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
        System.out.println("Plan Status Selected");
        Thread.sleep(5000);
        SearchContext planIdCell = driver.findElement(By.cssSelector(".my-grid")).getShadowRoot();
        return planIdCell.findElement(By.cssSelector("#vaadin-grid-cell-7")).getText();
    }

    private boolean belongsToArray(String value, String[] array) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
    public int getStage(){
        return  stage;
    }

}