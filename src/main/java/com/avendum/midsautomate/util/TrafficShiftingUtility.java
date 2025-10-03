package com.avendum.midsautomate.util;

import com.avendum.midsautomate.enums.*;
import com.avendum.midsautomate.model.DismantleTestResult;
import com.avendum.midsautomate.model.TrafficShiftingResultData;
import com.avendum.midsautomate.repository.TrafficShiftingTestHistoryRepository;
import com.avendum.midsautomate.selenium.TestGenerator.UploadTestContainer;
import com.avendum.midsautomate.selenium.dto.BulkUploadErrorReport;
import com.avendum.midsautomate.selenium.dto.BulkUploadSheetData;
import com.avendum.midsautomate.selenium.seleniumconfig.TrafficShiftingResourceCredentials;
import com.avendum.midsautomate.validation.TrafficShiftingValidations;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.avendum.midsautomate.util.DismantleUtility.*;
import static java.lang.Math.abs;

public class TrafficShiftingUtility {

    private static final Logger log = LoggerFactory.getLogger(TrafficShiftingUtility.class);

    public static boolean bulkUploadTest(List<TrafficShiftingResultData> trafficShiftingResultData, TrafficShiftingResourceCredentials resource, String driverPath, String binaryPath, String uniqueKey, String projectUrl, String department) {

        System.setProperty("webdriver.gecko.driver", driverPath); // set Browser driver
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(binaryPath); // set used browser
        WebDriver driver = new FirefoxDriver(firefoxOptions);
        driver.get(projectUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // set Wait to find element

        // fetch the user details
        HashMap<String, List<String>> dismantleUser = resource.getDismantleUser();
        List<String> user = dismantleUser.get(department);

        // fetch all the required details during the testing
        String circle = resource.getCircle();
        String planId = resource.getPlanId();
        String path = resource.getPath();

        String userName = user.get(0);

        boolean tested = false;

        try {
            // planner login
            tested = userLogin(wait, user.get(0), user.get(1));

            if (tested) {
                trafficShiftingResultData.add(createTSEntry(user.get(0), department, uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.LOGIN_SUCCESSFULLY.name(), TrafficShiftingRemark.LOGIN_SUCCESSFULLY.getStatus()));
            } else {
                trafficShiftingResultData.add(createTSEntry(user.get(0), department, uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.LOGIN_FAILED.name(), TrafficShiftingRemark.LOGIN_FAILED.getStatus()));
                return false;
            }

            // upload the plan
            tested = uploadTSPlan(wait, driver, circle, path);

            if (tested) {
                trafficShiftingResultData.add(createTSEntry(user.get(0), department, uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.BULK_UPLOAD_VALID.name(), TrafficShiftingRemark.BULK_UPLOAD_VALID.getStatus()));
            } else {
                trafficShiftingResultData.add(createTSEntry(user.get(0), department, uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.BULK_UPLOAD_INVALID.name(), TrafficShiftingRemark.BULK_UPLOAD_INVALID.getStatus()));
                return false;
            }

            // verify the upload data
            // verify Uploaded Sheet holds Valid Data
            List<UploadTestContainer> bulkResult = TsTestBulk(wait, userName, department);

            System.out.println("BulkResultData = " + bulkResult);

            // if sheet is vaild and uploaded
            if (bulkResult.size() == 1 && bulkResult.get(0).getPlanUploadStatus().equalsIgnoreCase("Pass")) {
                trafficShiftingResultData.add(createTSEntry(userName, department, uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.BULK_UPLOAD_VALID.name(), TrafficShiftingRemark.BULK_UPLOAD_VALID.getStatus()));

            } else if (!bulkResult.isEmpty()) { // if sheet holds invalid data fields
                for (UploadTestContainer i : bulkResult) {
                    String remark = "At row no. " + i.getRowNumber() + " got this error " + i.getErrorMessage();
                    trafficShiftingResultData.add(createTSEntry(userName, department, uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.SHEET_INVALID.name(), remark));
                }
                return false;
            } else { // sheet not process due to any kind of exceptions
                String remark = "Issue in Bulk Upload, Sheet not process";
                trafficShiftingResultData.add(createTSEntry(userName, department, uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.BULK_UPLOAD_INVALID.name(), remark));

                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return tested;
    }

    public static List<UploadTestContainer> TsTestBulk(WebDriverWait wait, String userName, String departmentName) {
        List<UploadTestContainer> report = new ArrayList<>();

        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TrafficShiftingXPath.uploadResultPath)));
            log.info("Upload Path data = " + element.getText());
            Map<String, Integer> recordDetails = getPlanRecordDataMap(element);

            if (recordDetails.get("Error In Records") == 0 && (recordDetails.get("New Records") > 0 || recordDetails.get("Total Updated Records") > 0)) {
                UploadTestContainer record = new UploadTestContainer();
                record.setPlanUploadStatus("Pass");
                record.setSuccessCount(recordDetails.get("Record(s)"));
                record.setUserName(userName);
                record.setDepartmentName(departmentName);
                if (recordDetails.get("New Records") > 0 && recordDetails.get("Total Updated Records") > 0) {
                    record.setErrorMessage("No Error, Plans uploaded & updated");
                } else if (recordDetails.get("New Records") > 0) {
                    record.setErrorMessage("No Error, Plans uploaded");
                } else {
                    record.setErrorMessage("No Error, Plans updated");
                }
                report.add(record);
            }

            if (recordDetails.get("Error In Records") > 0) {
                int errorCount = recordDetails.get("Error In Records");
                int idx = 1;
                while (errorCount > 0) {
                    UploadTestContainer error = new UploadTestContainer();
                    String rowPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[" + idx + "]";

                    String errorMessage = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[" + (idx + 1) + "]";
                    try {
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

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        log.info("Issue is Testing code.! some data is NULL");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("Issue in Actual code");
                    }
                    idx += 2;
                    errorCount--;
                }
            }
            System.out.println("Message = " + report);
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.info("Issue is Testing code.! some data is NULL");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Issue in Actual code");
        }

        return report;
    }

    private static Map<String, Integer> getPlanRecordDataMap(WebElement element) {
        String uploadMessage = element.getText();
        String[] uploadData = uploadMessage.split(",");

        Map<String, Integer> recordDetails = new HashMap<>();
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

    public static boolean testTSWorkFlow(List<TrafficShiftingResultData> trafficShiftingResultData, TrafficShiftingResourceCredentials resource, String driverPath, String binaryPath, String uniqueKey, String projectUrl) {

        System.setProperty("webdriver.gecko.driver", driverPath); // set Browser driver
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(binaryPath); // set used browser
        WebDriver driver = new FirefoxDriver(firefoxOptions);
        driver.get(projectUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // set Wait to find element

        // fetch the user details
        HashMap<String, List<String>> dismantleUser = resource.getDismantleUser();
        List<String> operation = dismantleUser.get("Circle Operation Team");
        List<String> planner = dismantleUser.get("Circle MW Planner");

        // fetch all the required details during the testing
        String circle = resource.getCircle();
        String planId = resource.getPlanId();
        String path = resource.getPath();
        String fillData = resource.getCheckHold(); // Yes / Hold
        String category = getRandomCategory();

        // planner login
        boolean tested = userLogin(wait, planner.get(0), planner.get(1));

        if (tested) {
            trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.LOGIN_SUCCESSFULLY.name(), TrafficShiftingRemark.LOGIN_SUCCESSFULLY.getStatus()));
        } else {
            trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.LOGIN_FAILED.name(), TrafficShiftingRemark.LOGIN_FAILED.getStatus()));
            return false;
        }

        // upload the plan
        tested = uploadTSPlan(wait, driver, circle, path);

        if (tested) {
            trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.BULK_UPLOAD_VALID.name(), TrafficShiftingRemark.BULK_UPLOAD_VALID.getStatus()));
        } else {
            trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.BULK_UPLOAD_INVALID.name(), TrafficShiftingRemark.BULK_UPLOAD_INVALID.getStatus()));
            return false;
        }

        List<UploadTestContainer> bulkResult = TsTestBulk(wait, planner.get(0), Departments.Circle_MW_Planner.departmentName());

        System.out.println("Bulk Result Data = " + bulkResult);

        // if sheet is vaild and uploaded
        if (bulkResult.size() == 1 && bulkResult.get(0).getPlanUploadStatus().equalsIgnoreCase("Pass")) {
            trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.BULK_UPLOAD_VALID.name(), TrafficShiftingRemark.BULK_UPLOAD_VALID.getStatus()));

        } else if (!bulkResult.isEmpty()) { // if sheet holds invalid data fields
            for (UploadTestContainer i : bulkResult) {
                String remark = "At row no. " + i.getRowNumber() + " got this error " + i.getErrorMessage();
                trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.SHEET_INVALID.name(), remark));
            }
            return false;
        } else { // sheet not process due to any kind of exceptions
            String remark = "Issue in Bulk Upload, Sheet not process";
            trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.BULK_UPLOAD_INVALID.name(), remark));

            return false;
        }

        // planner logout at "TS Pending
        tested = logOut(driver);

        if (tested) {
            trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.LOGOUT_SUCCESSFULLY.name(), TrafficShiftingRemark.LOGOUT_SUCCESSFULLY.getStatus()));
        } else {
            trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.LOGOUT_FAILED.name(), TrafficShiftingRemark.LOGOUT_FAILED.getStatus()));
            return false;
        }

        // operation user login to complete the TS Pending
        tested = userLogin(wait, operation.get(0), operation.get(1));

        if (tested) {
            trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.LOGIN_SUCCESSFULLY.name(), TrafficShiftingRemark.LOGIN_SUCCESSFULLY.getStatus()));
        } else {
            trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.LOGIN_FAILED.name(), TrafficShiftingRemark.LOGIN_FAILED.getStatus()));
            return false;
        }

        // open traffic shifting tracking page
        tested = openTSPage(wait, driver, planId);

        if (tested) {
            trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.PLAN_OPEN.name(), TrafficShiftingRemark.PLAN_OPEN.getStatus()));
        } else {
            trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.PLAN_NOT_OPEN.name(), TrafficShiftingRemark.PLAN_NOT_OPEN.getStatus()));
            return false;
        }

        if ("Yes".equalsIgnoreCase(fillData)) {  // fill "Yes" to complete TS Pending
            tested = fillTSYes(driver, "Yes");
            saveBtn(driver, wait);
            if (tested) {
                trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.TS_STATUS_FILLED.name(), TrafficShiftingRemark.TS_STATUS_FILLED.getStatus()));
            } else {
                trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.TS_STATUS_NOT_FILLED.name(), TrafficShiftingRemark.TS_STATUS_NOT_FILLED.getStatus()));
                return false;
            }
            logOut(driver);
        } else if ("Hold".equalsIgnoreCase(fillData)) {    // fill "Hold" to hold the plan
            try {
                tested = fillTSYes(driver, "hold");
                if (!tested) {
                    trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.HOLD_NOT_WORKING.name(), TrafficShiftingRemark.HOLD_NOT_WORKING.getStatus()));
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.HOLD_NOT_WORKING.name(), TrafficShiftingRemark.HOLD_NOT_WORKING.getStatus()));
                return false;
            }
            tested = fillHoldTS(driver, planId, category, wait);
            if (tested) {
                trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.HOLD_WORKING.name(), TrafficShiftingRemark.HOLD_WORKING.getStatus()));
            } else {
                trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.HOLD_NOT_WORKING.name(), TrafficShiftingRemark.HOLD_NOT_WORKING.getStatus()));
                return false;
            }
            logOut(driver);
            if ("planner".equalsIgnoreCase(getDepartmentByHoldCategory(category))) { // according to hold category, respective user login to complete the process
                boolean a = userLogin(wait, planner.get(0), planner.get(1));
                boolean b = openTSPage(wait, driver, planId);
                tested = resolveHold(driver, wait);
                if (tested && a && b) {
                    trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.HOLD_RESOLVE_WORKING.name(), TrafficShiftingRemark.HOLD_RESOLVE_WORKING.getStatus()));
                } else {
                    trafficShiftingResultData.add(createTSEntry(planner.get(0), Departments.Circle_MW_Planner.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.HOLD_RESOLVE_NOT_WORKING.name(), TrafficShiftingRemark.HOLD_RESOLVE_NOT_WORKING.getStatus()));
                    return false;
                }
            } else {
                boolean a = userLogin(wait, operation.get(0), operation.get(1));
                boolean b = openTSPage(wait, driver, planId);
                tested = resolveHold(driver, wait);
                if (tested && a && b) {
                    trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.HOLD_RESOLVE_WORKING.name(), TrafficShiftingRemark.HOLD_RESOLVE_WORKING.getStatus()));
                } else {
                    trafficShiftingResultData.add(createTSEntry(operation.get(0), Departments.Circle_Operation_Team.departmentName(), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.HOLD_RESOLVE_NOT_WORKING.name(), TrafficShiftingRemark.HOLD_RESOLVE_NOT_WORKING.getStatus()));
                    return false;
                }
            }
            logOut(driver);
        } else {
            log.info("Invalid data filling during TS completion");
        }

        if ("Hold".equalsIgnoreCase(fillData)) {
            // operation user login to complete the TS Pending
            boolean a = userLogin(wait, operation.get(0), operation.get(1));

            // open traffic shifting tracking page
            boolean b = openTSPage(wait, driver, planId);

            boolean c = fillTSYes(driver, "Yes");

            // click save btn to complete TS complete
            boolean d = saveBtn(driver, wait);

            tested = a && b && c && d;

            if (tested) {
                trafficShiftingResultData.add(createTSEntry(operation.get(0), operation.get(1), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.TS_STATUS_FILLED.name(), TrafficShiftingRemark.TS_STATUS_FILLED.getStatus()));
            } else {
                trafficShiftingResultData.add(createTSEntry(operation.get(0), operation.get(1), uniqueKey, planId, TestStatus.FAILED.name(), TrafficShiftingRemark.TS_STATUS_NOT_FILLED.name(), TrafficShiftingRemark.TS_STATUS_NOT_FILLED.getStatus()));
                return false;
            }
            logOut(driver);
        }

        // testing is complete of Traffic Shifting
        trafficShiftingResultData.add(createTSEntry(operation.get(0), operation.get(1), uniqueKey, planId, TestStatus.PASSED.name(), TrafficShiftingRemark.ALL_TEST_PASS.name(), TrafficShiftingRemark.ALL_TEST_PASS.getStatus()));

        return tested;
    }

    private static TrafficShiftingResultData createTSEntry(String userName, String department, String uniqueKey, String planId, String testStatus, String planStatus, String remark) {
        TrafficShiftingResultData entry = new TrafficShiftingResultData();
        entry.setUserName(userName);
        entry.setDepartment(department);
        entry.setUniquePlanId(uniqueKey);
        entry.setTestStatus(testStatus);
        entry.setPlanId(planId);
        entry.setPlanStatus(planStatus);
        entry.setRemark(remark);

        return entry;
    }

    private static String getRandomCategory() {
        List<String> categories = Arrays.asList("MEDIA ISSUE", "DESCOPE", "REVISE PLAN", "MATERIAL REQUIRED", "MW DEPLOYMENT PENDING", "ACCESS ISSUE", "POP NOT RFS");
        Random rand = new Random();
        return categories.get(rand.nextInt(categories.size()));
    }

    private static boolean resolveHold(WebDriver driver, WebDriverWait wait) {
        try {
            // click hold resolve button
            WebElement openTSNavBar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TrafficShiftingXPath.holdResolve)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", openTSNavBar);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openTSNavBar);

            // fill TS issue Resolve remark
            Thread.sleep(1000);
            SearchContext shadow = driver.findElement(By.cssSelector("#tsResolutionRemarks")).getShadowRoot();
            Thread.sleep(1000);
            WebElement elm = shadow.findElement(By.cssSelector("input[part='value']"));
            elm.sendKeys("resolve it");
            elm.sendKeys(Keys.ENTER);

            // fill TS issue Resolve date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            String formattedDate = date.format(formatter);

            Thread.sleep(1000);
            SearchContext shadow0 = driver.findElement(By.cssSelector("#tsHoldCloseDate")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement dateElm = shadow1.findElement(By.cssSelector("input[part='value']"));
            dateElm.sendKeys(formattedDate);
            dateElm.sendKeys(Keys.ENTER);

            Thread.sleep(1000);
            SearchContext shadow9 = driver.findElement(By.cssSelector("vaadin-button[role='button']")).getShadowRoot();
            Thread.sleep(1000);
            WebElement saveBt = shadow9.findElement(By.cssSelector("#button"));
            saveBt.click();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getDepartmentByHoldCategory(String holdCategory) {
        if (holdCategory == null) {
            return null;
        }

        switch (holdCategory) {
            case "MEDIA ISSUE":
            case "DESCOPE":
            case "REVISE PLAN":
            case "MATERIAL REQUIRED":
            case "MW DEPLOYMENT PENDING":
                return "PLANNER";

            case "ACCESS ISSUE":
            case "POP NOT RFS":
                return "OPERATION TEAM";

            default:
                return "UNKNOWN";

        }
    }

    private static boolean fillHoldTS(WebDriver driver, String site, String category, WebDriverWait wait) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            SearchContext shadow01 = wait.until(d -> d.findElement(By.cssSelector("#tsHoldCategory")).getShadowRoot());

            SearchContext shadow11 = wait.until(d -> shadow01.findElement(By.cssSelector("#input")).getShadowRoot());

            WebElement tsIssueELm = wait.until(d -> shadow11.findElement(By.cssSelector("input[role='combobox']")));

            tsIssueELm.sendKeys(category);
            tsIssueELm.sendKeys(Keys.ENTER);

            // --- Fill TS Issue Site ---
            WebElement siteElm = getShadowRootElement(driver, "#tsAccessIssueSite", "#input", "input[role='combobox']", wait);
            setValue(js, siteElm, site, true);

            // --- TS Remarks (short text field) ---
            WebElement remarkTs = getShadowRootElement(driver, "#tsHoldCategoryRemarks", "input[part='value']", wait);
            setValue(js, remarkTs, "Good", false);

            // --- General Remark (dialog text field) ---
            WebElement remark = getShadowRootElement(driver, "body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-vertical-layout:nth-child(2) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(1) > vaadin-text-field:nth-child(8)", "input[part='value']", wait);
            setValue(js, remark, "Plan need to Hold", false);

            // --- Save button ---
            WebElement saveBtn = getShadowRootElement(driver, ".ts-hold-save-button", "#button", wait);
            try {
                saveBtn.click();
            } catch (Exception e) {
                e.printStackTrace();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static WebElement getShadowRootElement(WebDriver driver, String hostSelector, String innerSelector, WebDriverWait wait) {
        WebElement host = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(hostSelector)));
        SearchContext shadow = host.getShadowRoot();
        return shadow.findElement(By.cssSelector(innerSelector));
    }

    private static WebElement getShadowRootElement(WebDriver driver, String hostSelector, String nestedHostSelector, String finalSelector, WebDriverWait wait) {
        WebElement host = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(hostSelector)));
        SearchContext shadow1 = host.getShadowRoot();
        WebElement nestedHost = shadow1.findElement(By.cssSelector(nestedHostSelector));
        SearchContext shadow2 = nestedHost.getShadowRoot();
        return shadow2.findElement(By.cssSelector(finalSelector));
    }

    private static void setValue(JavascriptExecutor js, WebElement inputElm, String value, boolean b) {
        js.executeScript("let input = arguments[0];" + "let host = input.getRootNode().host;" + // get vaadin-combo-box
                "host.value = arguments[1];" + "host.dispatchEvent(new CustomEvent('change', { bubbles: true }));", inputElm, value);
    }


    private static boolean fillTSYes(WebDriver driver, String val) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            System.out.println("Looking for #tsDoneStatus...");

            // Wait for element with more specific conditions
            WebElement tsDoneStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#tsDoneStatus")));
            System.out.println("Found #tsDoneStatus");

            // Check if element is actually visible and interactable
            if (!tsDoneStatus.isDisplayed()) {
                System.out.println("Element is not displayed, scrolling into view...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tsDoneStatus);
                Thread.sleep(500);
            }

            SearchContext shadow0 = tsDoneStatus.getShadowRoot();
            System.out.println("Accessed first shadow root");

            WebElement inputElement = wait.until(i -> {
                WebElement el = shadow0.findElement(By.cssSelector("#input"));
                return el.isDisplayed() ? el : null;
            });

            SearchContext shadow1 = inputElement.getShadowRoot();
            System.out.println("Accessed second shadow root");

            WebElement combobox = wait.until(ExpectedConditions.elementToBeClickable(shadow1.findElement(By.cssSelector("input[role='combobox']"))));

            combobox.clear();
            combobox.sendKeys(val);
            combobox.sendKeys(Keys.ENTER);

            System.out.println("Successfully filled TS with 'Yes'");
            return true;

        } catch (Exception e) {
            System.err.println("Error in fillTSYes: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public static boolean saveBtn(WebDriver driver, WebDriverWait wait) {
        try {
            WebElement openTSNavBar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TrafficShiftingXPath.saveBtn)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", openTSNavBar);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openTSNavBar);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean openTSPage(WebDriverWait wait, WebDriver driver, String planId) {
        try {
            // Click TS section in nav bar
            WebElement openTSNavBar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TrafficShiftingXPath.openTsPage)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", openTSNavBar);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openTSNavBar);

            WebElement openTS = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TrafficShiftingXPath.openTsTrackingPage)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", openTS);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openTS);

            // search the plan
            openTSPlanById(planId, driver, wait);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean openTSPlanById(String hopId, WebDriver driver, WebDriverWait wait) {
        boolean gotHopId = false;
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // first fetch grid
            WebElement grid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("vaadin-grid")));

            Long totalRows = (Long) js.executeScript("return arguments[0].items.length;", grid);
            System.out.println("Total rows in grid: " + totalRows);

            int idx = 5;      // column index start
            int rowIndex = 0; // grid row tracker
            int scrollIdx = 1;  // scroll idx
            while (rowIndex < totalRows * 10) {
                try {
                    String hopIdPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[" + idx + "]";
                    Thread.sleep(100);

                    WebDriverWait shortWaits = new WebDriverWait(driver, Duration.ofSeconds(1));
                    WebElement plan = shortWaits.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(hopIdPath)));

                    String planText = plan.getText().trim();
                    System.out.println("Row " + rowIndex + " → " + planText);

                    if (hopId.equalsIgnoreCase(planText)) {
                        int trackIdx = idx - 3;
                        String trackingPath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[" + trackIdx + "]/flow-component-renderer/vaadin-button";
                        System.out.println("Clicking at " + trackIdx + " : " + trackingPath);

                        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                        WebElement trackElement = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(trackingPath)));

                        // force click
                        js.executeScript("arguments[0].click();", trackElement);

                        gotHopId = true;
                        return gotHopId;
                    }

                    // move to next row (don’t skip)
                    rowIndex++;
                    idx += 19;

                } catch (Exception e) {
                    try {
                        grid = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("vaadin-grid")));
                        js.executeScript("arguments[0].scrollToIndex(arguments[1]);", grid, scrollIdx);
                        scrollIdx += 5;
                        Thread.sleep(200);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        return false;
                    }
                }
            }

            System.out.println("HopId " + hopId + " not found in grid!");
            gotHopId = false;

        } catch (Exception e) {
            e.printStackTrace();
            gotHopId = false;
        }
        return gotHopId;
    }


    public static boolean uploadTSPlan(WebDriverWait wait, WebDriver driver, String circle, String path) {
        try {
            // Click TS section in nav bar
            WebElement openTSNavBar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TrafficShiftingXPath.openTsPage)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", openTSNavBar);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openTSNavBar);

            // Open the TS plan upload
            WebElement openTSUpload = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(TrafficShiftingXPath.openTsUploadPage)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", openTSUpload);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openTSUpload);

            // select Circle
            Thread.sleep(1000);
            SearchContext circleElement = driver.findElement(By.cssSelector("vaadin-combo-box[tabindex='0']")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext element = circleElement.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement webCircle = element.findElement(By.cssSelector("input[placeholder='Select Circle']"));
            webCircle.sendKeys(circle);
            webCircle.sendKeys(Keys.ENTER);

            // upload The plan
            SearchContext upload = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload"))).getShadowRoot();
            WebElement file = upload.findElement(By.cssSelector("input[type='file']"));
            file.sendKeys(path);
            SearchContext s0 = upload.findElement(By.cssSelector("vaadin-upload-file")).getShadowRoot();
            WebElement startButton = s0.findElement(By.cssSelector("div[part='start-button']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", startButton);
            WebDriverWait waitt = new WebDriverWait(driver, Duration.ofSeconds(3));
            waitt.until(ExpectedConditions.elementToBeClickable(startButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startButton);

            log.info("Plan Uploaded..");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String generateUniquePlanId(TrafficShiftingTestHistoryRepository historyRepository, String planId) {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // 20250826
        long count = historyRepository.count() + 1; // sequence
        return planId + "-" + datePart + "-" + String.format("%03d", count);
    }


    // verify the bulk upload functionality and validate the user uploaded sheet

    public static List<BulkUploadErrorReport> validateBulkUploadSheet(String path, String circle) {
        List<BulkUploadErrorReport> errorList = new ArrayList<>();
        try {

            List<BulkUploadSheetData> list = validateBulkUpload(path);

            validateTrafficShiftingData(list, circle, errorList);

            log.info("Total Error List = " + errorList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorList;
    }

    public static List<BulkUploadSheetData> validateBulkUpload(String path) {
        List<BulkUploadSheetData> bulkDataList = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(new File(path)); XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream)) {

            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            if (!iterator.hasNext()) {
                System.out.println("Sheet is empty!");
                return new ArrayList<>();
            }

            // header
            Row headerRow = iterator.next();
            for (Cell cell : headerRow) {
                BulkUploadSheetData data = getBulkUploadSheetData(cell);
                bulkDataList.add(data);
            }

            // rows
            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (isRowEmpty(row)) {
                    continue;
                }

                int colIdx = 0;
                for (Cell cell : row) {
                    String cellValue = getCellValue(cell);

                    if (colIdx < bulkDataList.size() && !bulkDataList.get(colIdx).getColumnName().isEmpty()) {
                        bulkDataList.get(colIdx).getValue().add(cellValue);
                    }
                    colIdx++;
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error reading Excel file: " + path, ex);
        }

        return bulkDataList;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK && cell.toString().trim().length() > 0) {
                return false;
            }
        }
        return true;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private static BulkUploadSheetData getBulkUploadSheetData(Cell cell) {
        BulkUploadSheetData data = new BulkUploadSheetData();
        data.setRowNumber(1); // header row is always 1
        data.setColumnType(cell.getCellType().name());

        String columnName;
        switch (cell.getCellType()) {
            case NUMERIC:
                columnName = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                columnName = cell.getStringCellValue();
                break;
            case BOOLEAN:
                columnName = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
                columnName = "";
                break;
            default:
                columnName = "";
        }

        data.setColumnName(columnName != null ? columnName.trim() : "");
        data.setValue(new ArrayList<>());
        return data;
    }

    private static BulkUploadErrorReport createBulkError(String colName, String errorMessage, Integer rowNumber) {
        BulkUploadErrorReport bulkUploadErrorReport = new BulkUploadErrorReport();

        bulkUploadErrorReport.setRowNumber(rowNumber);
        bulkUploadErrorReport.setErrorMessage(errorMessage);
        bulkUploadErrorReport.setColumnName(colName);

        return bulkUploadErrorReport;
    }


    public static void validateTrafficShiftingData(List<BulkUploadSheetData> tsData, String circleId, List<BulkUploadErrorReport> errorList) {
        try {
            validateCircleData(tsData, circleId, errorList);
            validateSiteIdData(tsData, errorList);
            validateProjectData(tsData, errorList);
            validateRelocationPopData(tsData, errorList);
            validateConnectivityData(tsData, errorList);
            validatePlannedPopData(tsData, errorList);
            validatePlannedPcmData(tsData, errorList);
            validateNewHopInstallationData(tsData, errorList);
            validateHopIdDeploymentData(tsData, errorList);
            validateHopDismantleOemData(tsData, errorList);
            validateHopDismantleTypeData(tsData, errorList);
            validateTsPlanReleasedData(tsData, errorList);
            validateCategoryData(tsData, errorList);

            // Get RA validation data
            Map<String, List<String>> raNumber = new LinkedHashMap<>();
            TrafficShiftingValidations.getRaValidationList(raNumber);

            List<Boolean> validateByCategoryAndConnectivity = validateCategoryConnectivityCombination(tsData, errorList);

            validateDcnRAData(tsData, raNumber, errorList);
            validateDcnVlanData(tsData, validateByCategoryAndConnectivity, errorList);
            validateDcnGwData(tsData, validateByCategoryAndConnectivity, errorList);
            validateDcnSiteAData(tsData, validateByCategoryAndConnectivity, errorList);
            validateDcnSiteBData(tsData, validateByCategoryAndConnectivity, errorList);

            validate2GFields(tsData, errorList);
            validate5GFields(tsData, errorList);
            validate4GFields(tsData, errorList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void validateCircleData(List<BulkUploadSheetData> tsData, String circleId, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData circle = tsData.get(0);
        int rowCount = 2;
        for (String value : circle.getValue()) {
            if (!TrafficShiftingValidations.validateCircle(value, circleId)) {
                log.info("Circle is invalid of row no " + rowCount);
                errorList.add(createBulkError(circle.getColumnName(), "Filled Invalid Circle", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateSiteIdData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData siteId = tsData.get(1);
        int rowCount = 2;
        for (String value : siteId.getValue()) {
            if (value == null || value.trim().isEmpty() || value.equals("BLANK")) {
                log.info("SiteId is invalid at row no " + rowCount);
                errorList.add(createBulkError(siteId.getColumnName(), "SiteId cannot be empty", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateProjectData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData project = tsData.get(3);
        int rowCount = 2;
        for (String value : project.getValue()) {
            if (!TrafficShiftingValidations.validateProject(value)) {
                log.info(project.getColumnName() + " :Project is invalid of row no " + rowCount);
                errorList.add(createBulkError(project.getColumnName(), "Invalid project value", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateRelocationPopData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData relocationPop = tsData.get(4);
        int rowCount = 2;
        for (String value : relocationPop.getValue()) {
            if (!TrafficShiftingValidations.validateRelocation(value)) {
                errorList.add(createBulkError(relocationPop.getColumnName(), "Relocation Pop is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateConnectivityData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData connectivity = tsData.get(10);
        int rowCount = 2;
        for (String value : connectivity.getValue()) {
            if (!TrafficShiftingValidations.validateConnectivity(value)) {
                errorList.add(createBulkError(connectivity.getColumnName(), "Connectivity type is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validatePlannedPopData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData existingPop = tsData.get(6);
        List<String> connectivityValues = tsData.get(10).getValue();
        int rowCount = 2;

        for (int i = 0; i < existingPop.getValue().size(); i++) {
            String plannedPopVal = existingPop.getValue().get(i);
            String connVal = i < connectivityValues.size() ? connectivityValues.get(i) : null;

            if (connVal != null && !TrafficShiftingValidations.validatePlannedPop(plannedPopVal, connVal)) {
                errorList.add(createBulkError(existingPop.getColumnName(), "Planned Pop is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validatePlannedPcmData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData plannedPcmData = tsData.get(9);
        BulkUploadSheetData siteId = tsData.get(1);
        BulkUploadSheetData relocationPop = tsData.get(4);
        List<String> plannedPcmList = plannedPcmData.getValue();
        int rowCount = 2;

        for (int i = 0; i < relocationPop.getValue().size(); i++) {
            String plannedPcm = siteId.getValue().get(i) + "/" + relocationPop.getValue().get(i);
            if (!TrafficShiftingValidations.validatePlannedPCM(plannedPcm, plannedPcmList.get(i))) {
                errorList.add(createBulkError(plannedPcmData.getColumnName(), "Planned PCM Path is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateNewHopInstallationData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData newHopInstalled = tsData.get(11);
        int rowCount = 2;
        for (String value : newHopInstalled.getValue()) {
            if (!TrafficShiftingValidations.validateNewHopToBeInstalled(value)) {
                errorList.add(createBulkError(newHopInstalled.getColumnName(), "New Hop to be Installed (Y/N) is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateHopIdDeploymentData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData hopIdDeployed = tsData.get(12);
        int rowCount = 2;
        for (String value : hopIdDeployed.getValue()) {
            if (value != null && !value.isEmpty() && !value.contains("-")) {
                errorList.add(createBulkError(hopIdDeployed.getColumnName(), "Hop ID to be deployed is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateHopDismantleOemData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData hopDismantleOem = tsData.get(14);
        List<String> validOems = Arrays.asList("Ericsson", "Aviat", "Ceragoan", "Huawei");
        int rowCount = 2;

        for (String value : hopDismantleOem.getValue()) {
            if (value != null && !value.isEmpty() && !validOems.contains(value)) {
                errorList.add(createBulkError(hopDismantleOem.getColumnName(), "Dismantle HOP_OEM is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateHopDismantleTypeData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData hopDismantleType = tsData.get(15);
        List<String> validTypes = Arrays.asList("PDH", "Split", "Full-outdoor", "UBR", "UBR-EXT");
        int rowCount = 2;

        for (String value : hopDismantleType.getValue()) {
            if (value != null && !value.isEmpty() && !validTypes.contains(value)) {
                errorList.add(createBulkError(hopDismantleType.getColumnName(), "Dismantle HOP_Type is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateTsPlanReleasedData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData tsPlanReleased = tsData.get(16);
        List<String> validValues = Arrays.asList("Yes", "No");
        int rowCount = 2;

        for (String value : tsPlanReleased.getValue()) {
            if (value == null || value.isEmpty() || !validValues.contains(value)) {
                errorList.add(createBulkError(tsPlanReleased.getColumnName(), "TS Plan released(Y/N) is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateCategoryData(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData category = tsData.get(18);
        int rowCount = 2;
        for (String value : category.getValue()) {
            if (!TrafficShiftingValidations.validateCategory(value)) {
                errorList.add(createBulkError(category.getColumnName(), "Category is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static List<Boolean> validateCategoryConnectivityCombination(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData category = tsData.get(18);
        BulkUploadSheetData connectivity = tsData.get(10);
        List<Boolean> validationResults = new ArrayList<>();

        for (int i = 0; i < category.getValue().size(); i++) {
            boolean isValid = TrafficShiftingValidations.validateByCategoryAndConnectivity(category.getValue().get(i), connectivity.getValue().get(i));
            validationResults.add(isValid);
        }

        return validationResults;
    }

    private static void validateDcnRAData(List<BulkUploadSheetData> tsData, Map<String, List<String>> raNumber, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData dcnRa = tsData.get(19);
        BulkUploadSheetData project = tsData.get(3);
        BulkUploadSheetData relocationPop = tsData.get(4);
        int rowCount = 2;

        if (dcnRa.getValue().size() != project.getValue().size() || dcnRa.getValue().size() != relocationPop.getValue().size()) {
            errorList.add(createBulkError("DCN RA", "Input lists have different sizes", 0));
            return;
        }

        for (int i = 0; i < dcnRa.getValue().size(); i++) {
            String currentDcnRa = dcnRa.getValue().get(i);
            String currentProject = project.getValue().get(i);
            String currentRelocationPop = relocationPop.getValue().get(i);

            if (currentRelocationPop == null) {
                errorList.add(createBulkError("DCN RA", "Relocation POP is null", rowCount));
                rowCount++;
                continue;
            }

            if (TrafficShiftingValidations.validateDcnRA(currentProject, currentDcnRa)) {
                List<String> raList = raNumber.get(currentRelocationPop);
                if (raList != null && !raList.isEmpty()) {
                    if (!raList.contains(currentDcnRa)) {
                        errorList.add(createBulkError("DCN RA", currentDcnRa + " RA Number not exist", rowCount));
                    }
                } else {
                    errorList.add(createBulkError("DCN RA", currentRelocationPop + " pop id not exist", rowCount));
                }
            } else {
                errorList.add(createBulkError("DCN RA", currentDcnRa + " is not in AlphaNumerical order", rowCount));
            }
            rowCount++;
        }
    }

    private static void validateDcnVlanData(List<BulkUploadSheetData> tsData, List<Boolean> validationFlags, List<BulkUploadErrorReport> errorList) {
        validateConditionalField(tsData.get(20), tsData.get(3), validationFlags, "DCN VLAN", TrafficShiftingValidations::validateVlanRange, errorList);
    }

    private static void validateDcnGwData(List<BulkUploadSheetData> tsData, List<Boolean> validationFlags, List<BulkUploadErrorReport> errorList) {
        validateConditionalField(tsData.get(21), tsData.get(3), validationFlags, "DCN GW", TrafficShiftingValidations::isIpV4OrV6, errorList);
    }

    private static void validateDcnSiteAData(List<BulkUploadSheetData> tsData, List<Boolean> validationFlags, List<BulkUploadErrorReport> errorList) {
        validateConditionalField(tsData.get(22), tsData.get(3), validationFlags, "DCN Site A", TrafficShiftingValidations::isIpV4OrV6, errorList);
    }

    private static void validateDcnSiteBData(List<BulkUploadSheetData> tsData, List<Boolean> validationFlags, List<BulkUploadErrorReport> errorList) {
        validateConditionalField(tsData.get(23), tsData.get(3), validationFlags, "DCN Site B", TrafficShiftingValidations::isIpV4OrV6, errorList);
    }

    private interface FieldValidator {
        boolean validate(String value);
    }

    private static void validateConditionalField(BulkUploadSheetData fieldData, BulkUploadSheetData projectData, List<Boolean> validationFlags, String fieldName, FieldValidator validator, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < fieldData.getValue().size(); i++) {
            if (!"CSR".equals(projectData.getValue().get(i)) || validationFlags.get(i)) {
                if (!validator.validate(fieldData.getValue().get(i))) {
                    errorList.add(createBulkError(fieldName, fieldName + " is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }


    private static void validate2GFields(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        // 2G-BSC
        validate2GBsc(tsData.get(24), errorList);

        // 2G-(MPLS/MPBN/CEN)
        validate2GNetworkType(tsData.get(25), errorList);

        // 2G RA
        List<String> g2RaList = tsData.get(26).getValue();

        // 2G-UP-VLAN (ABIS)
        validate2GUpVlan(tsData.get(27), g2RaList, errorList);

        // 2G-UP Network IP (ABIS)
        validate2GUpNetworkIp(tsData.get(28), g2RaList, errorList);

        // 2G-UP GW (ABIS)
        validate2GUpGw(tsData.get(29), g2RaList, errorList);

        // Validate remaining 2G fields (columns 30-34)
        validate2GRemainingFields(tsData, g2RaList, errorList);
    }

    private static void validate2GBsc(BulkUploadSheetData g2BscData, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (String value : g2BscData.getValue()) {
            if (!value.isEmpty() && !TrafficShiftingValidations.validateAlphaNumericalData(value)) {
                errorList.add(createBulkError(g2BscData.getColumnName(), "2G BSC is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validate2GNetworkType(BulkUploadSheetData g2NetworkData, List<BulkUploadErrorReport> errorList) {
        List<String> validTypes = Arrays.asList("MPLS", "MPBN", "CEN");
        int rowCount = 2;
        for (String value : g2NetworkData.getValue()) {
            if (!value.isEmpty() && !validTypes.contains(value)) {
                errorList.add(createBulkError(g2NetworkData.getColumnName(), "2G Network Type is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validate2GUpVlan(BulkUploadSheetData g2UpVlanData, List<String> g2RaList, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < g2UpVlanData.getValue().size(); i++) {
            if (!"NA".equals(g2RaList.get(i))) {
                String vlan = g2UpVlanData.getValue().get(i);
                if (!TrafficShiftingValidations.validateVlanRange(vlan)) {
                    errorList.add(createBulkError(g2UpVlanData.getColumnName(), "2G UP VLAN (ABIS) is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate2GUpNetworkIp(BulkUploadSheetData g2UpNetworkData, List<String> g2RaList, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < g2UpNetworkData.getValue().size(); i++) {
            if (!"NA".equals(g2RaList.get(i))) {
                String ip = g2UpNetworkData.getValue().get(i);
                if (!TrafficShiftingValidations.isIpV4OrV6(ip)) {
                    errorList.add(createBulkError(g2UpNetworkData.getColumnName(), "2G-UP Network IP (ABIS) is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate2GUpGw(BulkUploadSheetData g2GwData, List<String> g2RaList, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < g2GwData.getValue().size(); i++) {
            if (!"NA".equals(g2RaList.get(i))) {
                String gw = g2GwData.getValue().get(i);
                if (!TrafficShiftingValidations.isIpV4OrV6(gw)) {
                    errorList.add(createBulkError(g2GwData.getColumnName(), "2G-UP GW (ABIS) is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate2GRemainingFields(List<BulkUploadSheetData> tsData, List<String> g2RaList, List<BulkUploadErrorReport> errorList) {
        List<String> g2MpVlanList = tsData.get(31).getValue();

        for (int i = 30; i < 35; i++) {
            BulkUploadSheetData cellData = tsData.get(i);
            String columnName = cellData.getColumnName();

            if (columnName.contains("2G")) {
                if (columnName.contains("VLAN")) {
                    validate2GVlanField(cellData, g2RaList, g2MpVlanList, columnName, errorList);
                } else if (columnName.contains("IP")) {
                    validate2GIpField(cellData, g2RaList, g2MpVlanList, columnName, errorList);
                }
            }
        }
    }

    private static void validate2GVlanField(BulkUploadSheetData vlanData, List<String> g2RaList, List<String> g2MpVlanList, String columnName, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < vlanData.getValue().size(); i++) {
            if (!"NA".equals(g2RaList.get(i))) {
                String vlan = vlanData.getValue().get(i);
                if (!TrafficShiftingValidations.validateVlanRange(vlan)) {
                    errorList.add(createBulkError(columnName, columnName + " is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate2GIpField(BulkUploadSheetData ipData, List<String> g2RaList, List<String> g2MpVlanList, String columnName, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < ipData.getValue().size(); i++) {
            if (!columnName.contains("MP") && !"NA".equals(g2RaList.get(i))) {
                String ip = ipData.getValue().get(i);
                if (!TrafficShiftingValidations.isIpV4OrV6(ip)) {
                    errorList.add(createBulkError(columnName, columnName + " is invalid", rowCount));
                }
            } else if (!g2MpVlanList.get(i).isEmpty()) {
                String ip = ipData.getValue().get(i);
                if (!TrafficShiftingValidations.isIpV4OrV6(ip)) {
                    errorList.add(createBulkError(columnName, columnName + " is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate5GFields(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        BulkUploadSheetData g5SiteAir = tsData.get(72);
        BulkUploadSheetData category = tsData.get(18);
        List<String> g2RaList = tsData.get(26).getValue();
        List<String> g4RaList = tsData.get(35).getValue();
        List<String> g5SiteAirList = g5SiteAir.getValue();
        List<String> categoryList = category.getValue();

        validate5GSiteOnAir(tsData.get(72), g2RaList, g4RaList, errorList);
        validate5GColoRA(tsData.get(73), g5SiteAirList, categoryList, errorList);
        validate5GVlan(tsData.get(74), g5SiteAirList, errorList);
        validate5GGw(tsData.get(75), g5SiteAirList, errorList);
        validate5GNbIp1(tsData.get(76), g5SiteAirList, errorList);
        validate5GNbIp2(tsData.get(77), errorList);
        validate5GAnchorGw(tsData.get(78), errorList);
        validate5GEnbIps(tsData, 79, 82, errorList);
    }

    private static void validate5GSiteOnAir(BulkUploadSheetData g5SiteAirData, List<String> g2RaList, List<String> g4RaList, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        List<String> validValues = Arrays.asList("Yes", "No");
        for (int i = 0; i < g5SiteAirData.getValue().size(); i++) {
            if (g2RaList.get(i).isEmpty() && g4RaList.get(i).isEmpty()) {
                String value = g5SiteAirData.getValue().get(i);
                if (!validValues.contains(value)) {
                    errorList.add(createBulkError(g5SiteAirData.getColumnName(), "5G Site On Air is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate5GColoRA(BulkUploadSheetData g5ColoRaData, List<String> g5SiteAirList, List<String> categoryList, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < g5ColoRaData.getValue().size(); i++) {
            if ("Yes".equalsIgnoreCase(g5SiteAirList.get(i)) && "Coco".equalsIgnoreCase(categoryList.get(i))) {
                String value = g5ColoRaData.getValue().get(i);
                if (!TrafficShiftingValidations.validateAlphaNumericalData(value)) {
                    errorList.add(createBulkError(g5ColoRaData.getColumnName(), "5G Colo RA is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate5GVlan(BulkUploadSheetData g5VlanData, List<String> g5SiteAirList, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < g5VlanData.getValue().size(); i++) {
            if ("Yes".equalsIgnoreCase(g5SiteAirList.get(i))) {
                String vlan = g5VlanData.getValue().get(i);
                if (!TrafficShiftingValidations.validateVlanRange(vlan)) {
                    errorList.add(createBulkError(g5VlanData.getColumnName(), "5G VLAN is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate5GGw(BulkUploadSheetData g5GwData, List<String> g5SiteAirList, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < g5GwData.getValue().size(); i++) {
            if ("Yes".equalsIgnoreCase(g5SiteAirList.get(i))) {
                String gw = g5GwData.getValue().get(i);
                if (!TrafficShiftingValidations.isIpV4OrV6(gw)) {
                    errorList.add(createBulkError(g5GwData.getColumnName(), "5G GW is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate5GNbIp1(BulkUploadSheetData gNbIp1Data, List<String> g5SiteAirList, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (int i = 0; i < gNbIp1Data.getValue().size(); i++) {
            if ("Yes".equalsIgnoreCase(g5SiteAirList.get(i))) {
                String ip = gNbIp1Data.getValue().get(i);
                if (!TrafficShiftingValidations.isIpV4OrV6(ip)) {
                    errorList.add(createBulkError(gNbIp1Data.getColumnName(), "gNB - IP1 is invalid", rowCount));
                }
            }
            rowCount++;
        }
    }

    private static void validate5GNbIp2(BulkUploadSheetData gNbIp2Data, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (String ip : gNbIp2Data.getValue()) {
            if (!ip.isEmpty() && !TrafficShiftingValidations.isIpV4OrV6(ip)) {
                errorList.add(createBulkError(gNbIp2Data.getColumnName(), "gNB - IP2 is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validate5GAnchorGw(BulkUploadSheetData g5AnchorGwData, List<BulkUploadErrorReport> errorList) {
        int rowCount = 2;
        for (String gw : g5AnchorGwData.getValue()) {
            if (!gw.isEmpty() && !TrafficShiftingValidations.isIpV4OrV6(gw)) {
                errorList.add(createBulkError(g5AnchorGwData.getColumnName(), "5G Anchor GW is invalid", rowCount));
            }
            rowCount++;
        }
    }

    private static void validate5GEnbIps(List<BulkUploadSheetData> tsData, int startIndex, int endIndex, List<BulkUploadErrorReport> errorList) {
        for (int j = startIndex; j < endIndex; j++) {
            BulkUploadSheetData ipData = tsData.get(j);
            int rowCount = 2;
            for (String ip : ipData.getValue()) {
                if (!ip.isEmpty() && !TrafficShiftingValidations.isIpV4OrV6(ip)) {
                    errorList.add(createBulkError(ipData.getColumnName(), ipData.getColumnName() + " is invalid", rowCount));
                }
                rowCount++;
            }
        }
    }

    private static void validate4GFields(List<BulkUploadSheetData> tsData, List<BulkUploadErrorReport> errorList) {
        // Validate 4G BBU sections
        validate4GBbuSection(tsData.get(51), tsData.get(52), tsData.get(53), "4G BBU2-MP", errorList);
        validate4GBbuSection(tsData.get(54), tsData.get(55), tsData.get(56), "4G BBU3-UP", errorList);
        validate4GBbuSection(tsData.get(57), tsData.get(58), tsData.get(59), "4G BBU3-CP", errorList);
        validate4GBbuSection(tsData.get(60), tsData.get(61), tsData.get(62), "4G BBU3-MP", errorList);
        validate4GBbuSection(tsData.get(63), tsData.get(64), tsData.get(65), "4G BBU4-UP", errorList);
        validate4GBbuSection(tsData.get(66), tsData.get(67), tsData.get(68), "4G BBU4-CP", errorList);
        validate4GBbuSection(tsData.get(69), tsData.get(70), tsData.get(71), "4G BBU4-MP", errorList);
    }

    private static void validate4GBbuSection(BulkUploadSheetData vlanData, BulkUploadSheetData gwData, BulkUploadSheetData ipData, String sectionName, List<BulkUploadErrorReport> errorList) {
        List<String> vlanList = vlanData.getValue();
        List<String> gwList = gwData.getValue();
        List<String> ipList = ipData.getValue();
        int rowCount = 2;

        for (int i = 0; i < vlanList.size(); i++) {
            String vlan = vlanList.get(i);
            String gw = gwList.get(i);
            String ip = ipList.get(i);

            // VLAN validation
            if (!vlan.isEmpty() && !TrafficShiftingValidations.validateVlanRange(vlan)) {
                errorList.add(createBulkError(sectionName + " VLAN", sectionName + " VLAN is invalid", rowCount));
            }

            // GW validation (mandatory if VLAN is filled)
            if ((!vlan.isEmpty() || !gw.isEmpty()) && !TrafficShiftingValidations.isValidIpWithCidr(gw)) {
                errorList.add(createBulkError(sectionName + " GW", sectionName + " GW is invalid", rowCount));
            }

            // IP validation (mandatory if VLAN is filled)
            if ((!vlan.isEmpty() || !ip.isEmpty()) && !TrafficShiftingValidations.isIpV4OrV6(ip)) {
                errorList.add(createBulkError(sectionName + " IP", sectionName + " IP is invalid", rowCount));
            }
            rowCount++;
        }
    }


}
