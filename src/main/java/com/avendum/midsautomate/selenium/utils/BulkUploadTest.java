package com.avendum.midsautomate.selenium.utils;

import com.avendum.midsautomate.enums.DismantleXPath;
import com.avendum.midsautomate.selenium.TestGenerator.UploadTestContainer;
import com.avendum.midsautomate.util.DismantleUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class BulkUploadTest {
    private static final Logger log = LoggerFactory.getLogger(BulkUploadTest.class);

    public static void plannerBulkUpload(WebDriver driver, WebDriverWait wait){
        try{
            String selectCircle = "JK";
            String plannerPath = "C:\\Users\\Kartik Lohate\\Downloads\\dismantlePlannerUpload 4.xlsx";
            String operationPath = "C:\\Users\\Kartik Lohate\\Downloads\\dismantleOperationUpload 1.xlsx";
            String deployPath = "C:\\Users\\Kartik Lohate\\Downloads\\dismantleDeploymentUpload.xlsx";
            String dincPath = "";

            HashMap<String, List<String>> dismantleUser = DismantleUtility.giveAllUsers();
            log.info("Start to test Planner Bulk Upload");

            List<String> operation = dismantleUser.get("Circle Operation Team");
            List<String> planner = dismantleUser.get("Circle MW Planner");
            List<String> deploy = dismantleUser.get("Circle Deployment Team");
            List<String> dincPartner = dismantleUser.get("Circle I&C Partner");

            // planner login
            DismantleUtility.userLogin(wait,planner.get(0),planner.get(1));

            // upload plan by planner
            DismantleUtility.dismantlePlanUpload(wait,driver,selectCircle,plannerPath);

            // test The bulk Upload
            testBulk(wait, planner.get(0),"Circle MW Planner");

            // logout by Planner
            DismantleUtility.logOut(driver);

//            DismantleUtility.userLogin(wait,operation.get(0),operation.get(1));
//
//            // upload plan by operation
//            DismantleUtility.dismantlePlanUpload(wait,driver,selectCircle,operationPath);
//
//            testBulk(wait, operation.get(0),"Circle Operation Team");

            // Deployment bulk upload
//            DismantleUtility.userLogin(wait,deploy.get(0),deploy.get(1));
//
//            // upload plan by operation
//            DismantleUtility.dismantlePlanUpload(wait,driver,selectCircle,operationPath);
//
//            testBulk(wait, deploy.get(0),"Circle Deployment Team");



        }catch (Exception e){
            e.printStackTrace();
        }
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
