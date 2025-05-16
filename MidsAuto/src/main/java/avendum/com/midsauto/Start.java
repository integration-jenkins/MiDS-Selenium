package avendum.com.midsauto;

import avendum.com.midsauto.config.MyWebDriverManager;
import avendum.com.midsauto.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class Start {
    public void UserLogin(String username, String password) throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
    }

    }
