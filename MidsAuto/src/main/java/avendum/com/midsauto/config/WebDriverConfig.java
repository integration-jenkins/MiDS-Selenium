package avendum.com.midsauto.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Professional-grade WebDriver management system with thread-safe implementation
 * and enhanced error handling. Maintains driver instances and configurations.
 */
public final class WebDriverConfig {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverConfig.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final String BASE_URL = "http://localhost:6086/";
    private static final int IMPLICIT_WAIT = 10; // Seconds

    private WebDriverConfig() {
        throw new IllegalStateException("Utility class - do not instantiate");
    }

    /**
     * Initializes and configures the WebDriver instance
     */
    public static synchronized void initializeDriver() {
        if (driverThread.get() != null) {
            logger.warn("Driver already initialized for this thread");
            return;
        }

        try {
            WebDriverManager.chromedriver().setup();
            ChromeDriver driver = new ChromeDriver(buildChromeOptions());
            configureDriver(driver);
            driverThread.set(driver);
            logger.info("Successfully initialized ChromeDriver for thread: {}",
                    Thread.currentThread().getId());
        } catch (Exception e) {
            logger.error("Driver initialization failed: {}", e.getMessage());
            throw new BrowserConfigException("WebDriver initialization failure", e);
        }
    }

    /**
     * Retrieves the active WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            logger.error("Driver accessed before initialization");
            throw new BrowserConfigException("WebDriver not initialized");
        }
        return driverThread.get();
    }

    /**
     * Safely terminates the WebDriver instance
     */
    public static void terminateDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Successfully terminated driver for thread: {}",
                        Thread.currentThread().getId());
            } catch (Exception e) {
                logger.error("Driver termination failed: {}", e.getMessage());
            }
            driverThread.remove();
        }
    }

    /**
     * Builds and configures Chrome browser options
     */
    private static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--disable-web-security",
                "--allow-running-insecure-content",
                "--remote-allow-origins=*",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-infobars",
                "--disable-notifications"
        );

        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
            logger.debug("Configured headless browser mode");
        }

        return options;
    }

    /**
     * Applies universal driver configurations
     */
    private static void configureDriver(WebDriver driver) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(IMPLICIT_WAIT)
        );
        driver.get(BASE_URL);
    }

    /**
     * Custom exception for browser configuration errors
     */
    private static class BrowserConfigException extends RuntimeException {
        public BrowserConfigException(String message) {
            super(message);
        }

        public BrowserConfigException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}