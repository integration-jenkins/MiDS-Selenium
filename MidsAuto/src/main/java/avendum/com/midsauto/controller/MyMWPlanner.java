package avendum.com.midsauto.controller;

import avendum.com.midsauto.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class MyMWPlanner {
    private WebDriver driver;
    public static void MWPlanner(String username, String password) throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);

    }

}
