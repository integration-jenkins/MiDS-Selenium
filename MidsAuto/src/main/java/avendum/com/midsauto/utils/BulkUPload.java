package avendum.com.midsauto.utils;
import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BulkUPload {
    static WebDriver driver;

//    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

//    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("http://localhost:6086/");
    }

//    public static void main(String[] args) throws InterruptedException {
//        setUp();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//
//        driver.findElement(By.name("username")).sendKeys("JK_MS_PT_1");
//        driver.findElement(By.name("password")).sendKeys("adm@123");
//
//        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[part='vaadin-login-submit']")));
//        loginButton.click();
//        System.out.println("Login Successful");
//
//        WebElement bulkUploadButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[9]/a")));
//        bulkUploadButton.click();
//
//        SearchContext shadow0 = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-combo-box[tabindex='0']"))).getShadowRoot();
//        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
//        shadow1.findElement(By.cssSelector("input[placeholder='Select Circle']")).sendKeys("JK");
//        System.out.println("Circle Selected");
//
//        SearchContext upload = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload"))).getShadowRoot();
//        String path = "C:\\Users\\Bhanu\\Desktop\\My Documents\\Bulk and Manual AT test\\aa.xlsx";
//        WebElement file = upload.findElement(By.cssSelector("input[type='file']"));
//        file.sendKeys(path);
//        System.out.println("File Uploaded");
//        Thread.sleep(1000);
//        SearchContext s0 = upload.findElement(By.cssSelector("vaadin-upload-file")).getShadowRoot();
//        Thread.sleep(1000);
//        System.out.println("Ok");
////        SearchContext s1 = s0.findElement(By.xpath("vaadin-upload-file")).getShadowRoot();
////        Thread.sleep(1000);
////        s0.findElement(By.cssSelector("div[part='start-button']")).click();
//        System.out.println("Upload Buuuton Clicked");
//        WebElement startButton = s0.findElement(By.cssSelector("div[part='start-button']"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", startButton);
//        WebDriverWait waitt = new WebDriverWait(driver, Duration.ofSeconds(3));
//        waitt.until(ExpectedConditions.elementToBeClickable(startButton));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startButton);
//        System.out.println("Queue stage koi bye");
//    }
}