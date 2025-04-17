package avendum.com.midsauto.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

/**
 * Professional-grade login automation class with enhanced reliability
 * and maintainability features
 */
public class LoginAutomation {
    private static final Logger logger = LoggerFactory.getLogger(LoginAutomation.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Element Locators
    private static final By USERNAME_FIELD = By.name("username");
    private static final By PASSWORD_FIELD = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("vaadin-button[part='vaadin-login-submit']");
    private static final By LOGOUT_BUTTON = By.id("logout-button"); // Example post-login element

    public LoginAutomation(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    /**
     * Performs login operation with provided credentials
     * @param username Valid user username
     * @param password Valid user password
     */
    public void performLogin(String username, String password) {
        try {
            navigateToLoginPage();
            enterCredentials(username, password);
            submitLogin();
            verifyLoginSuccess();
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            throw new AutomationException("Login operation failed", e);
        }
    }

    private void navigateToLoginPage() {
        if (!driver.getCurrentUrl().contains("login")) {
            driver.get("http://localhost:6086/login");
            wait.until(ExpectedConditions.urlContains("login"));
        }
    }

    private void enterCredentials(String username, String password) {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
        WebElement passwordInput = driver.findElement(PASSWORD_FIELD);

        usernameInput.clear();
        usernameInput.sendKeys(username);
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    private void submitLogin() {
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        loginBtn.click();
        wait.until(ExpectedConditions.invisibilityOf(loginBtn));
    }

    private void verifyLoginSuccess() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGOUT_BUTTON));
        logger.info("Successfully logged in as user");
    }

    /**
     * Custom exception for automation failures
     */
    public static class AutomationException extends RuntimeException {
        public AutomationException(String message) {
            super(message);
        }

        public AutomationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}