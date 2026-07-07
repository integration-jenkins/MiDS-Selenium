package com.avendum.midsautomate.selenium;

import com.avendum.midsautomate.selenium.seleniumpages.LoginPage;

import java.util.Arrays;

public class Start {
    public void UserLogin(String username, String password) throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        Arrays.asList("a","b");
    }

    }
