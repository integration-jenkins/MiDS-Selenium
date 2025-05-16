package avendum.com.midsauto.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DeployDashboard {
    public boolean launchPanIndiaPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try{
            WebElement panIndia = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-tabs/vaadin-tab[1]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", panIndia);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/div/vaadin-board/vaadin-board-row[7]/vaadin-vertical-layout[3]/vaadin-vertical-layout/vaadin-chart")));
        }catch (Exception e) {
            System.out.println("Pan India Page is not working");
            return false;
        }
        return true;
    }


    public boolean launchCircleWisePage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try{
            WebElement circleWise = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-tabs/vaadin-tab[2]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", circleWise);
            circleWise.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/div/vaadin-board/vaadin-board-row[9]/vaadin-vertical-layout[1]/vaadin-vertical-layout/vaadin-chart")));
        }catch (Exception e) {
            System.out.println("Circle Wise Page is not working");
            return false;
        }
        return true;
    }
}
