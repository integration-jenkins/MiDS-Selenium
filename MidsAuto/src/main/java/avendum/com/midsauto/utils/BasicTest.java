package avendum.com.midsauto.utils;

import avendum.com.midsauto.config.Base;
import avendum.com.midsauto.pages.Dashboard;
import avendum.com.midsauto.pages.DeployDashboard;
import avendum.com.midsauto.pages.Login;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.sql.Array;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BasicTest {
    private String url;
    private WebDriver driver;
    //url Checking first
    public boolean isUrlCorrect(String url) {
        this.url=url;
        // Implement logic to check if the URL is correct
        return true; // Placeholder return value
    }


    //Login Page Working
    public boolean isLoginPageWorking(String username,String password) {
        try{
            Login login = new Login();
            login.Login(username,password);
            driver= Base.getDriver();
        }catch (Exception e){
            System.out.println("Login page is not working");
            return false;
        }
        return true;
    }

    //rendering all pages are render correctly or not
    public boolean isPageRenderingCorrectly(String pageName) throws InterruptedException {
        PanelTraverser panelTraverser = new PanelTraverser();
        try {
            //Dashboard Page (Done)
            if (pageName.equalsIgnoreCase("Dashboard Page")) {
                panelTraverser.navigateToDashboard(driver);
                Dashboard dashboard = new Dashboard();
                dashboard.launchCircleWisePage(driver);
            }
            //Deployement Dashboard
            else if (pageName.equalsIgnoreCase("Deployment Dashboard")) {
                panelTraverser.navigateToDeployDashboard(driver);
                DeployDashboard deployDashboard = new DeployDashboard();
                deployDashboard.launchCircleWisePage(driver);
            }
            //MW Plan Tracking Page(Done)
            else if (pageName.equalsIgnoreCase("MW Plan Tracking Page")) {
                panelTraverser.navigateToMWPlanTrackingPage(driver);
            }
            //RAN Mw planning
            else if (pageName.equalsIgnoreCase("RAN MW Page")) {
                panelTraverser.navigateToRANMWPlanning(driver);
            }
            //LB Report Page(Done)
            else if (pageName.equalsIgnoreCase("LB Report Page")) {
                panelTraverser.navigateToLBreport(driver);
            }
            //UBR LB Report Page (Done)
            else if (pageName.equalsIgnoreCase("UBR LB Report Page")) {
                panelTraverser.navigateToUBRLBReport(driver);
            }
            //POP Info Page
            else if (pageName.equalsIgnoreCase("POP Info Page")) {
                panelTraverser.navigateToPOPInfo(driver);
            }
            //Atom Summary Page(Done)
            else if (pageName.equalsIgnoreCase("Atom Summary Page")) {
                panelTraverser.navigateToAtomSummary(driver);
            }
            //WAN IP Page
            else if (pageName.equalsIgnoreCase("WAN IP Page")) {
                panelTraverser.navigateToWANIP(driver);
            }
            //DPR Report Page
            else if (pageName.equalsIgnoreCase("DPR Report Page")) {
                panelTraverser.navigateToDPRreport(driver);
            }
            //SOFT AT Page
            else if (pageName.equalsIgnoreCase("SOFT AT Page")) {
                panelTraverser.navigateToSOFTAT(driver);
            }
            //Deploy Assignment Page
            else if (pageName.equalsIgnoreCase("Deploy Assignment Page")) {
                panelTraverser.navigateToDeployAssignment(driver);
            }
            //PRI Issue
            else if (pageName.equalsIgnoreCase("PRI Issue")) {
                panelTraverser.navigateToPRIIssue(driver);
            }
            //PRI Reporting
            else if (pageName.equalsIgnoreCase("PRI Reporting")) {
                panelTraverser.navigateToPRIReporting(driver);
            }
            //Assignment Report
            else if (pageName.equalsIgnoreCase("Assignment Report")) {
                panelTraverser.navigateToAssignmentReport(driver);
            }
            //LB recon
            else if (pageName.equalsIgnoreCase("LB Recon")) {
                panelTraverser.navigateToMWLBRecon(driver);
            }
            //LB Parameter
            else if (pageName.equalsIgnoreCase("LB Parameter")) {
                panelTraverser.navigateToLBParameter(driver);
            }
            //HOP Frequency Report
            else if (pageName.equalsIgnoreCase("HOP Frequency Report")) {
                panelTraverser.navigateToHOPFrequencyReport(driver);
            }
            //RFC Report
            else if (pageName.equalsIgnoreCase("RFC Report")) {
                panelTraverser.navigateToRFC(driver);
            }
            //Plan Upload
            else if (pageName.equalsIgnoreCase("Plan Upload")) {
                panelTraverser.navigateToPlanUpload(driver);
            }
            //Mids Nep Dismantle
            else if (pageName.equalsIgnoreCase("Mids Nep Dismantle")) {
                panelTraverser.navigateToMidsNepDismantle(driver);
            }
            //NEP Dismantle
            else if (pageName.equalsIgnoreCase("NEP Dismantle")) {
                panelTraverser.navigateToNepDismantle(driver);
            }
            //Dismantle Material
            else if (pageName.equalsIgnoreCase("Dismantle Material")) {
                panelTraverser.navigateToDismantleMaterial(driver);
            }
            //Soft AT Upload
            else if (pageName.equalsIgnoreCase("Soft AT Upload")) {
                panelTraverser.navigateToSoftATUpload(driver);
            }
            //Mids DPR Upload
            else if (pageName.equalsIgnoreCase("Mids DPR Upload")) {
                panelTraverser.navigateToMidsDPRUpload(driver);
            }
            //Frequency Detail report
            else if (pageName.equalsIgnoreCase("Frequency Detail report")) {
                panelTraverser.navigateToFrequencyDetailReport(driver);
            }
            //Frequency Detail upload
            else if (pageName.equalsIgnoreCase("Frequency Detail upload")) {
                panelTraverser.navigateToFrequencyDetailUpload(driver);
            }
            //Dismantel Track
            else if (pageName.equalsIgnoreCase("Dismantel Track")) {
                panelTraverser.navigateToDismantleTrack(driver);
            }
            //Dismantle Report
            else if (pageName.equalsIgnoreCase("Dismantle Report")) {
                panelTraverser.navigateToDismantleReport(driver);
            }
            //Dismantle Upload
            else if (pageName.equalsIgnoreCase("Dismantle Upload")) {
                panelTraverser.navigateToDismantleUpload(driver);
            }
            //Change Assign User
            else if (pageName.equalsIgnoreCase("Change Assign User")) {
                panelTraverser.navigateToChangeAssignUser(driver);
            }
            //Create OEM Vendor
            else if (pageName.equalsIgnoreCase("Create OEM Vendor")) {
                panelTraverser.navigateToCreateOEMVendor(driver);
            }
            //Stock Dashboard
            else if (pageName.equalsIgnoreCase("Stock Dashboard")) {
                panelTraverser.navigateToStockDashboard(driver);
            }
            //Order Summary report
            else if (pageName.equalsIgnoreCase("Order Summary report")) {
                panelTraverser.navigateToOrderSummaryReport(driver);
            }
            //Stock report
            else if (pageName.equalsIgnoreCase("Stock report")) {
                panelTraverser.navigateToStockReport(driver);
            }
            //Item Code mapping
            else if (pageName.equalsIgnoreCase("Item Code mapping")) {
                panelTraverser.navigateToItemCodeMapping(driver);
            }
            //MW Plan Delete
            else if (pageName.equalsIgnoreCase("MW Plan Delete")) {
                panelTraverser.navigateToMWPlanDelete(driver);
            }
            //Traffic Upload
            else if (pageName.equalsIgnoreCase("Traffic Upload")) {
                panelTraverser.navigateToTrafficUpload(driver);
            }
            //Traffic Report
            else if (pageName.equalsIgnoreCase("Traffic Report")) {
                panelTraverser.navigateToTrafficReport(driver);
            }
            //Traffic Track
            else if (pageName.equalsIgnoreCase("Traffic Track")) {
                panelTraverser.navigateToTrafficTrack(driver);
            }
            //Central Remark upload
            else if (pageName.equalsIgnoreCase("Central Remark upload")) {
                panelTraverser.navigateToCentralRemarkUpload(driver);
            }
        }catch (Exception e){
            System.out.println("Page is not rendering correctly");
            return false;
        }
        return true;
    }

    //Sample report are get downloaded or not
    private File findDownloadedFile(String downloadDir, long startTime, String fileExtension, long timeout) throws InterruptedException {
        File dir = new File(downloadDir);
        while (System.currentTimeMillis() - startTime < timeout) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(fileExtension));
            if (files != null) {
                for (File file : files) {
                    if (file.lastModified() > startTime) {
                        return file;
                    }
                }
            }
            Thread.sleep(1000); // Check every 1 second
        }
        return null;
    }
//    @Getter
//    private Map<String, String[]> sampleData ;

    public  String[] isSampleReportDownloaded(String reportName,String downloadDir) {
        PanelTraverser panelTraverser = new PanelTraverser();
        String[] sampleData=new String[3];
        long timeout = 360000; // 30 seconds
        long tookTime=0;
        try {
            long startTime = 0;
            File downloadedFile=null;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(360));
            if (reportName.equalsIgnoreCase("MW LB Report")) {
                panelTraverser.navigateToLBreport(driver);
                SearchContext shadow = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("body > div:nth-child(4) > app-layout-left-hybrid:nth-child(1) > div:nth-child(3) > vaadin-vertical-layout:nth-child(1) > vaadin-button:nth-child(2)")
                )).getShadowRoot();
                startTime = System.currentTimeMillis();
                shadow.findElement(By.cssSelector("#button")).click();
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            } else if (reportName.equalsIgnoreCase("UBR Report")) {
                panelTraverser.navigateToUBRLBReport(driver);
                WebElement ubrReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[2]/a/iron-icon")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ubrReport);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Pop Report")) {
                panelTraverser.navigateToPOPInfo(driver);
                WebElement myReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/a")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", myReport);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xls", timeout);
            }else if (reportName.equalsIgnoreCase("Atom RA Report")) {
                panelTraverser.navigateToAtomSummary(driver);
                WebElement myReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/a/iron-icon")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", myReport);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xls", timeout);
            }else if (reportName.equalsIgnoreCase("WAN IP Report")) {
                panelTraverser.navigateToWANIP(driver);
                WebElement myReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/a/iron-icon")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", myReport);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xls", timeout);
            }else if (reportName.equalsIgnoreCase("DPR Report")) {
                panelTraverser.navigateToDPRreport(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                Thread.sleep(500);
                SearchContext shadow2 = driver.findElement(By.cssSelector("vaadin-radio-button[role='radio'][tabindex='-1']")).getShadowRoot();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", shadow2.findElement(By.cssSelector("label")));
                SearchContext shadow = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("body > vaadin-dialog-overlay:nth-child(7) > flow-component-renderer:nth-child(1) > div:nth-child(1) > vaadin-button:nth-child(2)"))).getShadowRoot();
                shadow.findElement(By.cssSelector("#button")).click();
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("CERAGON SOFT AT Report")) {
                panelTraverser.navigateToSOFTAT(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("ERICSSON SOFT AT Report")) {
                panelTraverser.navigateToSOFTAT(driver);
                WebElement elementt=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-tabs/vaadin-tab[2]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elementt);
                Thread.sleep(8000);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("HUAWEI AT SOFT AT Report")) {
                panelTraverser.navigateToSOFTAT(driver);
                WebElement elementt=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-tabs/vaadin-tab[3]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elementt);
                Thread.sleep(8000);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("AVIAT SOFT AT Report")) {
                panelTraverser.navigateToSOFTAT(driver);
                WebElement elementt=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-tabs/vaadin-tab[4]")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elementt);
                Thread.sleep(8000);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Deployment Assignment Report")) {
                panelTraverser.navigateToDeployAssignment(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("PRI Issue Data Report")) {
                panelTraverser.navigateToPRIIssue(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("PRI Email Report")) {
                panelTraverser.navigateToPRIReporting(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button[2]")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("MW LB Recon Report")) {
                panelTraverser.navigateToMWLBRecon(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/a/iron-icon")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xls", timeout);
            }else if (reportName.equalsIgnoreCase("LB Parameter Report")) {
                panelTraverser.navigateToLBParameter(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("HOP Frequency Report")) {
                panelTraverser.navigateToHOPFrequencyReport(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/a/iron-icon")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("RFC Report")) {
                panelTraverser.navigateToRFC(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("NEP Dismantle Link")) {
                panelTraverser.navigateToNepDismantle(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Dismantle Material")) {
                panelTraverser.navigateToDismantleMaterial(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/a/iron-icon")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Frequency Detail Report")) {
                panelTraverser.navigateToFrequencyDetailReport(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button[1]")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Dismantle Track Report")) {
                panelTraverser.navigateToDismantleTrack(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button[3]")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Dismantle Report")) {
                panelTraverser.navigateToDismantleReport(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Stock Dashboard Report")) {
                panelTraverser.navigateToStockDashboard(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Order Summary Report")) {
                panelTraverser.navigateToOrderSummaryReport(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button[2]")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Stock Report")) {
                panelTraverser.navigateToStockReport(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button[2]")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Item Code mapping Report")) {
                panelTraverser.navigateToItemCodeMapping(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-vertical-layout[1]/vaadin-horizontal-layout/vaadin-button[1]")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Traffic Report")) {
                panelTraverser.navigateToTrafficReport(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }else if (reportName.equalsIgnoreCase("Traffic Track Report")) {
                panelTraverser.navigateToTrafficTrack(driver);
                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout[1]/vaadin-button")));
                startTime = System.currentTimeMillis();
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
            }
            tookTime = (startTime == 0) ? 0 : System.currentTimeMillis() - startTime;
//            else if (reportName.equalsIgnoreCase("Soft AT Upload Sample File")) {
//                panelTraverser.navigateToSoftATUpload(driver);
//                WebElement element=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/app-layout-left-hybrid/div[3]/vaadin-vertical-layout/vaadin-horizontal-layout/a[1]")));
//                startTime = System.currentTimeMillis();
//                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
//                downloadedFile = findDownloadedFile(downloadDir, startTime, ".xlsx", timeout);
//            }

            if (downloadedFile != null) {
                System.out.println("File downloaded successfully: " + downloadedFile.getAbsolutePath());
//                sampleData.put(reportName, new String[]{"Success",String.valueOf(tookTime), downloadedFile.getAbsolutePath()});
                return new String[]{"Success", String.valueOf(tookTime), downloadedFile.getAbsolutePath()};
            } else {
                System.out.println("No file downloaded within the timeout period.");
                return new String[]{"Failed", String.valueOf(tookTime), "No file downloaded"};
            }
        } catch (Exception e) {
            System.out.println("An error occurred while downloading the report: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return new String[]{"Failed", String.valueOf(tookTime), "An error occurred: " + e.getMessage()};
        }
    }

    {/*
    public static void main(String[] args) throws InterruptedException {
        BasicTest bb=new BasicTest();
        System.out.println("Login Success: "+bb.isLoginPageWorking("Z_Bhanu","adm@123"));
//        //Assignment Report  , "Mids Nep Dismantle",
//        String pages[]={"Dashboard Page","Deployment Dashboard","RAN MW Page","LB Report Page","UBR LB Report Page","POP Info Page","Atom Summary Page","WAN IP Page","DPR Report Page","SOFT AT Page","Deploy Assignment Page","PRI Issue","PRI Reporting","LB Recon","LB Parameter","HOP Frequency Report","RFC Report","Plan Upload","NEP Dismantle","Dismantle Material","Soft AT Upload","Mids DPR Upload","Frequency Detail report","Frequency Detail upload","Dismantel Track","Dismantle Report","Dismantle Upload","Change Assign User","Create OEM Vendor","Stock Dashboard","Order Summary report","Stock report","Item Code mapping","MW Plan Delete","Traffic Upload","Traffic Report","Traffic Track","Central Remark upload"};
////        String pages[]={"Central Remark upload",};
//        for (String page : pages) {
//            long startTime = System.currentTimeMillis();
//            boolean checkPage = bb.isPageRenderingCorrectly(page);
//            long timeTook = System.currentTimeMillis() - startTime;
//            System.out.println("Page: " + page + " (Success: " + checkPage + ", Time taken: " + timeTook + "ms)");
//        }

//        bb.isSampleReportDownloaded("Pop Report");
        String sampleReport[]={"MW LB Report","UBR Report","Pop Report","Atom RA Report","WAN IP Report","DPR Report","CERAGON SOFT AT Report","ERICSSON SOFT AT Report","HUAWEI AT SOFT AT Report","AVIAT SOFT AT Report","Deployment Assignment Report","PRI Issue Data Report","PRI Email Report","MW LB Recon Report","LB Parameter Report","HOP Frequency Report","RFC Report","NEP Dismantle Link","Dismantle Material","Frequency Detail Report","Dismantle Track Report","Dismantle Report","Stock Dashboard Report","Order Summary Report","Stock Report","Item Code mapping Report","Traffic Report","Traffic Track Report"};
        for(String report:sampleReport){
            String downloadDir = "C:\\Users\\Bhanu\\Downloads";
            boolean check=bb.isSampleReportDownloaded(report,downloadDir);
            System.out.println("Report: "+report+" (Success: "+check+")");
        }
    }
    */}
}
