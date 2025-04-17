package avendum.com.midsauto;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DPRAutomate {
    //This Will Open the DPR Page for ME
    public static void dprPlan(String planId, int stageCount) throws InterruptedException {
        WebDriver driver = Base.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PanelTraverser panelTraverser = new PanelTraverser();
        panelTraverser.navigateToMWPlanTrackingPage(driver);
//        WebElement srRfai =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-button[1]")));
//        srRfai.click();
        //Now according to the different stages i have to chosse stage
        WebElement stages = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[3]/vaadin-button[" + stageCount + "]")));
        stages.click();
        WebElement shadow_host = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[1072]/flow-component-renderer/div/vaadin-text-field")));
        SearchContext myShd = (SearchContext) shadow_host.getShadowRoot();
        WebElement inputElement = myShd.findElement(By.cssSelector("input"));
        inputElement.sendKeys(planId);
        System.out.println("Plan Id Entered");
        Thread.sleep(5000);
        WebElement shadow_host1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-grid/vaadin-grid-cell-content[2]/flow-component-renderer/vaadin-button")));
        SearchContext myShd1 = (SearchContext) shadow_host1.getShadowRoot();
        WebElement inputElement1 = myShd1.findElement(By.cssSelector("#button"));
        inputElement1.click();
        System.out.println("Search Button Clicked");
    }

    //Tab shifting
    public static void stageM(int stage, WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        String xpath = "/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-tabs/vaadin-tab[" + stage + "]";

        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                WebElement stageM = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", stageM);
                Thread.sleep(1000);
                try {
                    stageM.click();
                } catch (ElementClickInterceptedException e1) {
                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", stageM);
                    } catch (Exception e2) {
                        ((JavascriptExecutor) driver).executeScript(
                                "arguments[0].dispatchEvent(new MouseEvent('click', " +
                                        "{view: window, bubbles: true, cancelable: true}));", stageM);
                    }
                }
                return;

            } catch (Exception e) {
                if (attempts == 2) {
                    System.out.println("Failed to click stage " + stage + " after 3 attempts: " + e.getMessage());
                    throw e;
                }
                Thread.sleep(1000);
            }
        }
    }

    String[] statuses = {
            "SR Pending", "SP Pending", "SO Pending", "RFAI Pending",
            "MO Pending", "Material Delivery Pending",
            "HOP Alignment Pending/Phy-AT Pending", "HOP Alignment Pending/Phy-AT Raised", "HOP Alignment Pending/Phy-AT Rejected", "HOP Alignment Pending/Phy-AT Accept", "I&C Pending",
            "PHY+SOFT AT PENDING", "Phy Raised/Soft Pending", "Phy Reject/Soft Pending", "Phy Accept/Soft Pending",
            "Phy + Soft Raised", "Phy Pending/Soft Raised", "Phy Reject/Soft Raised", "Phy Accept/Soft Raised",
            "Phy + Soft Reject", "Phy Pending/Soft Reject", "Phy Raised/Soft Reject", "Phy Accept/Soft Reject",
            "AT Accepted", "Phy Pending/Soft Accept", "Phy Raised/Soft Accept", "Phy Reject/Soft Accept",
            "TS Completed",
            "Canceled", "Request for Cancellation", "Material Returned",
            "Upgrade Pending", "Upgrade Completed"
    };

    public static int stageIndex(String startStage) {
        int startIndex;
        if (startStage.equals("PHY-AT RAISED/SOFT-AT PENDING")) {
            startIndex = 1;
        } else if (startStage.equals("PHY-AT REJECTED/SOFT-AT RAISED")) {
            startIndex = 2;
        } else if (startStage.equals("PHY-AT RAISED/SOFT-AT RAISED")) {
            startIndex = 3;
        } else if (startStage.equals("AT ACCEPTED")) {
            startIndex = 4;
        } else if (startStage.equals("PHY-AT PENDING/SOFT-AT ACCEPTED")) {
            startIndex = 5;
        } else if (startStage.equals("PHY-AT ACCEPTED/SOFT-AT PENDING")) {
            startIndex = 6;
        } else if (startStage.equals("PHY+SOFT AT PENDING")) {
            startIndex = 7;
        } else if (startStage.equals("PHY-AT ACCEPTED/SOFT-AT REJECTED")) {
            startIndex = 8;
        } else if (startStage.equals("PHY-AT PENDING/SOFT-AT RAISED")) {
            startIndex = 9;
        } else if (startStage.equals("PHY-AT RAISED/SOFT-AT REJECTED")) {
            startIndex = 10;
        } else if (startStage.equals("PHY-AT REJECTED/SOFT-AT REJECTED")) {
            startIndex = 11;
        } else if (startStage.equals("PHY-AT PENDING/SOFT-AT REJECTED")) {
            startIndex = 12;
        } else if (startStage.equals("PHY-AT REJECTED/SOFT-AT ACCEPTED")) {
            startIndex = 13;
        } else if (startStage.equals("PHY-AT ACCEPTED/SOFT-AT RAISED")) {
            startIndex = 14;
        } else if (startStage.equals("PHY-AT REJECTED/SOFT-AT PENDING")) {
            startIndex = 15;
        } else if (startStage.equals("PHY-AT RAISED/SOFT-AT ACCEPTED")) {
            startIndex = 16;
        } else {
            startIndex = -1;
        }
        return startIndex;
    }


//    public static void getPathAT(String startStageP, String startStageS, String endStageP, String endStageS) {
//        //base Case
//        if (startStageS.equals(endStageS) && startStageP.equals(endStageP)) {
//            return;
//        }
//        if (startStageS.equals(endStageS) || !startStageP.equals(endStageP)) {
//            //Mean We have to go to next stage of PHHYi
//            if (startStageP.contains("Pending") || startStageP.contains("Rejected")) {
//                getPathAT("Phy Raised", startStageS, endStageP, endStageS);
//            } else if (startStageP.contains("Raised")) {
//                if (endStageP.contains("Accept")) {
//                    getPathAT("Phy Accept", startStageS, endStageP, endStageS);
//                } else {
//                    getPathAT("Phy Reject", startStageS, endStageP, endStageS);
//                }
//            }
//        } else if (startStageP.equals(endStageP) || !startStageS.equals(endStageS)) {
//            //Means we have to go to next stage of Soft
//            if (startStageS.contains("Pending") || startStageS.contains("Rejected")) {
//                getPathAT(startStageP, "Soft Raised", endStageP, endStageS);
//            } else if (startStageS.contains("Raised")) {
//                if (endStageS.contains("Accept")) {
//                    getPathAT(startStageP, "Soft Accept", endStageP, endStageS);
//                } else {
//                    getPathAT(startStageP, "Soft Reject", endStageP, endStageS);
//                }
//            }
//        }
//    }

    public static int stageNumber(String stage) {

        if (stage.equals("SR Pending")) {
            return 1;
        } else if (stage.equals("SP Pending") || stage.equals("SO Pending") || stage.equals("RFAI Pending")) {
            return 2;
        } else if (stage.equals("MO Pending") || stage.equals("Material Delivery Pending")) {
            return 3;
        } else if (stage.equals("HOP Alignment Pending/Phy-AT Pending") || stage.equals("HOP Alignment Pending/Phy-AT Raised") ||
                stage.equals("HOP Alignment Pending/Phy-AT Rejected") || stage.equals("HOP Alignment Pending/Phy-AT Accept") ||
                stage.equals("I&C Pending")) {
            return 4;
        } else if (stage.equals("PHY+SOFT AT PENDING") || stage.equals("Phy Raised/Soft Pending") || stage.equals("Phy Reject/Soft Pending") ||
                stage.equals("Phy Accept/Soft Pending") || stage.equals("Phy+Soft AT Raised") || stage.equals("Phy Pending/Soft Raised") ||
                stage.equals("Phy Reject/Soft Raised") || stage.equals("Phy Accept/Soft Raised") || stage.equals("Phy+Soft AT Reject") ||
                stage.equals("Phy Pending/Soft Reject") || stage.equals("Phy Raised/Soft Reject") || stage.equals("Phy Accept/Soft Reject") ||
                stage.equals("AT Accepted") || stage.equals("Phy Pending/Soft Accept") || stage.equals("Phy Raised/Soft Accept") ||
                stage.equals("Phy Reject/Soft Accept")) {
            return 5;
        } else if (stage.equals("TS Completed")) {
            return 6;
        } else if (stage.equals("Other")) {
            return 7;
        } else {
            return -1;
        }
    }

    public static void dprStage(String startStage, String endStage, String atStatus,String planId) throws InterruptedException, ParseException {
        WebDriver driver = Base.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        int stageCount = stageNumber(startStage);
        int haltCount = stageNumber(endStage);
        if (stageCount == -1 || haltCount == -1 && (haltCount < stageCount)) {
            return;
        }
//        boolean INC_Half = startStage.equals("Installation and Commissioning Half");
        //TocoVendor A and B filled or not, if not then fill it
        //Case one when toco vendor is not filled for a and b
        //Basic Stage
        if (stageCount == 1) {
            stageM(stageCount++, driver);
//           basicStage(driver,"1003","1004","UBICO","VIDEOCON");
            String[] basicData = TestDataGenerator.generateBasicData();
            basicStage(driver, basicData[0], basicData[1], basicData[2], basicData[3]);
            System.out.println("Basic Stage");
            saveButton(driver);
            if (haltCount == 1) {
                return;
            }
        }
        System.out.println("Basic Detail Filled");
        //SR-RFAI
        if (stageCount == 2) {
            stageM(stageCount++, driver); // Open SR_RFAI stage
//            srRfaiStage(driver,"SRAN","SRBN","4/4/2025","4/4/2025","5/4/2025","5/4/2025","6/4/2025","6/4/2025","6/4/2025","6/4/2025");

            String[] srData;
            int index = 1;
            if (startStage.equals("SP Pending")) {
                String existingSRAD = getDateValue(driver, 3);
                String existingSRBD = getDateValue(driver, 5);
                srData = TestDataGenerator.generateForSPPending(existingSRAD, existingSRBD);
                index = 2;
            } else if (startStage.equals("SO Pending")) {
                //May be use full in future that's why used here
                String existingSRAD = getDateValue(driver, 3);
                String existingSRBD = getDateValue(driver, 5);
                String existingSBAD = getDateValue(driver, 9);
                String existingSBBD = getDateValue(driver, 10);
                srData = TestDataGenerator.generateForSOPending(existingSRAD, existingSRBD, existingSBAD, existingSBBD);
                index = 3;
            } else if (startStage.equals("RFAI Pending")) {
                String existingSRAD = getDateValue(driver, 3);
                String existingSRBD = getDateValue(driver, 5);
                String existingSBAD = getDateValue(driver, 9);
                String existingSBBD = getDateValue(driver, 10);
                String existingSOAD = getDateValue(driver, 12);
                String existingSOBD = getDateValue(driver, 13);
                srData = TestDataGenerator.generateForRFPending(existingSRAD, existingSRBD, existingSBAD, existingSBBD, existingSOAD, existingSOBD);
                index = 4;
            } else {
                srData = TestDataGenerator.generateSRRFaiData();
            }
            int endIndex = -1;
            if (haltCount == 2) {
                //Means it can halt at in this stage at any point
                if (endStage.equals("SR Pending")) {
                    endIndex = 1;
                } else if (endStage.equals("SP Pending")) {
                    endIndex = 2;
                } else if (endStage.equals("SO Pending")) {
                    endIndex = 3;
                } else if (endStage.equals("RFAI Pending")) {
                    endIndex = 4;
                }
            }
            srRfaiStage(driver, srData, index, endIndex);
            System.out.println("SR-RFAI Stage");
            saveButton(driver);
            if (haltCount == 2) {
                return;
            }
        }

        System.out.println("SR-RFAI Stage Filled");

        //Material Order
        if (stageCount == 3) {

//      (WebDriver driver,String MONA,String MONB,String MODA,String MODB,String MTA,String MTB, String MDDA,String MDDB,String DDA,String DDB,String MDS) throws InterruptedException {
//                materialStage(driver,"1146","1147","6/4/2025","6/4/2025","Fresh","Fresh","7/4/2025","7/4/2025","8/4/2025","8/4/2025","Fully Delivered");

//            materialStage(driver, materialData[0], materialData[1], materialData[2],
//                    materialData[3], materialData[4], materialData[5], materialData[6],
//                    materialData[7], materialData[8], materialData[9], materialData[10]);
            stageM(stageCount++, driver);
            Thread.sleep(1000);
            String[] materialData;
            int startIndex = 1;
            if (startStage.equals("Material Delivery Pending")) {
                DPRDataGetters dprDataGetters = new DPRDataGetters();
                String existingMONA = dprDataGetters.getMaterialSiteMO(2);
                String existingMONB = dprDataGetters.getMaterialSiteMO(5);
                String existingMODA = dprDataGetters.getMaterialTypeMo(4);
                String existingMODB = dprDataGetters.getMaterialTypeMo(7);
                String existingMTA = dprDataGetters.getMaterialMoDate(3);
                String existingMTB = dprDataGetters.getMaterialMoDate(6);
                startIndex = 2;
                materialData = TestDataGenerator.genMDMDPending(existingMODA, existingMODB);
            } else {
                materialData = TestDataGenerator.genMDMOPending();
            }
            int endIndex = -1;
            if (haltCount == 3) {
                if (endStage.equals("MO Pending")) {
                    endIndex = 1;
                } else if (endStage.equals("Material Delivery Pending")) {
                    endIndex = 2;
                }
            }
            materialStage(materialData, startIndex, endIndex);
            saveButton(driver);
            if (haltCount == 3) {
                return;
            }
        }
        System.out.println("Material Order Stage Filled");

        //Installation and Commision
        if (stageCount == 4) {
            stageM(stageCount++, driver);
//            inandCommStage(driver,"8/4/2025","8/4/2025",INC_Half,"8/4/2025","8/4/2025");
            String[] incData = TestDataGenerator.generateINCData();
            int startIndex = 1, endIndex = -1;
            if (startStage.equals("HOP Alignment Pending/Phy-AT Pending")) {
                startIndex = 2;
            } else if (startStage.equals("HOP Alignment Pending/Phy-AT Raised")) {
                startIndex = 3;
            } else if (startStage.equals("HOP Alignment Pending/Phy-AT Rejected")) {
                startIndex = 4;
            } else if (startStage.equals("HOP Alignment Pending/Phy-AT Accept")) {
                startIndex = 5;
            }
            //For End stage
            if (haltCount == 4) {
                stageCount = -1;
                if (endStage.equals("I&C Pending")) {
                    endIndex = 1;
                } else if (endStage.equals("HOP Alignment Pending/Phy-AT Pending")) {
                    endIndex = 2;
                } else if (endStage.equals("HOP Alignment Pending/Phy-AT Raised")) {
                    endIndex = 3;
                }
                else if (endStage.equals("HOP Alignment Pending/Phy-AT Rejected")) {
                    endIndex = 4;
                } else if (endStage.equals("HOP Alignment Pending/Phy-AT Accept")) {
                    endIndex = 5;
                }
            }else{
                endIndex=-2;//In INS There no linear sequence if there no endStage in the INC then here we only have to do INS complete then move to AT
            }
            saveButton(driver);
            iandCStage(incData, startIndex, endIndex,planId); Thread.sleep(4000);
        }
        System.out.println("Installation and Commisioning Stage Filled");


        //Acceptance Test
        if (stageCount == 5) {
            //NMS Date filling of INC Stage
            //Means it Not coming from Hop Alignment Condition
            if(!startStage.contains("Soft") ||startStage.contains("AT Accepted")) {
                String srData[] = TestDataGenerator.generateINCData();
                //RFAI Missing Data to be filled
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal = Calendar.getInstance();  cal.add(Calendar.DAY_OF_MONTH, -1);
                //Text Filled
                stageM(2, driver);  fillDateField(driver, 21,sdf.format(cal.getTime())); fillDateField(driver, 33,sdf.format(cal.getTime())+"");
//                fillTextField(driver,18,"SacfaSiteA");
//                fillTextField(driver,19,"AppicationSiteA");
//                fillTextField(driver,23,"WPCA");
//                fillTextField(driver,30,"SacfaSiteB");
//                fillTextField(driver,31,"AppicationSiteB");
//                fillTextField(driver,35,"WPCB");
//                fillDateField(driver, 16,sdf.format(cal.getTime())+"");
//                fillDateField(driver, 17,sdf.format(cal.getTime())+"");
//                fillDateField(driver, 20,sdf.format(cal.getTime())+"");
//                fillDateField(driver, 28,sdf.format(cal.getTime())+"");
//                fillDateField(driver, 29,sdf.format(cal.getTime())+"");
//                fillDateField(driver, 32,sdf.format(cal.getTime())+"");
//                fillDateField(driver, 34,sdf.format(cal.getTime())+"");
                stageM(4, driver);
                iandcChoose(7, srData[2]);//PN
                iandcDateFill(8, srData[3]);//Align Date
                iandcDateFill(11, srData[4]);//NMS Date
                iandcChoose(12, srData[5]);//NMS Status
                saveButton(driver);
                Thread.sleep(2000);
                //After this the AT at Phy+SoftPending
            }
            stageM(stageCount++, driver);
            String startSplit[] = splitStageAT(startStage);
            String startStageP = startSplit[0];
            String startStageS = startSplit[1];
            String endSplit[] = splitStageAT(endStage);
            String endStageP = endSplit[0];
            String endStageS = endSplit[1];

            List<String> pathL= getPathAT(startStageP, endStageP,new ArrayList<>());
            List<String> pathS= getPathAT(startStageS, endStageS,new ArrayList<>());
            System.out.println("Path of Phy: "+pathL);
            System.out.println("Path of Soft: "+pathS);

            //Now According to the path i have to click the buttons
            //For Path of Phy
            String msPartnerName="MS Partner";
            for (String path: pathL){
                if(path.equals("Raised")){
                    manualAT(driver,"Phy");
                    msPartnerName= raisedMSPartner();
                }else if(path.equals("Accept")){
                    //MS Partner Ka logic
                    //Muko Waspe login karke
                    MSPartCall( planId,"Accept","Kuch Toh Log kahenge","Phy",msPartnerName,"5");
                }else if(path.equals("Reject")){
                    //MS Partner Ka logic
                    MSPartCall( planId,"Reject","Kuch Toh Log kahenge","Phy",msPartnerName,"5");
                }
            }
            //Here i need to trigger something to get of out dialog box trap
            WebElement mediaPlanningIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mediaPlanningIcon);//So that we can avoid it
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", mediaPlanningIcon);//Close it so that i want effect other programs




            //For Path of Soft
            for (String path: pathS){
                if(path.equals("Raised")){
                    manualAT(driver,"Soft");
                }else if(path.equals("Accept")){
                    //MS Partner Ka logic
                    MSPartCall( planId,"Accept","Kuch Toh Log kahenge","Soft",msPartnerName,"5");
                }else if(path.equals("Reject")){
                    //MS Partner Ka logic
                    MSPartCall( planId,"Reject","Kuch Toh Log kahenge","Soft",msPartnerName,"5");
                }
            }
            System.out.println("Acceptance Test Stage Filled");

            /*

            //now i will write here my code here
            public static List<String> getPathAT(String startStage,String endStage,List<String> path){
                    if(startStage.equals(endStage)){
                        return path;
                    }
                    if(startStage.equals("Pending")|| (startStage.equals("Reject")){
                            path.add("Raised");
                            return getPathAT("Raised",endStage,path);
                     }else if(startStage.equals("Raised")){
                        if(endStage.contains("Reject")){
                            return getPathAT("Reject",endStage,path);
                        }else{
                            return getPathAT("Accept",endStage,path);
                        }
                     }
            }


             */


//            if(atStatus.equals("ManualSoft")){
//                manualAT(driver, "Phy");
//            }else if(atStatus.equals("ManualPhy")) {
//                manualAT(driver, "Phy");
//            }else if(atStatus.equals("ManualBoth")){
//                manualAT(driver, "Phy");
//                manualAT(driver, "Soft");
//            }else if(atStatus.equals("BulkPhy")) {
//                bulkAtRaise(driver, "Phy");
//            }else if(atStatus.equals("BulkSoft")) {
//                bulkAtRaise(driver, "Soft");
//            }else{
//                bulkAtRaise(driver, "Phy");
//                bulkAtRaise(driver, "Soft");
//            }
            int startIndex = -1, endIndex;
            startIndex = stageIndex(startStage);
            if (haltCount == 5) {
                stageCount = -1;
                endIndex = stageIndex(endStage);
            }


//            saveButton(driver);
//            if (atStatus.equalsIgnoreCase("Manual")) {
////                {"PHY-AT RAISED/SOFT-AT PENDING", "PHY-AT REJECTED/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT RAISED", "AT ACCEPTED", "PHY-AT PENDING/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT PENDING", "PHY+SOFT AT PENDING", "PHY-AT ACCEPTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT RAISED", "PHY-AT RAISED/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT REJECTED", "PHY-AT PENDING/SOFT-AT REJECTED", "PHY-AT REJECTED/SOFT-AT ACCEPTED", "PHY-AT ACCEPTED/SOFT-AT RAISED", "PHY-AT REJECTED/SOFT-AT PENDING", "PHY-AT RAISED/SOFT-AT ACCEPTED"}
//
//            } else {
//
//            }
//            if (haltCount == 5) {
//                return;
//            }
        }


        //Traffic Stage
        if (stageCount == 6) {
            stageM(stageCount++, driver);

            System.out.println("Traffic Stage");


            saveButton(driver);
            if (haltCount == 6) {
                return;
            }
        }
    }
    private static void TrafficDataFill() throws InterruptedException {
        WebDriver driver=Base.driver;
        WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(30));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        SearchContext trafficText = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-combo-box[class='planEditText']"))).getShadowRoot();
        Thread.sleep(1000);
        SearchContext trafficText1 = trafficText.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(500);
        trafficText1.findElement(By.cssSelector("input[role='combobox']")).sendKeys("Yes");
        Thread.sleep(1000);
        SearchContext dateT = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("vaadin-date-picker[class='planEditText']"))).getShadowRoot();
        Thread.sleep(1000);
        SearchContext dateT1 = dateT.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(1000);
        dateT1.findElement(By.cssSelector("input[part='value']")).sendKeys(sdf.format(cal.getTime()));
//        fillDateField(driver, 3, sdf.format(cal.getTime()));

    }

    public static String[] splitStageAT(String stage) {
        String[] split;
        if (stage.contains("/")) {
            split = stage.split("/");
            split[0] = (split[0].contains("Raised")) ? "Raised" : (split[0].contains("Pending")) ? "Pending" : "Accepted";
            split[1] = (split[1].contains("Raised")) ? "Raised" : (split[1].contains("Pending")) ? "Pending" : "Accepted";
        } else if (stage.equalsIgnoreCase("AT Accepted") || stage.equalsIgnoreCase("TS Completed") ) {
            split = new String[2];
            split[0] = "AT ACCEPTED";
            split[1] = "AT ACCEPTED";
        } else {
            split = new String[2];
            split[0] = "Pending";
            split[1] = "Pending";
        }
        return  split;
    }
    public static List<String> getPathAT(String startStage, String endStage, List<String> path){
        if(startStage.equals(endStage)){
            return path;
        }
        if(startStage.equals("Pending")|| (startStage.equals("Reject"))){
            path.add("Raised");
            getPathAT("Raised",endStage,path);
        }else if(startStage.equals("Raised")){
            if(endStage.contains("Reject")){
                path.add("Reject");
                getPathAT("Reject",endStage,path);
            }else{
                path.add("Accept");
                getPathAT("Accept",endStage,path);
            }
        }
        return path;
    }



    public static void manualAT(WebDriver driver,String service) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        if(service.equals("Phy")){
            WebElement phy=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-radio-group/vaadin-radio-button[1]/span")));
            phy.click();
            System.out.println("Phy clicked");
        }else if(service.equals("Soft")){
            WebElement soft=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-radio-group/vaadin-radio-button[2]")));
            soft.click();
            System.out.println("Soft clicked");
        }
        Thread.sleep(1000);
        SearchContext shadow = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-button:nth-child(2)")).getShadowRoot();
        Thread.sleep(1000);
        shadow.findElement(By.cssSelector("#button")).click();

    }
        public static void basicStage(WebDriver driver,String tocoA,String tocoB,String tocoAVendor,String tocoBVendor) throws InterruptedException {
            //For Geting the tocoA filled Value A Vendoer
            SearchContext shadow0 = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(16)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext shadow1 = shadow0.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            WebElement inputt = shadow1.findElement(By.cssSelector("input[role='combobox']"));
            String toA=inputt.getAttribute("value");

            SearchContext s1 = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(19)")).getShadowRoot();
            Thread.sleep(500);
            SearchContext s2 = s1.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(500);
            WebElement input = s2.findElement(By.cssSelector("input[role='combobox']"));
            String toB=input.getAttribute("value");
            System.out.println("TOCO A: "+toA +" TOCO B: "+toB);


        if((toA.length()<=1)) {
            Actions actions = new Actions(driver);
            actions.click(inputt) // Focus on the input field
                    .keyDown(Keys.CONTROL)
                    .sendKeys("a") // Select all text
                    .keyUp(Keys.CONTROL)
                    .pause(Duration.ofMillis(500)) // Pause for 500ms
                    .sendKeys(Keys.DELETE) // Delete the selected text
                    .pause(Duration.ofMillis(500)) // Pause for 500ms
                    .sendKeys(tocoAVendor) // Enter the new value
                    .build()
                    .perform();
//            inputt.sendKeys(Keys.chord(Keys.CONTROL, "a"));
//            Thread.sleep(500);
//            inputt.sendKeys(Keys.DELETE);
//            Thread.sleep(500);
//            inputt.sendKeys(tocoAVendor);
//            Thread.sleep(500);
            SearchContext tocoVenderAID = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child(17)")).getShadowRoot();
            Thread.sleep(1000);
            tocoVenderAID.findElement(By.cssSelector("input[part='value']")).sendKeys(tocoA);
        }
        if((toB.length()<=1)) {
            Actions actions = new Actions(driver);
            actions.click(input)
                    .keyDown(Keys.CONTROL)
                    .sendKeys("a")
                    .keyUp(Keys.CONTROL)
                    .pause(Duration.ofMillis(100))
                    .sendKeys(Keys.DELETE)
                    .pause(Duration.ofMillis(100))
                    .sendKeys(tocoBVendor)
                    .build()
                    .perform();
//            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
//            Thread.sleep(500);
//            input.sendKeys(Keys.BACK_SPACE);
//            Thread.sleep(500);
//            input.sendKeys(tocoBVendor);
//            Thread.sleep(500);
            SearchContext tocoVenderBID = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child(20)")).getShadowRoot();
            Thread.sleep(200);
            tocoVenderBID.findElement(By.cssSelector("input[part='value']")).sendKeys(tocoB);
        }
    }

    public static void bulkAtRaise(WebDriver driver,String service) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        if(service.equals("Phy")){
            WebElement phy=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-radio-group/vaadin-radio-button[1]/span")));
            phy.click();
            System.out.println("Phy clicked");
            SearchContext phA = driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[2]/vaadin-horizontal-layout[1]/vaadin-button[1]")).getShadowRoot();
            Thread.sleep(1000);
            WebElement phA1=phA.findElement(By.cssSelector("button"));
            phA1.click();

        }else if(service.equals("Soft")){
            WebElement soft=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[1]/vaadin-radio-group/vaadin-radio-button[2]")));
            soft.click();
            System.out.println("Soft clicked");
            SearchContext sft = driver.findElement(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-form-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-vertical-layout[2]/vaadin-horizontal-layout[2]/vaadin-button[1]")).getShadowRoot();
            Thread.sleep(1000);
            WebElement sft1=sft.findElement(By.cssSelector("button"));
            sft1.click();
        }

        Thread.sleep(1000);
        SearchContext s0 = driver.findElement(By.xpath("/html/body/vaadin-dialog-overlay/flow-component-renderer/div/vaadin-vertical-layout/vaadin-upload")).getShadowRoot();
        String path = "C:\\Users\\Bhanu\\Desktop\\My Documents\\Bulk and Manual AT test\\bulky.xlsx";
        WebElement file = s0.findElement(By.cssSelector("input[type='file']"));
        file.sendKeys(path);
        System.out.println("File Uploaded");

        Thread.sleep(1000);
        SearchContext shadow = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-vertical-layout:nth-child(2) > vaadin-horizontal-layout:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-button:nth-child(2)")).getShadowRoot();
        Thread.sleep(1000);
        shadow.findElement(By.cssSelector("#button")).click();
    }



    //I and C Stage Data Filling
    public static void iandcDateFill(int index,String value) throws InterruptedException {
        WebDriver driver=Base.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        SearchContext idA = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child("+index+")"))).getShadowRoot();
        Thread.sleep(500);
        SearchContext idA1 = idA.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(500);
        idA1.findElement(By.cssSelector("input[part='value']")).sendKeys(value);
        idA1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);

    }

    public static void iandcChoose(int index, String value) throws InterruptedException {
        WebDriver driver = Base.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        SearchContext incP = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(" + index + ")")
        )).getShadowRoot();
        Thread.sleep(500);
        SearchContext incP1 = incP.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(500);
        WebElement inputElement = incP1.findElement(By.cssSelector("input[role='combobox']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputElement);
        wait.until(ExpectedConditions.elementToBeClickable(inputElement));
        try {
            inputElement.sendKeys(value);
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", inputElement, value);
        }
    }

    public static void iandCStage(String srData[],int startIndex,int endIndex,String planid) throws InterruptedException {
        WebDriver driver=Base.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        //start index talk where we have to start to from here endIndex talks we have to stop here or not
        if(startIndex==1 && endIndex!=1) {
            iandcDateFill(2, srData[0]);//SitA
            iandcDateFill(3, srData[1]);//SiteB
            saveButton(driver);
            startIndex++;
        }
//        if(startIndex==2 && endIndex!=2) {
//            iandcChoose(7,srData[2]);//PN
//            iandcDateFill(5, srData[4]);//NMS Date
//            iandcDateFill(8, srData[3]);//Align Date
//            iandcChoose(12,srData[5]);//NMS Status
//        }

        String MS_Partner_Name="MS Partner";
        if(endIndex!=-2) { //When we don'nt to stop in INC Stage therefore we can move to next stage
            //Hop Alignment Pending Physical at Raise
            if (startIndex == 2 && endIndex != 2) {
                //Tab Shifting to AT
                stageM(5, driver);//AT tab
                //Raseing Phy AT
                manualAT(driver, "Phy");//It Raised
                MS_Partner_Name = raisedMSPartner();
                if (endIndex == 4) {
                    startIndex+=2;
                } else if (endIndex == 5) {
                    startIndex = startIndex + 3;
                }
            }
            if (startIndex == 4 && endIndex ==4 ) {
                //Now Phy is raised now we can reject or accept
                MSPartCall(planid, "Reject", "Reject It", "Phy",MS_Partner_Name,"4");
            }
            if (startIndex == 5 && endIndex == 5) {
                //Now Phy is raised now we can reject or accept
                MSPartCall(planid, "Accept", "Accept It", "Phy",MS_Partner_Name,"4");
            }
        }
    }
    private static String raisedMSPartner() throws InterruptedException {
        WebDriver driver=Base.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"overlay\"]/flow-component-renderer/div/vaadin-vertical-layout/p")));
        String text = element.getText();
        System.out.println("Text: " + text);
        //SOFT raised for acceptance to Ceragon-Bhanu-JK_MS_PT_1
        String[] parts = text.split("-");
//        Thread.sleep(100);
//        WebElement close = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[2]/div/vaadin-vertical-layout/div/vaadin-vertical-layout/div[3]/iron-collapse-layout/div/a/span")));
//
//        driver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-button:nth-child(3)"));
//        Thread.sleep(1000);
//        close.findElement(By.cssSelector("#button")).click();
//        Thread.sleep(1000);
//        SearchContext close = driver.findElement(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-vertical-layout:nth-child(1) > vaadin-button:nth-child(3)")).getShadowRoot();
//        Thread.sleep(1000);
//        WebElement dialogOverlay = wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.cssSelector("body > vaadin-dialog-overlay:nth-child(7)")
//        ));
//        SearchContext shadowRoot = dialogOverlay.getShadowRoot();
//        WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
//                shadowRoot.findElement(By.cssSelector("flow-component-renderer > div > vaadin-vertical-layout > vaadin-button:nth-child(3)"))
//        ));
//        closeButton.click();
//        close.findElement(By.cssSelector("#button")).click();
        if(parts[2]==null){
            if(parts[1]==null){
                return parts[0];
            }
           return parts[1];
        }
        return parts[2];
    }

    private static void MSPartCall(String planid,String task,String detailcomment,String status,String msPartnerName,String stageTab) throws InterruptedException {
        WebDriver driver1=Base.getNewDriver();
        Login loginPage = new Login();
        loginPage.Login(driver1, msPartnerName,"adm@123");
        PanelTraverser panelTraverser = new PanelTraverser();
        panelTraverser.navigateToMWPlanTrackingPage(driver1);
        Manual_AT_Acpt_Rjt.accept_Reject_MSPartner(driver1, planid,"Labelling Issue",detailcomment,  status,task,stageTab);
        saveButton(driver1);
    }

 /*
    public static  void inandCommStage(WebDriver driver,String IDA,String IDB,Boolean INCHalf,String INCP,String NMSD ) throws InterruptedException {
        //For Installation Completed
        //For Site A Installation Date
        Thread.sleep(1000);
        SearchContext idA = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(2)")).getShadowRoot();
        Thread.sleep(1000);
        SearchContext idA1 = idA.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(1000);
        idA1.findElement(By.cssSelector("input[part='value']")).sendKeys(IDA);
        idA1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);

        //For Site B Installation Date
        Thread.sleep(1000);
        SearchContext idB = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(3)")).getShadowRoot();
        Thread.sleep(1000);
        SearchContext idB1 = idB.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(1000);
        idB1.findElement(By.cssSelector("input[part='value']")).sendKeys(IDB);
        idB1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);

        //Hop Installation Completed
//        Thread.sleep(1000);
//        SearchContext hic = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(4)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext hic1 = hic.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        hic1.findElement(By.cssSelector("input[role='combobox']")).sendKeys(HIC);

        //HOP Installation Date
//        Thread.sleep(1000);
//        SearchContext hid = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(5)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext hid1 = hid.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        hid1.findElement(By.cssSelector("input[part='value']")).sendKeys(HID);
//        hid1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);


        //Hop Deployed
        //Trigger this only when the INCHalf is false
        if(INCHalf){
            //I&C Partner Name
            Thread.sleep(1000);
            SearchContext incP = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(7)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext incP1 = incP.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            incP.findElement(By.cssSelector("input[role='combobox']")).sendKeys(INCP);

            //Alignment / I&C Date
            //Automatically Filled

            //NMS Visibility Date
            SearchContext nmsD = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(11)")).getShadowRoot();
            Thread.sleep(1000);
            SearchContext nmsD1 = nmsD.findElement(By.cssSelector("#input")).getShadowRoot();
            Thread.sleep(1000);
            nmsD1.findElement(By.cssSelector("input[part='value']")).sendKeys(NMSD);
        }

    }
*/

    // Modified SR-RFAI
    public static void srRfaiStage(WebDriver driver, String[] srData,int index,int endIndex) throws InterruptedException, ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date today = new Date();

//        try {
//            Date[] dates = new Date[8];
            String[] dateStrs = {srData[2], srData[3], srData[4], srData[5], srData[6], srData[7], srData[8], srData[9]};

//            for (int i = 0; i < dates.length; i++) {
//                dates[i] = sdf.parse(dateStrs[i]);
//                if (dates[i].after(today)) {
//                    throw new IllegalArgumentException("Date cannot be in future: " + dateStrs[i]);
//                }
//            }
//
//            if (!(dates[0].before(dates[2]) && dates[2].before(dates[4]) &&
//                    dates[1].before(dates[3]) && dates[3].before(dates[5]))) {
//                throw new IllegalArgumentException("Invalid date sequence");
//            }
//
//            fillFormFields(driver, srData,index);
//
//        } catch (ParseException | IllegalArgumentException e) {
//            System.out.println("Invalid date input: " + e.getMessage());
//            throw e;
//        }
        fillFormFields(driver, srData,index,endIndex);
    }
    private static void fillFormFields(WebDriver driver, String[] srData,int startIndex,int endIndex) throws InterruptedException {

        if(startIndex==1 && endIndex!=1) {
            fillTextField(driver, 2, srData[0]); // SRAN
            fillTextField(driver, 4, srData[1]); // SRBN
            fillDateField(driver, 3, srData[2]); // SRAD
            fillDateField(driver, 5, srData[3]); // SRBD
            startIndex++;
        }
        if(startIndex==2 && endIndex!=2) {
            fillDateField(driver, 9, srData[4]); // SBAD
            fillDateField(driver, 10, srData[5]); // SBBD
            startIndex++;
        }
        if(startIndex==3 && endIndex!=3) {
            fillDateField(driver, 12, srData[6]); // SOAN
            fillDateField(driver, 13, srData[7]); // SOBN
            startIndex++;
        }

        if(startIndex==4 && endIndex!=4) {
            //There Occur Special Senerio Where we see ERROR:#N/A int the container that need to be handle it disturb work flow drstically we have to remove
            clearTextField(driver, 18);
            clearTextField(driver, 31);
            clearTextField(driver, 23);
            clearTextField(driver, 19);
            clearTextField(driver, 30);
            clearTextField(driver, 35);


            fillDateField(driver, 15, srData[8]); // RODA
            fillDateField(driver, 27, srData[9]); // RODB
        }

    }
    private static void fillTextField(WebDriver driver, int fieldIndex, String value) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        SearchContext srA = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child("+fieldIndex+")"))).getShadowRoot();
        srA.findElement(By.cssSelector("input[part='value']")).sendKeys(value);
    }


    private static void clearTextField(WebDriver driver, int fieldIndex) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        SearchContext srA = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child("+fieldIndex+")"))).getShadowRoot();
        WebElement field = srA.findElement(By.cssSelector("input[part='value']"));
        Actions actions = new Actions(driver);
        actions.click(field)
                .keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .build()
                .perform();
//        field.clear();
    }

    private static void fillDateField(WebDriver driver, int pickerIndex, String value) throws InterruptedException {
//        Thread.sleep(1000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        SearchContext picker = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child("+pickerIndex+")"))).getShadowRoot();
        SearchContext input = picker.findElement(By.cssSelector("#input")).getShadowRoot();
        WebElement field = input.findElement(By.cssSelector("input[part='value']"));
        field.sendKeys(value);
        field.sendKeys(Keys.ENTER);
    }


//    public  static  void srRfaiStage(WebDriver driver,String SRAN,String SRBN,String SRAD,String SRBD,String SBAD,String SBBD,String SOAD, String SOBD,String RODB,String RODA) throws InterruptedException {
//        //There is chance that few among they are already field if they do so first we check we have to change or not thencheck if we have is the container should be disable
//
//        //Before filling check we have the above date with in contration or not  contraint are like all date should be less then or equal to  today date and it format DD/MM/YYYY
//        //And SRAD <= SBAD <= SOAD
//        //And SRBD <= SBBD <= SOBD
//        //Now check dates with year and month
//        // Date validation
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date today = new Date();
//
//        try {
//            Date srAdDate = sdf.parse(SRAD);
//            Date srBdDate = sdf.parse(SRBD);
//            Date sbAdDate = sdf.parse(SBAD);
//            Date sbBdDate = sdf.parse(SBBD);
//            Date soAdDate = sdf.parse(SOAD);
//            Date soBdDate = sdf.parse(SOBD);
//
//            if (srAdDate.after(today) || srBdDate.after(today) || sbAdDate.after(today) || sbBdDate.after(today) || soAdDate.after(today) || soBdDate.after(today)) {
//                throw new IllegalArgumentException("Dates must be less than or equal to today's date.");
//            }
//
//            if (!(srAdDate.compareTo(sbAdDate) <= 0 && sbAdDate.compareTo(soAdDate) <= 0 && srBdDate.compareTo(sbBdDate) <= 0 && sbBdDate.compareTo(soBdDate) <= 0)) {
//                throw new IllegalArgumentException("Date constraints not satisfied.");
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//
//        //For SR
//        Thread.sleep(1000);
//        SearchContext srA = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child(2)")).getShadowRoot();
//        Thread.sleep(1000);
//        srA.findElement(By.cssSelector("input[part='value']")).sendKeys(SRAN);
//
//        SearchContext srB = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child(4)")).getShadowRoot();
//        Thread.sleep(1000);
//        WebElement srB1=srB.findElement(By.cssSelector("input[part='value']"));
//        srB1.sendKeys(SRBN);
//
//
//        Thread.sleep(1000);
//        SearchContext srAD = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(3)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext srAD1 = srAD.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        srAD1.findElement(By.cssSelector("input[part='value']")).sendKeys(SRAD);
//        srAD1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        Thread.sleep(1000);
//        SearchContext srBD = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(5)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext srBD1 = srBD.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        srBD1.findElement(By.cssSelector("input[part='value']")).sendKeys(SRBD);
//        srBD1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //For SP
//        Thread.sleep(1000);
//        SearchContext spA = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(9)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext spA1 = spA.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        spA1.findElement(By.cssSelector("input[part='value']")).sendKeys(SBAD);
//        spA1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//        SearchContext spB = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(10)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext spB1 = spB.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        spB1.findElement(By.cssSelector("input[part='value']")).sendKeys(SBBD);
//        spB1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //FOr SO
//        Thread.sleep(1000);
//        SearchContext soAD = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(12)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext soAD1 = soAD.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);soAD1.findElement(By.cssSelector("input[part='value']")).sendKeys(SOAD);
//        soAD1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        Thread.sleep(1000);
//        SearchContext soBD = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(13)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext soBD1 = soBD.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        soBD1.findElement(By.cssSelector("input[part='value']")).sendKeys(SOBD);
//        soBD1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //RFAI Entry To Movve to next stage Material one
//       //RFAI offered date site a
//        Thread.sleep(1000);
//        SearchContext roDA = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(15)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext roDA1 = roDA.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        roDA1.findElement(By.cssSelector("input[part='value']")).sendKeys(RODA);
//        roDA1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //RFAI offered date site B
//        Thread.sleep(1000);
//        SearchContext roDB = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(27)")).getShadowRoot();
//        Thread.sleep(1000);
//        SearchContext roDB1 = roDB.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        roDB1.findElement(By.cssSelector("input[part='value']")).sendKeys(RODB);
//        roDB1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//    }
    private static String getDateValue(WebDriver driver, int pickerIndex) throws InterruptedException {
        Thread.sleep(500);
        String baseSelector = "body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(%d)";
        SearchContext picker = driver.findElement(By.cssSelector(String.format(baseSelector, pickerIndex))).getShadowRoot();
        SearchContext input = picker.findElement(By.cssSelector("#input")).getShadowRoot();
        return input.findElement(By.cssSelector("input[part='value']")).getAttribute("value");
    }

    private static void materialDateFill( int pickerIndex, String value) throws InterruptedException {
        WebDriver driver = Base.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        SearchContext picker = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child("+pickerIndex+")"))).getShadowRoot();
        Thread.sleep(100);
        SearchContext input = picker.findElement(By.cssSelector("#input")).getShadowRoot();
        WebElement field = input.findElement(By.cssSelector("input[part='value']"));
        field.sendKeys(value);
        field.sendKeys(Keys.ENTER);
    }
//    private static void materialFieldFill( int fieldIndex, String value) throws InterruptedException {
//        WebDriver driver = Base.getDriver();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//        Thread.sleep(100);
//        SearchContext field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child("+fieldIndex+")"))).getShadowRoot();
//        field.findElement(By.cssSelector("input[part='value']")).sendKeys(value);
//    }

    private static void materialTypeFieldFill(int index,String value) throws InterruptedException {
        WebDriver driver = Base.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        SearchContext mtA= driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child("+index+")")).getShadowRoot();
        SearchContext mtA1 = mtA.findElement(By.cssSelector("#input")).getShadowRoot();
        Thread.sleep(100);
        mtA1.findElement(By.cssSelector("input[role='combobox']")).sendKeys(value);
    }

    public static  void materialStage(String [] strData,int startIndex,int endIndex) throws InterruptedException {
        if(startIndex==1 && endIndex!=1) {
            Thread.sleep(3000);//Lets Wait for some time before entering
            fillTextField(Base.driver,2, strData[0]); // MONA ->Some time left Empty
            fillTextField(Base.driver,5, strData[1]);//MONB
            fillTextField(Base.driver,2, strData[0]);//Fill ones again
            materialDateFill(4, strData[2]); // MODA
            materialDateFill(7, strData[3]); // MODB
            materialTypeFieldFill(3, strData[4]); // MTA
            materialTypeFieldFill(6, strData[5]); // MTB
            startIndex++;
        }
        if(startIndex==2 && endIndex!=2) {
            materialDateFill(11, strData[6]); // MDDA
            materialDateFill(12, strData[7]); // MDDB
            materialDateFill(13, strData[8]); // DDA
            materialDateFill(14, strData[9]); // DDB
            materialTypeFieldFill(15, strData[10]); // MDS
            startIndex++;
        }
    }

//    public static void materialStage(WebDriver driver,String MONA,String MONB,String MODA,String MODB,String MTA,String MTB, String MDDA,String MDDB,String DDA,String DDB,String MDS) throws InterruptedException {
//        //For MAterial Order
//        //MO Number Site A
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//        SearchContext moNA = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child(2)")).getShadowRoot();
//        moNA.findElement(By.cssSelector("input[part='value']")).sendKeys(MONA);
//
//        //Material Type Site A
//        SearchContext mtA= wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(3)")).getShadowRoot();
//        SearchContext mtA1 = mtA.findElement(By.cssSelector("#input")).getShadowRoot();
//        Thread.sleep(1000);
//        mtA1.findElement(By.cssSelector("input[role='combobox']")).sendKeys(MTA);
//
//        //MO Date Site A
//        SearchContext moDA = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(4)")).getShadowRoot();
//        SearchContext moDA1 = moDA.findElement(By.cssSelector("#input")).getShadowRoot();
//        moDA1.findElement(By.cssSelector("input[part='value']")).sendKeys(MODA);
//        moDA1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
////        materialDateFill( 4, MODA);
//
//        //MO Number Site B
//        SearchContext moNB = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-text-field:nth-child(5)")).getShadowRoot();
//        moNB.findElement(By.cssSelector("input[part='value']")).sendKeys(MONB);
//
//        //Materail Type Site B
//        SearchContext mtB = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(6)")).getShadowRoot();
//        SearchContext mtB1 = mtB.findElement(By.cssSelector("#input")).getShadowRoot();
//        mtB1.findElement(By.cssSelector("input[role='combobox']")).sendKeys(MTB);
//        mtB1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //MO Date Site B
//        Thread.sleep(1000);
//        SearchContext moDB = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(7)")).getShadowRoot();
//        SearchContext moDB1 = moDB.findElement(By.cssSelector("#input")).getShadowRoot();
//        moDB1.findElement(By.cssSelector("input[part='value']")).sendKeys(MODB);
//        moDB1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //Material Deleivered
//
//        //Material Dispatch Date A
//        SearchContext mdDA = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(11)")).getShadowRoot();
//        SearchContext mdDA1 = mdDA.findElement(By.cssSelector("#input")).getShadowRoot();
//        mdDA1.findElement(By.cssSelector("input[part='value']")).sendKeys(MDDA);
//        mdDA1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //Material Dispatch Date B
//        SearchContext mdDB = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(12)")).getShadowRoot();
//        SearchContext mdDB1 = mdDB.findElement(By.cssSelector("#input")).getShadowRoot();
//        mdDB1.findElement(By.cssSelector("input[part='value']")).sendKeys(MDDB);
//        mdDB1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //Delivery Date AT SIte A
//        SearchContext ddA = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(13)")).getShadowRoot();
//        SearchContext ddA1 = ddA.findElement(By.cssSelector("#input")).getShadowRoot();
//        ddA1.findElement(By.cssSelector("input[part='value']")).sendKeys(DDA);
//        ddA1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //Delivery Date AT SIte B
//        SearchContext ddB = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-date-picker:nth-child(14)")).getShadowRoot();
//        SearchContext ddB1 = ddB.findElement(By.cssSelector("#input")).getShadowRoot();
//        ddB1.findElement(By.cssSelector("input[part='value']")).sendKeys(DDB);
//        ddB1.findElement(By.cssSelector("input[part='value']")).sendKeys(Keys.ENTER);
//
//        //Material Deliver Status
//        SearchContext mds = driver.findElement(By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-form-layout:nth-child(3) > vaadin-combo-box:nth-child(15)")).getShadowRoot();
//        SearchContext mds1 = mds.findElement(By.cssSelector("#input")).getShadowRoot();
//        mds1.findElement(By.cssSelector("input[role='combobox']")).sendKeys(MDS);
//
//    }
    public static void saveButton(WebDriver driver) throws InterruptedException {
//        Thread.sleep(1000);
//        SearchContext shadow = driver.findElement(By.cssSelector("vaadin-button[role='button'][theme='primary']")).getShadowRoot();
//        Thread.sleep(1000);
//        shadow.findElement(By.cssSelector("#button")).click();
        String[] methods = {"waitForClickability", "scrollIntoView",  "javascriptClick"};
        boolean clicked = false;

        for (String method : methods) {
            try {
                switch (method) {
                    case "waitForClickability":
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                        SearchContext element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("vaadin-button[role='button'][theme='primary']"))).getShadowRoot();
                        element.findElement(By.cssSelector("#button")).click();
                        clicked = true;
                        break;

                    case "scrollIntoView":
                        SearchContext shadowRoot = driver.findElement(By.cssSelector("vaadin-button[role='button'][theme='primary']")).getShadowRoot();
                        WebElement elementToScroll=shadowRoot.findElement(By.cssSelector("#button"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementToScroll);
                        Thread.sleep(500); // Adding a small delay
                        elementToScroll.click();
                        clicked = true;
                        break;
                    case "javascriptClick":
                        SearchContext sr = driver.findElement(By.cssSelector("vaadin-button[role='button'][theme='primary']")).getShadowRoot();
                        WebElement elementToJSClick=sr.findElement(By.cssSelector("#button"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elementToJSClick);
                        clicked = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Method " + method + " failed: " + e.getMessage());
            }
            if (clicked) {
                System.out.println("Clicked using method: " + method);
                break;
            }
        }
        if (!clicked) {
            throw new RuntimeException("All methods failed to click the element.");
        }

    }

}
