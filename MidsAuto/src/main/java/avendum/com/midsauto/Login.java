package avendum.com.midsauto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

class Login extends Base {

    public void Login(String username, String password) throws InterruptedException {
        // Initialize WebDriver
        Base base = new Base();
        setup();
        // Enter credentials
        Thread.sleep(2000);
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
        loginButton.click();
        System.out.println("Login Successful");
    }

    //separate login Page for other user accordingly
    public void Login(WebDriver driver,String username, String password) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
        loginButton.click();
        System.out.println("Login Successful");
    }
}