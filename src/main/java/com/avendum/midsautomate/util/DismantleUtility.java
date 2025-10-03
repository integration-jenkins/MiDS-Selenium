package com.avendum.midsautomate.util;

import com.avendum.midsautomate.enums.Departments;
import com.avendum.midsautomate.enums.DismantleStatus;
import com.avendum.midsautomate.enums.DismantleXPath;
import com.avendum.midsautomate.enums.TestRemark;
import com.avendum.midsautomate.model.DismantleTestHistory;
import com.avendum.midsautomate.model.DismantleTestResult;
import com.avendum.midsautomate.repository.DismantleTestResultRepository;
import com.avendum.midsautomate.selenium.TestGenerator.UploadTestContainer;
import com.avendum.midsautomate.selenium.dto.DismantleDataResource;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class DismantleUtility {

    private static final Logger log = LoggerFactory.getLogger(DismantleUtility.class);

    public static boolean testOverallPlanExecutionFlow(DismantleDataResource resource,
                                                    List<DismantleTestResult> results,
                                                    DismantleTestHistory history,
                                                       String driverPath, String binaryPath){

        String url = DismantleXPath.projectUrl;
//        String driverPath = "D:\\seleniumTesting\\SeleniumDismantleTesting\\driver\\geckodriver.exe";
        //        String binaryPath = "C:\\Users\\Kartik Lohate\\AppData\\Local\\Mozilla Firefox\\firefox.exe";

        System.setProperty("webdriver.gecko.driver", driverPath); // set Browser driver
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(binaryPath); // set used browser
        WebDriver webDriver = new FirefoxDriver(firefoxOptions);
        webDriver.get(url);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));

        log.info("Start to test Dismantle Plan");
        String hopDismantleId = resource.getHopId();
        String selectCircle = resource.getCircle();
        HashMap<String,List<String>> dismantleUser = resource.getDismantleUser();
//        String path = "C:\\Users\\Kartik Lohate\\Downloads\\dismantlePlannerUpload 4.xlsx";
        String path = resource.getFilePath();

        // Dismantle user with username & password
        List<String> operation = dismantleUser.get("Circle Operation Team");
        List<String> planner = dismantleUser.get("Circle MW Planner");
        List<String> deploy = dismantleUser.get("Circle Deployment Team");
        List<String> dincPartner = dismantleUser.get("Circle I&C Partner");

        int testPassCount = 0;
        boolean testCase = true;

        // planner login
         testCase = userLogin(wait,planner.get(0),planner.get(1));
         if(testCase){
             DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), TestRemark.LOGIN_SUCCESSFULLY.name(),"PASS",TestRemark.LOGIN_SUCCESSFULLY.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
             testPassCount++;
         }else{
            DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), TestRemark.LOGIN_FAILED.name(),"FAIL",TestRemark.LOGIN_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // upload plan by planner
        testCase = dismantlePlanUpload(wait,webDriver,selectCircle,path);
         if(testCase){
             DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), TestRemark.PLAN_UPLOAD_WORK.name(),"PASS",TestRemark.PLAN_UPLOAD_WORK.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
             testPassCount++;
         }else {
             DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), TestRemark.PLAN_UPLOAD_FAILED.name(),"FAIL",TestRemark.PLAN_UPLOAD_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
              return false;
         }

         // verify Uploaded Sheet holds Valid Data
         List<UploadTestContainer> bulkResult = testBulk(wait, planner.get(0),Departments.Circle_MW_Planner.departmentName());

         // if sheet is vaild and uploaded
         if(bulkResult.size() == 1 && bulkResult.get(0).getPlanUploadStatus().equalsIgnoreCase("Pass")){
             DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), DismantleStatus.DISMANTLE_DOABLE_STATUS_PENDING.getStatus(),"PASS",TestRemark.PLANNER_UPLOAD_SHEET_PASS.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
             testPassCount++;
         }else if (bulkResult.size()>1){ // if sheet holds invalid data fields
             for(UploadTestContainer i: bulkResult) {
                 String remark = "At row no. "+i.getRowNumber()+" got this error "+i.getErrorMessage();

                 DismantleTestResult testResult = createTestReport(
                         planner.get(0), Departments.Circle_MW_Planner.departmentName(),
                         TestRemark.PLANNER_UPLOAD_SHEET_FAIL.getStatus(),
                         "FAIL",remark,LocalDate.now(),hopDismantleId);

                 results.add(testResult);
             }
              return false;
         }else{ // sheet not process due to any kind of exceptions
             String remark = "Issue in Planner Bulk Upload, Sheet not process";
             DismantleTestResult testResult = createTestReport(
                     planner.get(0), Departments.Circle_MW_Planner.departmentName(),
                     TestRemark.PLANNER_UPLOAD_SHEET_FAIL.getStatus(),
                     "FAIL",remark,LocalDate.now(),hopDismantleId);

             results.add(testResult);
             return false;
         }

        // logOut by Planner
        testCase = logOut(webDriver);
         if(testCase){
             DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), TestRemark.LOGOUT_SUCCESSFULLY.name(),"PASS",TestRemark.LOGOUT_SUCCESSFULLY.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
             testPassCount++;
         }else {
             DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), TestRemark.LOGOUT_FAILED.name(),"FAIL",TestRemark.LOGOUT_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
              return false;
         }

        // operation login
        testCase = userLogin(wait,operation.get(0),operation.get(1));
        if(testCase){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), TestRemark.LOGIN_SUCCESSFULLY.name(),"PASS",TestRemark.LOGIN_SUCCESSFULLY.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else{
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), TestRemark.LOGIN_FAILED.name(),"FAIL",TestRemark.LOGIN_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // Open Uploaded Plan by circle operation test
        boolean openTrack = openTrackPage(webDriver,wait);
        boolean openPlan = openDismantlePlanByHopId(hopDismantleId,wait);

        if(openTrack && openPlan){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.DISMANTLE_DOABLE_STATUS_PENDING.getStatus(),"PASS",TestRemark.PLAN_OPEN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
        }else{
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.DISMANTLE_DOABLE_STATUS_PENDING.getStatus(),"FAIL",TestRemark.PLAN_NOT_OPEN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // fill Do-Able Inputs at "Do-able Pending"
        testCase = fillOperationInputs(webDriver,wait);

        if(testCase){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.TRAFFIC_RELEASE_PENDING.getStatus(),"PASS",TestRemark.DO_ABLE_PASS.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
        }else{
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.DISMANTLE_DOABLE_STATUS_PENDING.getStatus(),"FAIL",TestRemark.DO_ABLE_FAIL.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // logOut by Operation user
        testCase = logOut(webDriver);
         if(testCase){
             DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.TRAFFIC_RELEASE_PENDING.getStatus(),"PASS",TestRemark.LOGOUT_SUCCESSFULLY.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
             testPassCount++;
         }else {
             DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.TRAFFIC_RELEASE_PENDING.getStatus(),"FAIL",TestRemark.LOGOUT_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
              return false;
         }

        // Planner login at "TS Release Pending"
        testCase = userLogin(wait,planner.get(0),planner.get(1));
         if(testCase){
             DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), DismantleStatus.TRAFFIC_RELEASE_PENDING.getStatus(),"PASS",TestRemark.LOGOUT_SUCCESSFULLY.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
             testPassCount++;
         }else {
             DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), DismantleStatus.TRAFFIC_RELEASE_PENDING.getStatus(),"FAIL",TestRemark.LOGOUT_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
              return false;
         }

        // approve Site Details
        testCase = approveSiteDetails(webDriver,wait,hopDismantleId);
        if(testCase){
            DismantleTestResult testResult = createTestReport(
                    planner.get(0), Departments.Circle_MW_Planner.departmentName(), DismantleStatus.TS_CONFIRMATION_PENDING.getStatus(),"PASS",TestRemark.TS_RUNNING_APPROVE.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else {
            DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), DismantleStatus.TRAFFIC_RELEASE_PENDING.getStatus(),"FAIL",TestRemark.TS_RUNNING_NOT_APPROVE.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // logout by Planner
        testCase = logOut(webDriver);
        if(!testCase){
            DismantleTestResult testResult = createTestReport(planner.get(0), Departments.Circle_MW_Planner.departmentName(), DismantleStatus.TS_CONFIRMATION_PENDING.getStatus(),"FAIL",TestRemark.LOGOUT_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // again Operation Login To fill Traffic_Shifting & IWAN_Status at "TS Confirmation Pending"
        testCase = userLogin(wait,operation.get(0),operation.get(1));
        if(!testCase){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.TS_CONFIRMATION_PENDING.getStatus(),"FAIL",TestRemark.LOGIN_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        openTrack = openTrackPage(webDriver,wait);
        openPlan = openDismantlePlanByHopId(hopDismantleId,wait);

        if(openTrack && openPlan){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.TS_CONFIRMATION_PENDING.getStatus(),"PASS",TestRemark.PLAN_OPEN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else{
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.TS_CONFIRMATION_PENDING.getStatus(),"FAIL",TestRemark.PLAN_NOT_OPEN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        String out = fillTrafficIwanStatus(webDriver,wait);
        if(out.equalsIgnoreCase("both work")){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.PARTNER_ALLOCATION_PENDING.getStatus(),"PASS",TestRemark.TS_IWAN_STATUS_WORK.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else if(out.equalsIgnoreCase("TS status")){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.TS_CONFIRMATION_PENDING.getStatus(),"FAIL",TestRemark.TS_STATUS_NOT_WORK.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }else{
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.IWAN_TS_PENDING.getStatus(),"FAIL",TestRemark.IWAN_STATUS_NOT_WORK.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // logout by Operation user
        logOut(webDriver);
        if(!testCase){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.PARTNER_ALLOCATION_PENDING.getStatus(),"FAIL",TestRemark.LOGOUT_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // login by Deployment user to allocate Partner at "Partner Allocation Pending"
        testCase = userLogin(wait,deploy.get(0),deploy.get(1));
        if(testCase){
             DismantleTestResult testResult = createTestReport(deploy.get(0), Departments.Circle_Deployment_Team.departmentName(), DismantleStatus.PARTNER_ALLOCATION_PENDING.getStatus(),"PASS",TestRemark.LOGIN_SUCCESSFULLY.getStatus(),LocalDate.now(),hopDismantleId);
             results.add(testResult);
             testPassCount++;
         }else{
            DismantleTestResult testResult = createTestReport(deploy.get(0), Departments.Circle_Deployment_Team.departmentName(), DismantleStatus.PARTNER_ALLOCATION_PENDING.getStatus(),"FAIL",TestRemark.LOGIN_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        String depName = "Circle I&C Partner";
        String userName = resource.getPartnerAllocationName();

        // Deployment user assign plan to DINC user
        testCase = planAssignment(wait,webDriver,depName,userName,hopDismantleId);

        if(testCase){
            DismantleTestResult testResult = createTestReport(deploy.get(0), Departments.Circle_Deployment_Team.departmentName(), DismantleStatus.MATERIAL_SURVEY_PENDING.getStatus(),"PASS",TestRemark.PLAN_ASSIGN_WORK.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else {
            DismantleTestResult testResult = createTestReport(deploy.get(0), Departments.Circle_Deployment_Team.departmentName(), DismantleStatus.PARTNER_ALLOCATION_PENDING.getStatus(),"FAIL",TestRemark.PLAN_NOT_ASSIGN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // logout by Deployment user
        logOut(webDriver);
        if(!testCase){
            DismantleTestResult testResult = createTestReport(deploy.get(0), Departments.Circle_Deployment_Team.departmentName(), DismantleStatus.MATERIAL_SURVEY_PENDING.getStatus(),"FAIL",TestRemark.LOGOUT_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // login by DINC at "Survey Dismantle Pending"
        testCase = userLogin(wait,dincPartner.get(0),dincPartner.get(1));
        if(testCase){
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.MATERIAL_SURVEY_PENDING.getStatus(),"PASS",TestRemark.LOGIN_SUCCESSFULLY.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else{
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.PARTNER_ALLOCATION_PENDING.getStatus(),"FAIL",TestRemark.LOGIN_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        openTrack = openTrackPage(webDriver,wait);
        openPlan = openDismantlePlanByHopId(hopDismantleId,wait);

        if(openTrack && openPlan){
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.MATERIAL_SURVEY_PENDING.getStatus(),"PASS",TestRemark.PLAN_OPEN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else{
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.MATERIAL_SURVEY_PENDING.getStatus(),"FAIL",TestRemark.PLAN_NOT_OPEN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

         //  fill Survey Details of Site A and Site B
        boolean surveyA = fillSurveyDetails(webDriver,wait,"1"); // for SiteA
        boolean surveyB = fillSurveyDetails(webDriver,wait,"2"); // for SiteB

        if(surveyA && surveyB){
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.MATERIAL_DISMANTLE_PENDING.getStatus(),"PASS",TestRemark.SURVEY_DETAILS_WORK_BOTH_SITE.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else {
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.MATERIAL_SURVEY_PENDING.getStatus(),"FAIL",TestRemark.SURVEY_DETAILS_BOTH_SITE_ISSUE.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        //  click Submit btn
        submitButton(webDriver);

        // fill component Details
        testCase = fillComponentsDetails(webDriver,wait);

        if(testCase){
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.DELOADING_SR_COMPLETED_NMS_DELETION_PENDING.getStatus(),"PASS",TestRemark.ALL_COMPONENT_FILLED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else {
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.MATERIAL_DISMANTLE_PENDING.getStatus(),"FAIL",TestRemark.ISSUE_IN_COMPONENT.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

//         again submit to change status
        submitButton(webDriver);
        submitButton(webDriver); // if some fields filled second times

        // DINC user Logout after fill survey & components details at "Deloading SR Completed / NMS Deletion Pending"
        testCase = logOut(webDriver);
        if(!testCase){
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.DELOADING_SR_COMPLETED_NMS_DELETION_PENDING.getStatus(),"FAIL",TestRemark.LOGOUT_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // operation user login to fill NMS status
        testCase = userLogin(wait,operation.get(0),operation.get(1));
        if(!testCase){
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.DELOADING_SR_COMPLETED_NMS_DELETION_PENDING.getStatus(),"FAIL",TestRemark.LOGIN_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // Open Uploaded Plan by circle operation test
        openTrack = openTrackPage(webDriver,wait);
        openPlan = openDismantlePlanByHopId(hopDismantleId,wait);

        if(openTrack && openPlan){
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.DELOADING_SR_COMPLETED_NMS_DELETION_PENDING.getStatus(),"PASS",TestRemark.PLAN_OPEN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else{
            DismantleTestResult testResult = createTestReport(dincPartner.get(0), Departments.Circle_INS_Partner.departmentName(), DismantleStatus.DELOADING_SR_COMPLETED_NMS_DELETION_PENDING.getStatus(),"FAIL",TestRemark.PLAN_NOT_OPEN.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // fill NMS status
        testCase = fillNMSStatus(webDriver);

        if(testCase){
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.DELOADING_SR_COMPLETED_NMS_DELETION_COMPLETED.getStatus(),"PASS",TestRemark.NMS_DONE.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
            testPassCount++;
        }else {
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.DELOADING_SR_COMPLETED_NMS_DELETION_PENDING.getStatus(),"FAIL",TestRemark.NMS_NOT_WORK.getStatus(),LocalDate.now(),hopDismantleId);
            results.add(testResult);
             return false;
        }

        // submit & logout and The Plan is completed at Stage of "Deloading SR Completed / NMS Deletion Completed"
        saveBtn(webDriver);

        logOut(webDriver);
        if(!testCase) {
            DismantleTestResult testResult = createTestReport(operation.get(0), Departments.Circle_Operation_Team.departmentName(), DismantleStatus.DELOADING_SR_COMPLETED_NMS_DELETION_COMPLETED.getStatus(), "FAIL", TestRemark.LOGOUT_FAILED.getStatus(), LocalDate.now(), hopDismantleId);
            results.add(testResult);
            return false;
        }

        return true;
    }


    private static DismantleTestResult createTestReport(String userName, String depName, String planStatus,
                                                        String testStatus, String remark, LocalDate date, String id) {

       DismantleTestResult result =  new DismantleTestResult();

        result.setDepartmentName(depName);
        result.setUserName(userName);
        result.setRemark(remark);
        result.setTestDate(date);
        result.setTestStatus(testStatus);
        result.setDismantleStatus(planStatus);
        result.setTestId(id);

        return result;
    }

    private static boolean fillNMSStatus(WebDriver driver){
        try{
            String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-form-layout:nth-child(2) > vaadin-combo-box:nth-child(16)";
            Thread.sleep(1000);
            SearchContext shadow0 = driver.findElement(By.cssSelector(cssSelectorForHost1)).getShadowRoot();
            Thread.sleep(1000);
            SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement nsmStatus = shadow1.findElement(By.cssSelector("input[placeholder='NMS Deletion Status']"));
            nsmStatus.clear();
            nsmStatus.sendKeys("Completed");
            nsmStatus.sendKeys(Keys.ENTER);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean openTrackPage(WebDriver webDriver,WebDriverWait wait){
        try{
            WebElement dismantlePlan = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DismantleXPath.dismantlePage)));
            dismantlePlan.click();

            // invoked Dismantle Tracking View page
            WebElement dismantleTrackPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DismantleXPath.dismantleTrackingView)));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();",dismantleTrackPage);
             return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public static boolean dismantlePlanUpload(WebDriverWait wait, WebDriver webDriver, String circle, String path){
        try {
            // dismantle manuBar
            WebElement dismantlePlan = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DismantleXPath.dismantlePage)));
            dismantlePlan.click();

            // invoked Dismantle upload page
            WebElement dismantleTrackPage = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DismantleXPath.dismantleUploadView)));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", dismantleTrackPage);

            // select Circle
            Thread.sleep(1000);
            SearchContext circleElement = webDriver.findElement(By.cssSelector("vaadin-combo-box[tabindex='0']")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext element = circleElement.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement webCircle = element.findElement(By.cssSelector("input[placeholder='Select Circle']"));
            webCircle.sendKeys(circle);
            webCircle.sendKeys(Keys.ENTER);

            // upload dismantle Plan
            SearchContext upload = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload"))).getShadowRoot();
            WebElement file = upload.findElement(By.cssSelector("input[type='file']"));
            file.sendKeys(path);
            SearchContext s0 = upload.findElement(By.cssSelector("vaadin-upload-file")).getShadowRoot();
            WebElement startButton = s0.findElement(By.cssSelector("div[part='start-button']"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", startButton);
            WebDriverWait waitt = new WebDriverWait(webDriver, Duration.ofSeconds(3));
            waitt.until(ExpectedConditions.elementToBeClickable(startButton));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", startButton);

            log.info("Plan Upload Successfully");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.info("Plan not uploaded, There any issue in Plan Upload");
        }
        return false;
    }
    public static boolean logOut(WebDriver driver) {
        try {
            Thread.sleep(10000);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Open the menu (first click)
            SearchContext shadow0 = driver.findElement(By.cssSelector("vaadin-menu-bar[role='menubar']")).getShadowRoot();
            SearchContext shadow1 = shadow0.findElement(By.cssSelector("vaadin-context-menu-item[theme='menu-bar-item']")).getShadowRoot();
            WebElement slotEl = shadow1.findElement(By.cssSelector("slot"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", slotEl);

            // Wait for overlay and then click logout
            // Wait until the overlay appears
            By logoutMenuSelector = By.cssSelector("vaadin-context-menu-overlay vaadin-context-menu-list-box vaadin-context-menu-item:nth-child(2)");
            wait.until(ExpectedConditions.presenceOfElementLocated(logoutMenuSelector));

            // Access shadow root of the logout item
            WebElement logoutMenu = driver.findElement(logoutMenuSelector);
            SearchContext shadowLogout = logoutMenu.getShadowRoot();
            WebElement logoutBtn = shadowLogout.findElement(By.cssSelector("div[part='content']"));

            // Click logout
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutBtn);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean userLogin(WebDriverWait wait, String userName, String password){
        try{
            WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
            usernameField.sendKeys(userName);
            WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.name("password")));
            passwordField.sendKeys(password);

            WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DismantleXPath.loginBtn)));
            webElement.click();

            log.info(userName+" Login Successfully : "+LocalDate.now());
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

   public static HashMap<String, List<String>> giveAllUsers(){
        HashMap<String, List<String>> dismantleUser = new LinkedHashMap<>();
        dismantleUser.put("Circle MW Planner", new ArrayList<>(Arrays.asList("jk_plan", "test@1234")));
        dismantleUser.put("Circle Operation Team", new ArrayList<>(Arrays.asList("jk_ops_dum", "test@123")));
        dismantleUser.put("Circle Deployment Team", new ArrayList<>(Arrays.asList("jk_deploy", "test@123")));
        dismantleUser.put("Circle I&C Partner", new ArrayList<>(Arrays.asList("jk_dummy", "jk_dummy@2003")));
        return dismantleUser;
    }


    public static boolean openDismantlePlanByHopId(String hopId,WebDriverWait wait){
        boolean gotHopId = false;
        try{
            int idx = 11;
            while(idx!=-1){
                try{
                    String hopIdPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content["+idx+"]";
                    Thread.sleep(100);
                    WebElement plan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(hopIdPath)));
                    if(hopId.equalsIgnoreCase(plan.getText())){
                        int trackIdx = idx - 9;
                        String trackingPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[" + trackIdx + "]/flow-component-renderer/vaadin-button";
                        Thread.sleep(1000);
                        WebElement trackElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(trackingPath)));
                        trackElement.click();
                        gotHopId = true;
                        return gotHopId;
                    }
                    idx+=20;
                }catch (Exception e){
                    e.printStackTrace();
                    idx = -1;
                    log.info("Hop Element Not Found");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gotHopId;
    }

    public static boolean fillOperationInputs(WebDriver webDriver, WebDriverWait wait){
        try {
            // fill Do-Able Status as Do-able
            Thread.sleep(1000);
            SearchContext shadow0 = webDriver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-form-layout:nth-child(2) > vaadin-combo-box:nth-child(13)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext shadow11 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement doAble = shadow11.findElement(By.cssSelector("input[placeholder='Do-able input']"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", doAble);
            doAble.sendKeys("Do-able");
            doAble.sendKeys(Keys.ENTER);

            // select cancel Cancel Confirmation
            Thread.sleep(1000);
            SearchContext shadow01211 = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-radio-group:nth-child(2) > vaadin-radio-button:nth-child(1)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement elm1 = shadow01211.findElement(By.cssSelector("label"));
            elm1.click();

            // click btn
            Thread.sleep(1000);
            SearchContext shadow024 = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-button:nth-child(2)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement conf = shadow024.findElement(By.cssSelector("#button"));
            conf.click();

            // add Site Section
            Thread.sleep(1000);
            SearchContext shadow = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-button:nth-child(1)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement addSiteBtf = shadow.findElement(By.cssSelector("#button"));
            try{
                addSiteBtf.click();
            } catch (Exception e) {
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", addSiteBtf);
                e.printStackTrace();
            }

            // add Sites ID
            Thread.sleep(1000);
            SearchContext shadow00 = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(6) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-combo-box:nth-child(1)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext shadow1 = shadow00.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement siteField = shadow1.findElement(By.cssSelector("input[role='combobox']"));
            siteField.sendKeys("Site_A");
            siteField.sendKeys(Keys.ENTER);

            // add Ts Running details
            SearchContext shadow01 = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(6) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-vertical-layout:nth-child(3) > vaadin-horizontal-layout:nth-child(2) > vaadin-checkbox:nth-child(1)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement tech = shadow01.findElement(By.cssSelector("label"));
            try{
                tech.click();
            } catch (Exception e) {
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", tech);
                e.printStackTrace();
            }

            Thread.sleep(1000);
            SearchContext shadow012 = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(6) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-button:nth-child(2)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement subMit = shadow012.findElement(By.cssSelector("#button"));
            try{
               subMit.click();
            } catch (Exception e) {
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", subMit);
                e.printStackTrace();
            }
            Thread.sleep(10000);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void saveBtn(WebDriver driver){
        try {
            Thread.sleep(1000);
            SearchContext shadow = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(2) > vaadin-button:nth-child(2)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement submit = shadow.findElement(By.cssSelector("#button"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void openTsRunningIcon(WebDriver driver, String hopId,WebDriverWait wait){
        try{
            int idx = 11;
            while(idx!=-1){
                try{
                    String hopIdPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content["+idx+"]";
                    Thread.sleep(100);
                    WebElement plan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(hopIdPath)));
                    if(hopId.equalsIgnoreCase(plan.getText())){
                        int trackIdx = idx - 8;
                        String trackingPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[" + trackIdx + "]/flow-component-renderer/vaadin-button";
                        Thread.sleep(100);
                        WebElement trackElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(trackingPath)));
                        trackElement.click();
                        Thread.sleep(3000);
                        return;
                    }
                    idx+=20;
                }catch (Exception e){
                    e.printStackTrace();
                    idx = -1;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void selectPlanByHopId(String hopId,WebDriverWait wait){
        try{
            int idx = 11;
            while(idx!=-1){
                try{
                    String hopIdPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content["+idx+"]";
                    Thread.sleep(100);
                    WebElement plan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(hopIdPath)));
                    if(hopId.equalsIgnoreCase(plan.getText())){
                        int trackIdx = idx - 10;
                        String trackingPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content["+trackIdx+"]/vaadin-checkbox";
                        Thread.sleep(100);
                        WebElement trackElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(trackingPath)));
                        trackElement.click();
                        return;
                    }
                    idx+=20;
                }catch (Exception e){
                    e.printStackTrace();
                    idx = -1;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean approveSiteDetails(WebDriver webDriver, WebDriverWait wait,String hopId){
        try {
            // open Track Page
            openTrackPage(webDriver, wait);

            // open TS running page
            openTsRunningIcon(webDriver, hopId, wait);

            // select Technology
            Thread.sleep(1000);
            SearchContext shadow = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-vertical-layout:nth-child(5) > vaadin-horizontal-layout:nth-child(2) > vaadin-checkbox:nth-child(1)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement subBtn = shadow.findElement(By.cssSelector("label"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", subBtn);

            // submit
            Thread.sleep(1000);
            SearchContext shadow01 = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(3) > vaadin-button:nth-child(2)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement subMit = shadow01.findElement(By.cssSelector("#button"));
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", subMit);
            Thread.sleep(1000);

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static String fillTrafficIwanStatus(WebDriver driver, WebDriverWait wait){
           //  fill Traffic Status
           try {
               Thread.sleep(1000);
               SearchContext shadow0 = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-form-layout:nth-child(2) > vaadin-combo-box:nth-child(14)")).getShadowRoot();
               Thread.sleep(1000);
               SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
               Thread.sleep(1000);
               WebElement trafficStatus = shadow1.findElement(By.cssSelector("input[placeholder='Traffic Shifting Status']"));
               trafficStatus.clear();
               trafficStatus.sendKeys("Completed");
               trafficStatus.sendKeys(Keys.ENTER);
               saveBtn(driver);
           } catch (Exception e) {
               e.printStackTrace();
               return "TS status";
           }

            // fill IWAN Status
            try {
                Thread.sleep(1000);
                SearchContext shadow00 = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-form-layout:nth-child(2) > vaadin-combo-box:nth-child(15)")).getShadowRoot();
                Thread.sleep(1000);
                SearchContext shadow11 = shadow00.findElement(By.cssSelector("#input")).getShadowRoot();
                Thread.sleep(1000);
                WebElement iwanStatus = shadow11.findElement(By.cssSelector("input[placeholder='IWAN TS Status']"));
                iwanStatus.clear();
                iwanStatus.sendKeys("Completed");
                iwanStatus.sendKeys(Keys.ENTER);

                saveBtn(driver);
            } catch (Exception e) {
                e.printStackTrace();
                return "IWAN status";
            }

        return "both work";
    }

    public static boolean planAssignment(WebDriverWait wait, WebDriver webDriver, String department, String userName,String hopId){
        try{
            // open the track page
            openTrackPage(webDriver, wait);

            // select the plan to assign DINC user
            selectPlanByHopId(hopId,wait);

            // select Department
            SearchContext shadow0 = webDriver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-combo-box:nth-child(2)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement e=shadow1.findElement(By.cssSelector("input[placeholder='Select Department']"));
            e.click();
            e.sendKeys(department);
            e.sendKeys(Keys.ENTER);

            // click the confirm button
            Thread.sleep(1000);
            SearchContext shadow00 = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext shadow11 = shadow00.findElement(By.cssSelector("vaadin-button[role='button'][theme='primary']")).getShadowRoot();
            Thread.sleep(1000);
            WebElement sbt = shadow11.findElement(By.cssSelector("#button"));
            sbt.click();

            // select user option
            Thread.sleep(18000);
            SearchContext selectUser = webDriver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-combo-box:nth-child(3)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext user = selectUser.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement usr =  user.findElement(By.cssSelector("input[placeholder='Select User']"));
            usr.sendKeys(userName);
            e.sendKeys(Keys.ENTER);

            Thread.sleep(1000);
            SearchContext shadow10 = webDriver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext shadow101 = shadow10.findElement(By.cssSelector("vaadin-button[role='button'][theme='primary']")).getShadowRoot();
            Thread.sleep(1000);
            shadow101.findElement(By.cssSelector("#button")).click();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    // This method Help to provide the sample survey data
    private static void getSurveyData(Map<String, List<List<String>>> surveyDate) {

        // for Survey Model Data Fields
        List<List<String>> surveyDataList = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("1", "Antenna Size-A", "0.9")),
                new ArrayList<>(Arrays.asList("1", "ODU Modal A", "RAU")),
                new ArrayList<>(Arrays.asList("1", "IDU Modal A", "AMM-20P")),
                new ArrayList<>(Arrays.asList("1", "Modem Model-A", "MMU-2E")),
                new ArrayList<>(Arrays.asList("1","Mdu Modal-B","ISV3"))
        ));
        surveyDate.put("combo-box", surveyDataList);

//        // for Survey Qty fields
//        List<List<String>> qtyList = new ArrayList<>(Arrays.asList(
//                new ArrayList<>(Arrays.asList("2", "", "10")),
//                new ArrayList<>(Arrays.asList("2", "", "3")),
//                new ArrayList<>(Arrays.asList("2", "", "4")),
//                new ArrayList<>(Arrays.asList("2", "", "2"))
//        ));
//        surveyDate.put("integer-field", qtyList);

        // for Survey Serial Number
        List<List<String>> serialList = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("3", "", "AntennaSerial")),
                new ArrayList<>(Arrays.asList("3", "", "Hello_op")),
                new ArrayList<>(Arrays.asList("3", "", "Serial_sample_Test")),
                new ArrayList<>(Arrays.asList("3", "", "Testing")),
                new ArrayList<>(Arrays.asList("3", "", "sample_Test"))
        ));
        surveyDate.put("text-field", serialList);
    }


    public static boolean fillSurveyDetails(WebDriver webDriver, WebDriverWait wait,String siteValue){
        try{
            Map<String,List<List<String>>> surveyDate = new LinkedHashMap<>();
            getSurveyData(surveyDate);
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            String formattedDate = date.format(formatter);

            //This Element is inside 2 nested shadow DOM.
            String surveyDataPath = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+siteValue+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-date-picker:nth-child(2)";
            Thread.sleep(1000);
            SearchContext surveyDateField = webDriver.findElement(By.cssSelector(surveyDataPath)).getShadowRoot();
            Thread.sleep(1000);
            SearchContext survey = surveyDateField.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement surveyElm = survey.findElement(By.cssSelector("input[part='value']"));
            surveyElm.sendKeys(formattedDate);
            surveyElm.sendKeys(Keys.ENTER);

            for(Map.Entry<String,List<List<String>>> i:surveyDate.entrySet()){
                String field_name = i.getKey();
                List<List<String>> field = i.getValue();
                int surveyIdx = 3;

                for(List<String> j:field) {
                    String field_index = j.get(0);
                    String field_placeholder = j.get(1);
                    String field_value =j.get(2);

                    String cssSelectorPath = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+siteValue+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child("+surveyIdx+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(2) > vaadin-"+field_name+":nth-child("+field_index+")";

                    try {
                        System.out.println(field_name + " : file_path " + cssSelectorPath);
                        Thread.sleep(1000);
                        SearchContext shadow0 = webDriver.findElement(By.cssSelector(cssSelectorPath)).getShadowRoot();
                        Thread.sleep(1000);

                        if ("combo-box".equals(field_name)) {
                            SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
                            Thread.sleep(1000);
                            WebElement surveyAntenna = shadow1.findElement(By.cssSelector("input[role='combobox']"));
                            surveyAntenna.sendKeys(field_value);
                        } else {
                            WebElement surveyAntenna = shadow0.findElement(By.cssSelector("input[part='value']"));
                            surveyAntenna.sendKeys(field_value);
                        }
                        surveyIdx += 1;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private static void submitButton(WebDriver driver){
        //This Element is inside single shadow DOM.
        try {
            Thread.sleep(1000);
            SearchContext shadow = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(2) > vaadin-button:nth-child(3)")).getShadowRoot();
            Thread.sleep(1000);
            WebElement submit = shadow.findElement(By.cssSelector("#button"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", submit);
            submit.click();
            Thread.sleep(8000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static boolean fillComponentsDetails(WebDriver driver, WebDriverWait wait) {
        try{

            String[] site = {"1","2"}; // (siteA, siteB)
            String[] component = {"2","3","4","5","6"};  // (Antenna, Odu, Idu, Modem, Mdu)
            String[] category = {"1","2","3","4","5"}; // (traffic running, theft, cam, srn, running)

            for(String sit:site){
                for(String compIdx: component) {
                    // open the component
                    boolean x = openComponents(driver,sit,compIdx); // (driver, sideIndex(SiteA(1) & SiteB(2)), Component(Antenna(2),Odu(3)..)

                    if(!x) return false;

                    // select the category
                    if(compIdx.equals("2")){
                        boolean y =  selectCategory(driver, category[3], compIdx,sit); // (driver, Category, Component(Antenna(2)...)
                        boolean z = fillSrnDetails(driver,sit,compIdx);
                        if(!y || !z) return false;
                    }else{
                        boolean y = selectCategory(driver, category[2], compIdx,sit); // (driver, Category, Component(Antenna(2)...)
                        boolean z = fillCamDetails(driver,sit,compIdx);
                        if(!y || !z) return false;
                    }

//                    selectCategory(driver, category[2], compIdx,sit);
//
//                    // fill CAM Details
//                    fillCamDetails(driver,sit,compIdx);

                    // fill Traffic Running remark
//                    fillTrafficRunningRemark(driver,sit,compIdx);

                    // fill running remark
//                    fillRunningRemark(driver,sit,compIdx);

                    // fill Theft remarks
//                    fillTheftRemark(driver,sit,compIdx);

                    // fill srn details
//                    fillSrnDetails(driver,sit,compIdx);
                }


            }
           return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private static void fillTheftRemark(WebDriver driver, String sit, String compIdx) {
        try{
            String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+sit+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+compIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(4) > vaadin-horizontal-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-text-area:nth-child(1)";
            Thread.sleep(1000);
            SearchContext shadow = driver.findElement(By.cssSelector(cssSelectorForHost1)).getShadowRoot();
            Thread.sleep(1000);
            WebElement element = shadow.findElement(By.cssSelector("textarea[part='value']"));
            element.sendKeys("SampleTEst_ThRfT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fillRunningRemark(WebDriver driver, String sit, String compIdx) {
        try {
            //This Element is inside single shadow DOM.
//            String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+sit+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+compIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(6) > vaadin-horizontal-layout:nth-child(1) > vaadin-text-field:nth-child(1)";
            String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+sit+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+compIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(4) > vaadin-horizontal-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(1) > vaadin-text-field:nth-child(1)";
            Thread.sleep(1000);
            SearchContext shadow = driver.findElement(By.cssSelector(cssSelectorForHost1)).getShadowRoot();
            Thread.sleep(1000);
            WebElement element = shadow.findElement(By.cssSelector("input[part='value']"));
            element.sendKeys("sample_remark");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void fillTrafficRunningRemark(WebDriver driver, String sit, String compIdx) {
        try {
//            String trafficRemarkPath =  "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+sit+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+compIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-horizontal-layout:nth-child(5) > vaadin-text-field:nth-child(1)";
            String trafficRemarkPath = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+sit+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+compIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(4) > vaadin-horizontal-layout:nth-child(1) > vaadin-text-field:nth-child(1)";

            System.out.println("trafficPath = "+trafficRemarkPath);
            Thread.sleep(1000);
            SearchContext shadow = driver.findElement(By.cssSelector(trafficRemarkPath)).getShadowRoot();
            Thread.sleep(1000);
            WebElement element = shadow.findElement(By.cssSelector("input[part='value']"));
            element.sendKeys("sample_remark");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void getSrnData(Map<String, List<String>> srnDetail){
        LocalDate dates = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        String date = dates.format(formatter);

        srnDetail.put("1", Arrays.asList("text-field","REQ123"));
        srnDetail.put("2",Arrays.asList("date-picker",date));
        srnDetail.put("3",Arrays.asList("date-picker",date));
        srnDetail.put("6",Arrays.asList("text-field","serial_no_sample"));
        srnDetail.put("7",Arrays.asList("text-field","sample_item_code"));
        srnDetail.put("8",Arrays.asList("date-picker",date));
        srnDetail.put("9",Arrays.asList("text-field","sample_dc_no"));
        srnDetail.put("10",Arrays.asList("date-picker",date));
        srnDetail.put("11",Arrays.asList("date-picker",date));
        srnDetail.put("12",Arrays.asList("date-picker",date));
        srnDetail.put("13",Arrays.asList("date-picker",date));
        srnDetail.put("14",Arrays.asList("integer-field","1"));
        srnDetail.put("15",Arrays.asList("date-picker",date));
        srnDetail.put("16",Arrays.asList("text-field","status"));
        srnDetail.put("17",Arrays.asList("date-picker",date));
        srnDetail.put("18",Arrays.asList("integer-field","1"));
        srnDetail.put("19",Arrays.asList("date-picker",date));
        srnDetail.put("20",Arrays.asList("integer-field","12"));
        srnDetail.put("21",Arrays.asList("combo-box","OK"));
        srnDetail.put("22",Arrays.asList("combo-box","Done"));
        srnDetail.put("23",Arrays.asList("text-field","good"));
        srnDetail.put("24",Arrays.asList("date-picker",date));
        srnDetail.put("25",Arrays.asList("text-field","srNo12"));
    }

    private static boolean fillSrnDetails(WebDriver driver, String sit, String compIdx) {
        try{
            Map<String, List<String>> srnDetail = new LinkedHashMap<>();
            getSrnData(srnDetail);
            String[] placeHolder = {"value","number","Done","OK"};

            for(Map.Entry<String,List<String>> i:srnDetail.entrySet()){
                String fieldIdx = i.getKey();
                String fieldType = i.getValue().get(0);
                String fieldValue = i.getValue().get(1);

//                String srnFieldPath = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+sit+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+compIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(5) > vaadin-vertical-layout:nth-child(2) > vaadin-form-layout:nth-child(1) > vaadin-"+fieldType+":nth-child("+fieldIdx+")";
                String srnFieldPath = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+sit+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+compIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(4) > vaadin-form-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-form-layout:nth-child(1) > vaadin-"+fieldType+":nth-child("+fieldIdx+")";

                if(fieldType.equalsIgnoreCase("combo-box")){
                    if(fieldIdx.equalsIgnoreCase("21")){
                        Thread.sleep(1000);
                        SearchContext shadow0 = driver.findElement(By.cssSelector(srnFieldPath)).getShadowRoot();
                        Thread.sleep(1000);
                        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
                        Thread.sleep(1000);
                        WebElement element = shadow1.findElement(By.cssSelector("input[placeholder='"+placeHolder[3]+"']"));
                        element.sendKeys(fieldValue);
                        element.sendKeys(Keys.ENTER);
                    }else{
                        Thread.sleep(1000);
                        SearchContext shadow0 = driver.findElement(By.cssSelector(srnFieldPath)).getShadowRoot();
                        Thread.sleep(1000);
                        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
                        Thread.sleep(1000);
                        WebElement element = shadow1.findElement(By.cssSelector("input[placeholder='"+placeHolder[2]+"']"));
                        element.sendKeys(fieldValue);
                        element.sendKeys(Keys.ENTER);
                    }
                }else if(fieldType.equalsIgnoreCase("integer-field")){
                    Thread.sleep(1000);
                    SearchContext shadow = driver.findElement(By.cssSelector(srnFieldPath)).getShadowRoot();
                    Thread.sleep(1000);
                    WebElement element = shadow.findElement(By.cssSelector("input[type='"+placeHolder[1]+"']"));
                    element.sendKeys(fieldValue);
                    element.sendKeys(Keys.ENTER);
                }else if(fieldType.equalsIgnoreCase("date-picker")){
                    Thread.sleep(1000);
                    SearchContext shadow0 = driver.findElement(By.cssSelector(srnFieldPath)).getShadowRoot();
                    Thread.sleep(1000);
                    SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
                    Thread.sleep(1000);
                    WebElement element = shadow1.findElement(By.cssSelector("input[part='"+placeHolder[0]+"']"));
                    element.sendKeys(fieldValue);
                    element.sendKeys(Keys.ENTER);
                }else if(fieldType.equalsIgnoreCase("text-field")){
                    Thread.sleep(1000);
                    SearchContext shadow = driver.findElement(By.cssSelector(srnFieldPath)).getShadowRoot();
                    Thread.sleep(1000);
                    WebElement element = shadow.findElement(By.cssSelector("input[part='"+placeHolder[0]+"']"));
                    element.sendKeys(fieldValue);
                    element.sendKeys(Keys.ENTER);
                }

            }
            return true;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return false;
    }

//    private static boolean selectCategory(WebDriver driver, String categoryIdx, String compIdx, String sit) {
//        try{
//            String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+sit+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+compIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-radio-group:nth-child(3) > vaadin-radio-button:nth-child("+categoryIdx+")";
//            Thread.sleep(1000);
//            SearchContext shadow = driver.findElement(By.cssSelector(cssSelectorForHost1)).getShadowRoot();
//            Thread.sleep(1000);
//            WebElement element = shadow.findElement(By.cssSelector("label"));
//            System.out.println("FieldPathsx = "+cssSelectorForHost1);
//            try {
//                element.click();
//            } catch (Exception e) {
//                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
//            }
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return false;
//    }

    private static boolean selectCategory(WebDriver driver, String categoryIdx, String compIdx, String sit) {
        try {
            String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child(" + sit + ") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child(" + compIdx + ") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-radio-group:nth-child(3) > vaadin-radio-button:nth-child(" + categoryIdx + ")";

            Thread.sleep(1000);
            WebElement hostElement = driver.findElement(By.cssSelector(cssSelectorForHost1));
            SearchContext shadow = hostElement.getShadowRoot();
            Thread.sleep(1000);

            WebElement element = shadow.findElement(By.cssSelector("label"));

            // Scroll into view before click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            Thread.sleep(300);

            // First click attempt - normal
            try {
                element.click();
                return true; // Successful first click
            } catch (Exception e1) {
                System.out.println("First click failed, trying JavaScript click...");
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return true; // Successful second click
                } catch (Exception e2) {
                    System.out.println("JavaScript click also failed.");
                    e2.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("Error locating or interacting with the element.");
            e.printStackTrace();
        }

        return false;
    }

    private static boolean openComponents(WebDriver driver, String sit, String compIdx) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            String cssSelectorForHost1 = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child(" + sit + ") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child(" + compIdx + ")";

            // Wait for host to be visible
            WebElement hostElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelectorForHost1)));

            // Access shadow root
            SearchContext shadow = hostElement.getShadowRoot();

            // Find toggle element
            WebElement toggle = shadow.findElement(By.cssSelector("span[part='toggle']"));

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", toggle);

            // Wait until clickable
            wait.until(ExpectedConditions.elementToBeClickable(toggle));

            // Try clicking directly, fallback to JS
            try {
                toggle.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private static boolean fillCamDetails(WebDriver driver, String siteIdx,String comIdx) {
        try {
            Map<String,String> camDetails = new LinkedHashMap<>();
            camDetails.put("2","Pending on MS");
            camDetails.put("4","sampleSite_Id");
            camDetails.put("5","user_as_sample");
            camDetails.put("6","camName_sample");

            for (Map.Entry<String,String> i: camDetails.entrySet()) {
                try {
                    Thread.sleep(1000);
                    if ("2".equalsIgnoreCase(i.getKey())) {
                        String camFieldPath = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+siteIdx+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+comIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(4) > vaadin-horizontal-layout:nth-child(1) > vaadin-combo-box:nth-child("+i.getKey()+")";
                        SearchContext shadow0 = driver.findElement(By.cssSelector(camFieldPath)).getShadowRoot();
                        Thread.sleep(1000);
                        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
                        Thread.sleep(1000);
                        WebElement input = shadow1.findElement(By.cssSelector("input[placeholder='Select CAM Status']"));
                        input.sendKeys(i.getValue());
                        input.sendKeys(Keys.ENTER);
                    } else {
                        String camFieldPath = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-split-layout:nth-child(5) > vaadin-vertical-layout:nth-child("+siteIdx+") > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-accordion:nth-child(2) > vaadin-accordion-panel:nth-child("+comIdx+") > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(4) > vaadin-horizontal-layout:nth-child(1) > vaadin-text-field:nth-child("+i.getKey()+")";
                        Thread.sleep(1000);
                        SearchContext shadow = driver.findElement(By.cssSelector(camFieldPath)).getShadowRoot();
                        Thread.sleep(1000);
                        WebElement input = shadow.findElement(By.cssSelector("input[part='value']"));
                        input.sendKeys(i.getValue());
                    }

                } catch (Exception e) {
                    e.printStackTrace(); // You can log more context here if needed
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDismantleStatus(WebDriverWait wait){
        String status = "";
       try{
           String trackingPath = DismantleXPath.dismantleStatusPath;
           Thread.sleep(100);
           WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(trackingPath)));
           status = element.getText();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return status;
    }

    public static boolean bulkUploadTesting(DismantleDataResource resource,
                                            List<DismantleTestResult> results,String department, String driverPath, String binaryPath){
        try{
            String url = DismantleXPath.projectUrl;
//            String driverPath = "D:\\seleniumTesting\\SeleniumDismantleTesting\\driver\\geckodriver.exe";
            System.setProperty("webdriver.gecko.driver", driverPath); // set Browser driver
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary(binaryPath); // set used browser
            WebDriver webDriver = new FirefoxDriver(firefoxOptions);
            webDriver.get(url);
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));

            log.info("Start to test Dismantle Bulk Upload");
            String hopDismantleId = resource.getHopId();
            String selectCircle = resource.getCircle();
            HashMap<String,List<String>> dismantleUser = resource.getDismantleUser();
            String path = resource.getFilePath();

            System.out.println("Resource = "+resource);

            String userName = dismantleUser.get(department).get(0);
            String password = dismantleUser.get(department).get(1);

            int testPassCount = 0;
            boolean testCase = true;

            // planner login
            testCase = userLogin(wait,userName,password);
            if(testCase){
                DismantleTestResult testResult = createTestReport(userName, department, TestRemark.LOGIN_SUCCESSFULLY.name(),"PASS",TestRemark.LOGIN_SUCCESSFULLY.getStatus(),LocalDate.now(),hopDismantleId);
                results.add(testResult);
                testPassCount++;
            }else{
                DismantleTestResult testResult = createTestReport(userName, department, TestRemark.LOGIN_FAILED.name(),"FAIL",TestRemark.LOGIN_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
                results.add(testResult);
                return false;
            }

            // upload plan by planner
            testCase = dismantlePlanUpload(wait,webDriver,selectCircle,path);
            if(testCase){
                DismantleTestResult testResult = createTestReport(userName, department, TestRemark.PLAN_UPLOAD_WORK.name(),"PASS",TestRemark.PLAN_UPLOAD_WORK.getStatus(),LocalDate.now(),hopDismantleId);
                results.add(testResult);
                testPassCount++;
            }else {
                DismantleTestResult testResult = createTestReport(userName, department, TestRemark.PLAN_UPLOAD_FAILED.name(),"FAIL",TestRemark.PLAN_UPLOAD_FAILED.getStatus(),LocalDate.now(),hopDismantleId);
                results.add(testResult);
                return false;
            }

            // verify Uploaded Sheet holds Valid Data
            List<UploadTestContainer> bulkResult = testBulk(wait, userName,department);

            // if sheet is vaild and uploaded
            if(bulkResult.size() == 1 && bulkResult.get(0).getPlanUploadStatus().equalsIgnoreCase("Pass")){
                DismantleTestResult testResult = createTestReport(userName, department, DismantleStatus.DISMANTLE_DOABLE_STATUS_PENDING.getStatus(),"PASS",TestRemark.PLANNER_UPLOAD_SHEET_PASS.getStatus(),LocalDate.now(),hopDismantleId);
                results.add(testResult);
                testPassCount++;
            }else if (bulkResult.size()>1){ // if sheet holds invalid data fields
                for(UploadTestContainer i: bulkResult) {
                    String remark = "At row no. "+i.getRowNumber()+" got this error "+i.getErrorMessage();

                    DismantleTestResult testResult = createTestReport(
                            userName, department,
                            TestRemark.PLANNER_UPLOAD_SHEET_FAIL.getStatus(),
                            "FAIL",remark,LocalDate.now(),hopDismantleId);

                    results.add(testResult);
                }
                return false;
            }else{ // sheet not process due to any kind of exceptions
                String remark = "Issue in Planner Bulk Upload, Sheet not process";
                DismantleTestResult testResult = createTestReport(
                        userName, department,
                        TestRemark.PLANNER_UPLOAD_SHEET_FAIL.getStatus(),
                        "FAIL",remark,LocalDate.now(),hopDismantleId);

                results.add(testResult);
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static List<UploadTestContainer> testBulk(WebDriverWait wait, String userName, String departmentName) {
        List<UploadTestContainer> report = new ArrayList<>();

        try{
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DismantleXPath.plannerUploadStatusPath)));
            Map<String, Integer> recordDetails = getPlanRecordDataMap(element);

            if(recordDetails.get("Error In Records")==0 &&
                    (recordDetails.get("New Records")>0 || recordDetails.get("Total Updated Records")>0)){
                UploadTestContainer record = new UploadTestContainer();
                record.setPlanUploadStatus("Pass");
                record.setSuccessCount(recordDetails.get("Record(s)"));
                record.setUserName(userName);
                record.setDepartmentName(departmentName);
                if(recordDetails.get("New Records")>0 && recordDetails.get("Total Updated Records")>0){
                    record.setErrorMessage("No Error, Plans uploaded & updated");
                }else if(recordDetails.get("New Records")>0){
                    record.setErrorMessage("No Error, Plans uploaded");
                }else{
                    record.setErrorMessage("No Error, Plans updated");
                }
                report.add(record);
            }

            if(recordDetails.get("Error In Records")>0){
                int errorCount = recordDetails.get("Error In Records");
                int idx = 1;
                while(errorCount>0){
                    UploadTestContainer error = new UploadTestContainer();
                    String rowPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content["+idx+"]";
                    String errorMessage = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content["+(idx+1)+"]";
                    try{
                        WebElement rowElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowPath)));
                        WebElement errorElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(errorMessage)));

                        Integer i = Integer.parseInt(rowElement.getText());
                        String j = errorElement.getText();
                        error.setErrorMessage(j);
                        error.setRowNumber(i);

                        error.setUserName(userName);
                        error.setDepartmentName(departmentName);

                        Integer validRows = recordDetails.get("Record(s)") - recordDetails.get("Error In Records");
                        error.setSuccessCount(validRows);
                        error.setPlanUploadStatus("Test Fail");
                        report.add(error);

                    }catch (NullPointerException e){
                        e.printStackTrace();
                        log.info("Issue is Testing code.! some data is NULL");
                    }catch (Exception e){
                        e.printStackTrace();
                        log.info("Issue in Actual code");
                    }
                    idx += 2;
                    errorCount--;
                }
            }
            System.out.println("Message = "+report);
        }catch (NullPointerException e){
            e.printStackTrace();
            log.info("Issue is Testing code.! some data is NULL");
        }catch (Exception e){
            e.printStackTrace();
            log.info("Issue in Actual code");
        }

        return report;
    }

    private static Map<String, Integer> getPlanRecordDataMap(WebElement element) {
        String uploadMessage = element.getText();
        String[] uploadData = uploadMessage.split(",");

        Map<String,Integer> recordDetails = new HashMap<>();
        for (String part : uploadData) {
            String[] record = part.trim().split("=", 2);
            if (record.length == 2) {
                String key = record[0].trim();
                String valuePart = record[1].trim().replaceAll("[^0-9]", "");
                int value = Integer.parseInt(valuePart);
                recordDetails.put(key, value);
            }
        }
        return recordDetails;
    }




}
