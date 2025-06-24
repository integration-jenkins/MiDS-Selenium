package com.avendum.midsautomate;

import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import com.avendum.midsautomate.selenium.seleniumcontroller.MWPlanner;
import com.avendum.midsautomate.selenium.tests.LBReportTest;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
//public class TestingClass {

//    //Selenium testing
//    @Test
//    void lbSucessReportTest() throws InterruptedException {
//        MWPlanner mw=new MWPlanner();
//        LBReportTest lb=new LBReportTest();
//        mw.MWPlannerLogin("Z_Bhanu", "adm@123");
//        lb.successUpload("Raja");
//    }
//
//    @Test
//    void lbWrongReportTest() throws InterruptedException {
//        MWPlanner mw=new MWPlanner();
//        LBReportTest lb=new LBReportTest();
//        mw.MWPlannerLogin("Z_Bhanu", "adm@123");
//        lb.wrongFileUpload("Raja Beta");
//    }
//@Autowired
//private Base base;

//    @Test
//    public void test() {
//        WebDriver driver = base.getDriver(); // Initializes here
//        // ... test logic ...
//    }

//    @After
//    public void cleanup() {
//        base.tearDown(); // Quits driver
//    }

//}
