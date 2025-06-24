package com.avendum.midsautomate.selenium.seleniumpages;

import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest extends Base {
    public void Fun(){
        Base base=new Base();
        String username="Z_Bhanu";
        String password="adm@123";
        base.setup();
        WebDriver driver= base.getDriver();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/vaadin-login-overlay-wrapper/vaadin-login-form/vaadin-login-form-wrapper/form/vaadin-text-field/input")));
//        element.sendKeys("lakshmi");
//        driver.findElement(By.name("username")).sendKeys(username);
//        driver.findElement(By.name("password")).sendKeys(password);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        js.executeScript("arguments[0].value = '" + username + "';", usernameField);
        js.executeScript("arguments[0].value = '" + password + "';", passwordField);
    }
    public static void main(String[] args){
      LoginTest ll=new LoginTest();
      ll.Fun();

    }
}
