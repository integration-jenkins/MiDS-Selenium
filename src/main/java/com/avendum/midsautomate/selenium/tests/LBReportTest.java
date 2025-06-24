package com.avendum.midsautomate.selenium.tests;

import com.avendum.midsautomate.controller.MidsTestController;
import com.avendum.midsautomate.model.MidsTest;
import com.avendum.midsautomate.repository.AllMidsTestRepository;
import com.avendum.midsautomate.selenium.TestGenerator.PlanUploadSheetGenerator;
import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import com.avendum.midsautomate.selenium.seleniumcontroller.MWPlanner;
import com.avendum.midsautomate.selenium.utils.BasicTest;
import com.avendum.midsautomate.selenium.utils.PanelTraverser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.time.Duration;
import java.util.Date;
import java.util.logging.Logger;

public class LBReportTest {
    private static final Logger logger = Logger.getLogger(LBReportTest.class.getName());

    private AllMidsTestRepository allMidsTestRepository;
    LBReportTest(AllMidsTestRepository allMidsTestRepository){
        this.allMidsTestRepository = allMidsTestRepository;
    }

    public void successUpload(String userName){
        WebDriver driver= Base.getDriver();
        PlanUploadSheetGenerator planUploadSheetGenerator = new PlanUploadSheetGenerator();
        PanelTraverser panelTraverser = new PanelTraverser();
        long startTime = System.currentTimeMillis();
        planUploadSheetGenerator.generateSampleData();
        panelTraverser.navigateToPlanUpload(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        SearchContext shadow0 = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-combo-box[tabindex='0']"))).getShadowRoot();
        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
        shadow1.findElement(By.cssSelector("input[placeholder='Select Circle']")).sendKeys("JK");
        MidsTest midsTest = new MidsTest();
        midsTest.setTestName("LB Success Report Upload");
        Date date = new Date();
//        BasicTest basicTest=new BasicTest();
        String temp=""+date;
//        String dateString = String.format("%1$td-%1$tm-%1$tY", date);
        midsTest.setTestDate(temp);
        midsTest.setStatus("In Progress");
        midsTest.setTestUser(userName);
        try {

            SearchContext upload = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload"))).getShadowRoot();
            String path = planUploadSheetGenerator.planSuccessSheetPath();
            WebElement file = upload.findElement(By.cssSelector("input[type='file']"));
            file.sendKeys(path);
            Thread.sleep(1000);
            SearchContext s0 = upload.findElement(By.cssSelector("vaadin-upload-file")).getShadowRoot();
            Thread.sleep(1000);
            logger.info("Upload Button Clicked");
            WebElement startButton = s0.findElement(By.cssSelector("div[part='start-button']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", startButton);
            WebDriverWait waitt = new WebDriverWait(driver, Duration.ofSeconds(3));
            waitt.until(ExpectedConditions.elementToBeClickable(startButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startButton);
            midsTest.setComments("Test executed successfully");
            WebElement box=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/div/p[1]")));
            String[] getText = box.getText().split(",");
            String[] errorPart=getText[1].split("=");
            int errorPartInt= Integer.parseInt(errorPart[1].trim());
            if(errorPartInt!=0){
                String dirPath = System.getProperty("file.upload-dir");
                long timeout = 3600;
                long startT = System.currentTimeMillis();
                WebElement errorReport=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/a")));
                errorReport.click();
                while ((System.currentTimeMillis() - startT) < timeout) {
                    File directory = new File(dirPath);
                    File[] files = directory.listFiles((d, name) -> name.endsWith("xlsx"));
                    try {
                        if (files != null) {
                            for (File f : files) {
                                if (f.lastModified() > startTime) {
                                    midsTest.setComments("Error Report is available at: " + f.getAbsolutePath());
                                }
                            }
                        }
                        if (errorReport.isDisplayed()) {
                            midsTest.setComments("Error Report is available at: " + dirPath);
                            break;
                        }
                    } catch (Exception e) {
                        logger.info("Error Report not found yet, retrying...");
                    }
                }

            }
            midsTest.setStatus("Success");
        } catch (Exception e) {
            logger.info("LB Report Success Test Failed: " + e.getMessage());
            midsTest.setComments("Test failed: " + e.getMessage());
            midsTest.setStatus("Failed");
        }

        long endTime = System.currentTimeMillis();
        long timeTakenSec = (endTime - startTime) ;
        midsTest.setTimeTakenSec(String.valueOf(timeTakenSec));
        try {
            allMidsTestRepository.save(midsTest);
        }catch (Exception e){
            logger.info("Error saving MidsTest: " + e.getMessage());
        }
    }

    public void wrongFileUpload(String userName){
        WebDriver driver= Base.getDriver();
        PlanUploadSheetGenerator planUploadSheetGenerator = new PlanUploadSheetGenerator();
        PanelTraverser panelTraverser = new PanelTraverser();
        long startTime = System.currentTimeMillis();
        planUploadSheetGenerator.generateWrongSample();
        panelTraverser.navigateToPlanUpload(driver);
        driver.navigate().refresh();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        SearchContext shadow0 = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-combo-box[tabindex='0']"))).getShadowRoot();
        SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
        shadow1.findElement(By.cssSelector("input[placeholder='Select Circle']")).sendKeys("JK");
        logger.info("Circle Selected");
        MidsTest midsTest = new MidsTest();
        midsTest.setTestName("LB Wrong Report Upload");
        Date date = new Date();
//        BasicTest basicTest=new BasicTest();
        String dateString = String.format("%1$td-%1$tm-%1$tY", date);
        midsTest.setTestDate(dateString);
        midsTest.setStatus("In Progress");
        midsTest.setTestUser(userName);
        try {

            SearchContext upload = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-upload"))).getShadowRoot();
            String path = planUploadSheetGenerator.planFailedSheetPath();
            WebElement file = upload.findElement(By.cssSelector("input[type='file']"));
            file.sendKeys(path);
            Thread.sleep(1000);
            SearchContext s0 = upload.findElement(By.cssSelector("vaadin-upload-file")).getShadowRoot();
            Thread.sleep(1000);
            WebElement startButton = s0.findElement(By.cssSelector("div[part='start-button']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", startButton);
            WebDriverWait waitt = new WebDriverWait(driver, Duration.ofSeconds(30));
            waitt.until(ExpectedConditions.elementToBeClickable(startButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startButton);
            midsTest.setStatus("Success");
            //Row 1
            WebElement bock=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[8]")));
            String[] checkArray = {"Modem Ethernet Site A","Modem Ethernet Site B","Site B Long","Tx Ant Azimut","Tx Antenna","Rx Antenna",
                    "Ber10e6EffMargin Db","Site A Lat","ACM Max QAM","ACM Status","Site A End IP","Site A Long","Site B Lat","Tx Radio","Nominal Quarter","Nominal Aop",
                    "Polarisation","ACM Min QAM","ATPC Status","BER10e6 Rain Interference","BandWidth Mhz","Rx Ant Azimuth", "Site B End IP"};
//            String ignoreArray[]={"Hop Type","For Site ID","Remarks","Modem Code Site A","Chassis Code Site A","Modem Code Site B","MDU Code Site B","Chassis Code Site B"};
//            String auoGenerated[]={"MO Number Site A","Deployment Date/Month","Plan Release Date","Plan status","Ageing","Ageing Range","Free Slot/Port Interface Detail Site A","Free Slot/Port Interface Detail Site B","Node ID Site A","Node ID Site B","Plan Stage"};
            String[] Dependent ={"ATPC Min","ATPC Max","Fiber POP Id","PCM Path","Final Project","MW Chasis","Channel","Tx Frequency","Rx Frequency"};
            String[] doubt ={"Availability","Radio Code(High)","Radio Code(Low)","Hop Dismantle Y / N","Antenna Size Site A","Antenna Size Site B","DCN RA Number","DCN VLAN","GATEWAY IP","Antenna Beam Width","Dismantle Hop ID","Dismantle Hop Modem A","Dismantle Hop Chassis A","Dismantle Hop Modem B","Dismantle Hop Chassis B","New or Existing"};
            String[] mandatory={"Tx Radion","Rx Site Elevation","Tx Site Elevation","Distance","Rx Ant Gain","Tx Ant Gain","Tx Frequency","Rx Frequency","Tx Ant Azimuth","Rx Ant Height","Ber10e6EirpDbm","Ber10e6TxPowerDbm","Ber10e6RxLevelDbm","BandWidth","Channel","Nomenclature Site A","Nomenclature Site B","Tx Ant Height"};
            String[] common={"Nominal Aop"," Nominal Quarter"};
            //"(MODEM(IF Card)/Ethernet Port) Site-A","(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-A",
            // ""(MODEM(IF Card)/Ethernet Port) Site-B","(MW Chasis/CSR (CEN/EPT/NPT/BBU) Site-B",
            String row1=bock.getText();
            StringBuilder comment1=new StringBuilder();
            comment1.append("Row 2: ");
            for (String item : checkArray) {
                if (!row1.contains(item)) {
                    comment1.append(item).append(" |");
                }
            }
            for (String value : doubt) {
                if (!row1.contains(value)) {
                    comment1.append(value).append(" |");
                }
            }
            for (String string : mandatory) {
                if (row1.contains(string)) {
                    comment1.append(string).append(" |");
                }
            }
            for (String string : common) {
                if (row1.contains(string)) {
                    comment1.append(string).append(" |");
                }
            }
            comment1.append("no Filtration Applied Yet ");
            comment1.append("\n");
            ///html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[12]
            WebElement bock2=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[12]")));
            String row2=bock2.getText();
            StringBuilder comment2=new StringBuilder();
            comment2.append("Row 3: ");
            for (String s : checkArray) {
                if (!row2.contains(s)) {
                    comment2.append(s).append(" |");
                }
            }
            for (String s : doubt) {
                if (!row2.contains(s)) {
                    comment2.append(s).append(" |");
                }
            }
            for (String s : mandatory) {
                if (row2.contains(s)) {
                    comment2.append(s).append(" |");
                }
            }
            comment2.append("when left empty, it is not checked");
            comment2.append("\n");
            StringBuilder comment3= new StringBuilder();
            comment3.append("Row 4: ");
            WebElement bock3=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[6]")));
            String row3=bock3.getText();
            for(int i=0;i<Dependent.length;i++){
                if(!row3.contains(Dependent[i])){
                    comment3.append(Dependent[i]).append(" |");
                }
            }
            comment3.append("Dependent fields are not present in the sheet, so not checked.");
            StringBuilder finalComment= new StringBuilder();
            finalComment.append(comment1.toString()+" "+comment2.toString()+" "+comment3.toString());
            logger.info(finalComment.toString());
            midsTest.setComments(finalComment.toString());

        } catch (Exception e) {
            logger.info("LB Report Wrong File Upload Test Failed: " + e.getMessage());
            midsTest.setComments("Test failed: " + e.getMessage());
            midsTest.setStatus("Failed");
        }
        long endTime = System.currentTimeMillis();
        long timeTakenSec = (endTime - startTime) ;
        midsTest.setTimeTakenSec(String.valueOf(timeTakenSec));
        try {
            allMidsTestRepository.save(midsTest);
        }catch (Exception e){
            logger.info("Error saving MidsTest: " + e.getMessage());
        }

    }

    public void lbReportPageFunction(String userName){
        WebDriver driver= Base.getDriver();
        PanelTraverser panelTraverser = new PanelTraverser();
        StringBuilder remarks = new StringBuilder();
        long startTime = System.currentTimeMillis();
        panelTraverser.navigateToPlanUpload(driver);
        driver.navigate().refresh();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        MidsTest midsTest = new MidsTest();
        midsTest.setTestName("LB Page Functionality Check");
        Date date = new Date();
        String dateString = String.format("%1$td-%1$tm-%1$tY", date);
        midsTest.setTestDate(dateString);
        midsTest.setStatus("In Progress");
        midsTest.setTestUser(userName);
        //MW /UBR Tab switching
        try {
            WebElement mwTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[2]")));
            mwTab.click();
            remarks.append("MW Tab found and clicked. ");
        }catch (Exception e){
            logger.info("MW Tab not found: " + e.getMessage());
            remarks.append("MW Tab not found. ");
        }
        remarks.append("\n");



        //Sample Data get uploaded or not
//        try{
//
//        }catch (Exception e){
//            logger.info("Sample Data not uploaded: " + e.getMessage());
//            remarks.append("Sample Data not uploaded. ");
//        }

        //Export Report Working or not
//        try{
//            long startT = System.currentTimeMillis();
//            String dirPath = System.getProperty("file.upload-dir");
//            long timeout = 3600;
//            WebElement errorReport=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/a")));
//            errorReport.click();
//            while ((System.currentTimeMillis() - startT) < timeout) {
//                File directory = new File(dirPath);
//                File[] files = directory.listFiles((d, name) -> name.endsWith("xlsx"));
//                    if (files != null) {
//                        for (File f : files) {
//                            if (f.lastModified() > startTime) {
//                                remarks.append("Error Report is available at: ").append(f.getAbsolutePath()).append("\n");
//                                remarks.append("/n");
//                                break;
//                            }
//                        }
//                    }
//            }
//
//        }catch (Exception e){
//            logger.info("Export Download Report not working: " + e.getMessage());
//            remarks.append("Export Report not working. ");
//        }



        //Modem MW Chasis Page opening or not
        try {
            SearchContext shadow = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-button[role='button']"))).getShadowRoot();
            shadow.findElement(By.cssSelector("#button")).click();
            remarks.append("Modem MW Chasis Page opened successfully. ");
        }catch (Exception e){
            logger.info("Modem MW Chasis Page not found: " + e.getMessage());
            remarks.append("Modem MW Chasis Page not found. ");
        }
        remarks.append("\n");


        midsTest.setStatus("Success");
        midsTest.setComments(remarks.toString());
        long endTime = System.currentTimeMillis();
        long timeTakenSec = (endTime - startTime) ;
        midsTest.setTimeTakenSec(String.valueOf(timeTakenSec));
        try {
            allMidsTestRepository.save(midsTest);
        }catch (Exception e){
            logger.info("Error saving MidsTest: " + e.getMessage());
        }


    }

//    public static void main(String[] args) throws InterruptedException {
//        MWPlanner mw=new MWPlanner();
//        mw.MWPlannerLogin("Z_Bhanu", "adm@123");
//        LBReportTest lbReportTest = new LBReportTest();
//        lbReportTest.wrongFileUpload("Go Pro");
//    }
}
