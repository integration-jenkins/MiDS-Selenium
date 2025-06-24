package com.avendum.midsautomate.selenium;

public class AutomationUtils {
    public static String getTime() {
        return java.time.LocalTime.now().toString();
    }
}