package avendum.com.midsauto.pages;

import avendum.com.midsauto.config.MyWebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private  WebDriver driver;
    public LoginPage(){
        MyWebDriverManager webDriverManager = new MyWebDriverManager();
        webDriverManager.setup();
        this.driver = webDriverManager.getDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:6086/");
    }
public void login(String username, String password) throws InterruptedException {
    Thread.sleep(2000);
    driver.findElement(By.name("username")).sendKeys(username);
    driver.findElement(By.name("password")).sendKeys(password);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
    loginButton.click();
    System.out.println("Login Successful");
}

    //separate login Page for other user accordingly
    public void login(WebDriver driver,String username, String password) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
        loginButton.click();
        System.out.println("Login Successful");
    }
}