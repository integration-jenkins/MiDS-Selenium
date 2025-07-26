package com.avendum.midsautomate.controller;

import com.avendum.midsautomate.model.BasicTestReport;
import com.avendum.midsautomate.model.DownloadReportTest;
import com.avendum.midsautomate.model.SampleUserCredentials;
import com.avendum.midsautomate.repository.BasicTestRepository;
import com.avendum.midsautomate.repository.DownloadReportTestRepository;
import com.avendum.midsautomate.repository.SampleUserCredentialsRepository;
import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import com.avendum.midsautomate.selenium.seleniumconfig.DriverSetup;
import com.avendum.midsautomate.selenium.seleniumconfig.SeleniumConfig;
import com.avendum.midsautomate.selenium.seleniumpages.Login;
import com.avendum.midsautomate.selenium.seleniumpages.LoginPage;
import com.avendum.midsautomate.selenium.utils.BasicTest;
import com.avendum.midsautomate.selenium.utils.PanelTraverser;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/basic-report")
@Service
@Slf4j
public class BasicTestReportController {
    @Autowired
    private BasicTestRepository basicTestRepository;

    @Value("${img.path}")
    private String imgPath;

    @Value("${download.path}")
    private String downloadPath;

    @Value("${opt.required}")
    private String otpCheck;

    private WebDriver myDriver;

    private static final Logger logger = Logger.getLogger(BasicTestReportController.class.getName());

    @Autowired
    private SampleUserCredentialsRepository sampleUserCredentialsRepository;

    @Autowired
    private DownloadReportTestRepository downloadReportTestRepository;

    @Lazy
    @Autowired
    private LoginPage loginPage;


    @GetMapping("/all")
    public ResponseEntity<List<BasicTestReport>> getAllBasicTestReports() {
        try {
            List<BasicTestReport> tests = basicTestRepository.findAll();
            return ResponseEntity.ok(tests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/all-download-reports")
    public ResponseEntity<List<DownloadReportTest>> getAllDownloadTestReports() {
        try {
            List<DownloadReportTest> tests = downloadReportTestRepository.findAll();
            return ResponseEntity.ok(tests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginStart(@RequestBody Map<String, String> payload) {
       try{
           if (payload == null || !payload.containsKey("user") || payload.get("user").isEmpty()|| !payload.containsKey("testName") ||payload.get("testName").isEmpty()) {
               return ResponseEntity.badRequest().body("Invalid payload: 'user' or 'testName' is missing.");
           }
           String username= payload.get("user");
           logger.info("Test Started for user: " + username);
           List<SampleUserCredentials> credentials;

           try {
               credentials = sampleUserCredentialsRepository.findByDoneByAndRole(username, "MW Planner");
           }catch (Exception e){
               logger.info("Error in the credentials"+e);
               return ResponseEntity.badRequest().body("No Data Found of MW Planner user "+username);
           }
           if (credentials == null || credentials.isEmpty()) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("No credentials found for role: MW Planner and username: " + username);
           }
           logger.info("Credentials found for user: " +credentials.get(0));
           SampleUserCredentials mwPlanner = credentials.get(0);
           String mwPlannerUserName = mwPlanner.getUserName();
           String mwPlannerPassword = mwPlanner.getPassword();
//           BasicTest basicTest = new BasicTest();

           //-------------------Special for taking screenshots----------------
           String savePath = imgPath;
           logger.info("Image save path: " + savePath);
           String page = "Login Page";
           boolean loginPageWork=false;
           long loginStartTime=0;
           long loginTimeTook=0;
           String fullScreenshotPath="";
           String sanitizedPageName = page.replaceAll("[^a-zA-Z0-9.-]", "_");
           if (sanitizedPageName.isEmpty()) {
               sanitizedPageName = "invalid_page_" + System.currentTimeMillis();
           }
           String screenshotFileName = sanitizedPageName + ".png";
//           String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//           String screenshotFileName = sanitizedPageName + "_" + timestamp + ".png";
           try {
               if (savePath == null || savePath.trim().isEmpty()) {
                   throw new IllegalStateException("Image save path is null or empty");
               }
               Path saveDirPath = Paths.get(savePath);
               if (!Files.exists(saveDirPath)) {
                   Files.createDirectories(saveDirPath);
               }
               if (!Files.isWritable(saveDirPath)) {
                   throw new IOException("No write permissions for: " + savePath);
               }

               fullScreenshotPath = Paths.get(savePath, screenshotFileName).toString();
               File destFile = new File(fullScreenshotPath);
               if (destFile.exists()) {
                   Files.delete(destFile.toPath());
               }
               // Wait for page stability
//               loginPage=new LoginPage();
               loginStartTime = System.currentTimeMillis();
//               loginPageWork = basicTest.isLoginPageWorking(mwPlannerUserName, mwPlannerPassword);
               myDriver=loginPage.login(mwPlannerUserName,mwPlannerPassword);
               loginTimeTook = System.currentTimeMillis() - loginStartTime;
               if(myDriver!=null){
                   loginPageWork=true;
               }
//               myDriver = loginPage.getLoginDriver();
               new WebDriverWait(myDriver, Duration.ofSeconds(10))
                       .until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
               File screenshot = ((TakesScreenshot) myDriver).getScreenshotAs(OutputType.FILE);
               Files.copy(screenshot.toPath(), destFile.toPath());
           } catch (Exception e) {
               String errorMsg = "Screenshot failed for [" + page + "]: " + e.getMessage();
               logger.info(errorMsg+" "+ e);
           }

          // ------------------------------Remove after it start working-----------------
           try{
//               long loginStartTime = System.currentTimeMillis();
//               boolean loginPageWork = basicTest.isLoginPageWorking(mwPlannerUserName, mwPlannerPassword);
//               long loginTimeTook = System.currentTimeMillis() - loginStartTime;
               BasicTestReport basicTestReport;
               basicTestReport = basicTestRepository.findByPageName("Login Page");
               if (basicTestReport == null) {
                   basicTestReport = new BasicTestReport();
                   basicTestReport.setPageName("Login Page");
               }
               if(loginPageWork){
                   basicTestReport.setLastExecutionStatus("Success");
                   basicTestReport.setLastTimeTakenMS(String.valueOf(loginTimeTook));
                   basicTestReport.setComments("Login page is rendering correctly");
                   basicTestReport.setImgPath(fullScreenshotPath);
                   Long avgLoginTime= basicTestRepository.findAverageTimeTakenMS("Login Page");
                   if (avgLoginTime == null) {
                       avgLoginTime = 0L;
                   }
                   Integer countt = basicTestRepository.findNoOfTest("Login Page");
                   if (countt == null) {
                       countt = 0;
                   }
                   Integer successCount = basicTestRepository.findNoOfSuccessTestCount("Login Page");
                   int count = (successCount != null) ? successCount : 0;
                   long newLoginAverageTime = (avgLoginTime + loginTimeTook) / (count + 1);
                   basicTestReport.setAverageTimeTakenMS(String.valueOf(newLoginAverageTime));
                   basicTestReport.setNoOfTest((int)(countt + 1));
                   basicTestReport.setNoOfSuccessTestCount((int)(count+1));
                   basicTestReport.setLastExecutionBy(username);
                   Date mydate = new Date();
                   basicTestReport.setLastExecutionDate(mydate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                   basicTestRepository.save(basicTestReport);
               }else{
                   basicTestReport.setLastExecutionStatus("Failed");
                   basicTestReport.setLastTimeTakenMS(String.valueOf(loginTimeTook));
                   basicTestReport.setComments("Login page is not rendering correctly");
                   basicTestReport.setLastExecutionBy(username);
                   Date mydate = new Date();
                   basicTestReport.setLastExecutionDate(mydate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                   basicTestRepository.save(basicTestReport);
                   return ResponseEntity.badRequest().body("Failed");
               }
           }catch(Exception e){
               Base.tearDown();
               logger.info("Failed to Click Login Button"+e);
               return ResponseEntity.badRequest().body("Failed");
           }
       }catch(Exception e){
           Base.tearDown();
           loginPage.cleanup();
           logger.info("Error occur during login page Execution"+e);
           return ResponseEntity.badRequest().body("Failed");
       }
       if(!otpCheck.equals("true")){
           String testName=payload.get("testName");
           if(testName.equals("Page Render Test")){
               getOTP(payload);
           }else if(testName.equals("Download Report Test")){
               downloadReport(payload);
           }else{
               logger.info("Test name is not valid");
               return  ResponseEntity.ok("Provide Valid Test Name");
           }
           return ResponseEntity.ok("Finished Test");
       }else{
           return ResponseEntity.ok("Success");
       }

    }
    @PostMapping("/otp-check")
    public ResponseEntity<String> getOTP(@RequestBody Map<String, String> payload) {
        try {
            String otp;
            boolean pageCheck=false;
            BasicTest basicTest = new BasicTest();
            if(otpCheck.equals("true")){
                if (payload == null || !payload.containsKey("user") || payload.get("user").isEmpty()|| !payload.containsKey("otp")|| payload.get("otp").isEmpty()) {
                    return ResponseEntity.badRequest().body("Invalid payload: 'user' or 'otp' is required.");
                }
                otp=payload.get("otp");
                logger.info("OTP Login Work");
//                LoginPage loginPage=new LoginPage();
//                pageCheck=basicTest.isOtpLoginWork(otp);
                pageCheck=loginPage.otpLoginPage(otp,myDriver);

            }else{
                if (payload == null || !payload.containsKey("user") || payload.get("user").isEmpty()) {
                    return ResponseEntity.badRequest().body("Invalid payload: 'user' is required.");
                }
                pageCheck=true;
            }
            String username= payload.get("user");
            logger.info("Test Started for user: " + username);
            String pages[]={"Dashboard Page","Deployment Dashboard","RAN MW Page","LB Report Page","UBR LB Report Page","POP Info Page","Atom Summary Page","WAN IP Page","DPR Report Page","SOFT AT Page","Deploy Assignment Page","PRI Issue","PRI Reporting","LB Recon","LB Parameter","HOP Frequency Report","RFC Report","Plan Upload","NEP Dismantle","Dismantle Material","Soft AT Upload","Mids DPR Upload","Frequency Detail report","Frequency Detail upload","Dismantel Track","Dismantle Report","Dismantle Upload","Change Assign User","Create OEM Vendor","Stock Dashboard","Order Summary report","Stock report","Item Code mapping","MW Plan Delete","Traffic Upload","Traffic Report","Traffic Track","Central Remark upload"};
            logger.info("Test Started");
            try{

                PanelTraverser panelTraverser=new PanelTraverser();
                BasicTestReport basicTestReport;
                if(pageCheck){
                    for (String page : pages) {
                        basicTestReport = basicTestRepository.findByPageName(page);
                        if (basicTestReport == null) {
                            basicTestReport = new BasicTestReport();
                            basicTestReport.setPageName(page);
                        }
//                        panelTraverser.exceptionThere(Base.getDriver());

                        panelTraverser.exceptionThere(myDriver);
                        long startTime = System.currentTimeMillis();
                        boolean check = basicTest.isPageRenderingCorrectly(page,myDriver);
                        long timeTook = System.currentTimeMillis() - startTime;


                        String savePath = imgPath;
                        logger.info("Image save path: " + savePath);
                        String sanitizedPageName = page.replaceAll("[^a-zA-Z0-9.-]", "_");
                        if (sanitizedPageName.isEmpty()) {
                            sanitizedPageName = "invalid_page_" + System.currentTimeMillis();
                        }
                        String screenshotFileName = sanitizedPageName + ".png";
//                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                        String screenshotFileName = sanitizedPageName + "_" + timestamp + ".png";
                        try {
                            if (savePath == null || savePath.trim().isEmpty()) {
                                throw new IllegalStateException("Image save path is null or empty");
                            }
                            Path saveDirPath = Paths.get(savePath);
                            if (!Files.exists(saveDirPath)) {
                                Files.createDirectories(saveDirPath);
                            }
                            if (!Files.isWritable(saveDirPath)) {
                                throw new IOException("No write permissions for: " + savePath);
                            }

                            String fullScreenshotPath = Paths.get(savePath, screenshotFileName).toString();
                            File destFile = new File(fullScreenshotPath);
                            if (destFile.exists()) {
                                Files.delete(destFile.toPath());
                            }
                            // Wait for page stability
//                            WebDriver driver = Base.getDriver();
                            new WebDriverWait(myDriver, Duration.ofSeconds(10))
                                    .until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
                            File screenshot = ((TakesScreenshot) myDriver).getScreenshotAs(OutputType.FILE);

                            Files.copy(screenshot.toPath(), destFile.toPath());
                            basicTestReport.setImgPath(fullScreenshotPath);
                        } catch (Exception e) {
                            String errorMsg = "Screenshot failed for [" + page + "]: " + e.getMessage();
                            logger.info(errorMsg+" "+ e);
                        }


                        basicTestReport.setLastExecutionStatus(check ? "Success" : "Failed");
                        basicTestReport.setLastTimeTakenMS(String.valueOf(timeTook));
                        if (!check) {
                            basicTestReport.setComments("Page is not rendering correctly");
                        } else {
                            basicTestReport.setComments("Page is rendering correctly");
                        }
                        Date date = new Date();
                        basicTestReport.setLastExecutionDate(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                        Long averageTime = basicTestRepository.findAverageTimeTakenMS(page);
                        if (averageTime == null) {
                            averageTime = 0L;
                        }

                        Integer count = basicTestRepository.findNoOfTest(page);
                        if (count == null) {
                            count = 0;
                        }
                        Integer successCount = basicTestRepository.findNoOfSuccessTestCount("Login Page");
                        int cnt = (successCount != null) ? successCount : 0;
                        long newAverageTime = (averageTime  + timeTook) / (cnt + 1);
                        if(check){
                            basicTestReport.setAverageTimeTakenMS(String.valueOf(newAverageTime));
                        }
                        basicTestReport.setNoOfTest((int)(count + 1));
                        basicTestReport.setNoOfSuccessTestCount((int)(cnt+1));
                        basicTestReport.setLastExecutionBy(username);
                        basicTestRepository.save(basicTestReport);

                    }
                }
            }catch (Exception e){
                logger.info("Error occur at otp page"+e);
                return ResponseEntity.badRequest().body("Failed");
            }
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            logger.info("Error occur at this OTP Test "+e);
            return ResponseEntity.internalServerError().body("Failed " );
        }finally{
            loginPage.cleanup();
            Base.tearDown();
        }
    }

    //Remove after finally code finished
//    @PostMapping("/page-test")
//    public ResponseEntity<String> startBasicTest(@RequestBody Map<String, String> payload){
//        try{
//            // Validate payload
//            if (payload == null || !payload.containsKey("user") || payload.get("user").isEmpty()) {
//                return ResponseEntity.badRequest().body("Invalid payload: 'user' is required.");
//            }
//            String username= payload.get("user");
//
//            // Log the start of the test
//            logger.info("Test Started for user: " + username);
//            //Assignment Report  , "Mids Nep Dismantle",
//            String pages[]={"Dashboard Page","Deployment Dashboard","RAN MW Page","LB Report Page","UBR LB Report Page","POP Info Page","Atom Summary Page","WAN IP Page","DPR Report Page","SOFT AT Page","Deploy Assignment Page","PRI Issue","PRI Reporting","LB Recon","LB Parameter","HOP Frequency Report","RFC Report","Plan Upload","NEP Dismantle","Dismantle Material","Soft AT Upload","Mids DPR Upload","Frequency Detail report","Frequency Detail upload","Dismantel Track","Dismantle Report","Dismantle Upload","Change Assign User","Create OEM Vendor","Stock Dashboard","Order Summary report","Stock report","Item Code mapping","MW Plan Delete","Traffic Upload","Traffic Report","Traffic Track","Central Remark upload"};
////        List<SampleUserCredentials> credentials = sampleUserCredentialsRepository.findAll();
////        SampleUserCredentials mwPlanner = credentials.stream()
////                .filter(cred -> "MW Planner".equals(cred.getDoneBy()))
////                .findFirst()
////                .orElse(null);
////        String mwPlannerUserName = mwPlanner != null ? mwPlanner.getUserName() : null;
////        String mwPlannerPassword = mwPlanner != null ? mwPlanner.getPassword() : null;
//            logger.info("Test Started");
//            List<SampleUserCredentials> credentials;
//            try {
//                credentials = sampleUserCredentialsRepository.findByDoneByAndRole(username, "MW Planner");
//            }catch (Exception e){
//                logger.info("Error in the credentials"+e);
//                return ResponseEntity.badRequest().body("No Data Found of MW Planner user "+username);
//            }
//            if (credentials == null || credentials.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("No credentials found for role: MW Planner and username: " + username);
//            }
//            logger.info("Credentials found for user: " +credentials.get(0));
//            SampleUserCredentials mwPlanner = credentials.get(0);
//            String mwPlannerUserName = mwPlanner.getUserName();
//            String mwPlannerPassword = mwPlanner.getPassword();
////        Map<String,String> map=mapUserDetails(sampleUserCredentialsRepository);
////        String mwPlannerUserName = map.keySet().stream().findFirst().orElse(null);
////        String mwPlannerPassword = map.get(mwPlannerUserName);
//            logger.info("MW Planner UserName: " + mwPlannerUserName);
//            BasicTest basicTest = new BasicTest();
//            try {
//                long loginStartTime = System.currentTimeMillis();
//                //Here Issue no otp check is there required
//                boolean loginPageWork = basicTest.isLoginPageWorking(mwPlannerUserName, mwPlannerPassword);
//
//                long loginTimeTook = System.currentTimeMillis() - loginStartTime;
//                BasicTestReport basicTestReport;
//                basicTestReport = basicTestRepository.findByPageName("Login Page");
//                if (basicTestReport == null) {
//                    basicTestReport = new BasicTestReport();
//                    basicTestReport.setPageName("Login Page");
//                }
//                if(loginPageWork){
//                    basicTestReport.setLastExecutionStatus("Success");
//                    basicTestReport.setLastTimeTakenMS(String.valueOf(loginTimeTook));
//                    basicTestReport.setComments("Login page is rendering correctly");
//                    Long avgLoginTime= basicTestRepository.findAverageTimeTakenMS("Login Page");
//                    if (avgLoginTime == null) {
//                        avgLoginTime = 0L;
//                    }
//                    Integer countt = basicTestRepository.findNoOfTest("Login Page");
//                    if (countt == null) {
//                        countt = 0;
//                    }
//                    long newLoginAverageTime = (avgLoginTime * countt + loginTimeTook) / (countt + 1);
//                    basicTestReport.setAverageTimeTakenMS(String.valueOf(newLoginAverageTime));
//                    basicTestReport.setNoOfTest((int)(countt + 1));
//                    basicTestReport.setLastExecutionBy(username);
//                    Date mydate = new Date();
//                    basicTestReport.setLastExecutionDate(mydate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
//                    basicTestRepository.save(basicTestReport);
//                    for (String page : pages) {
//                        basicTestReport = basicTestRepository.findByPageName(page);
//                        if (basicTestReport == null) {
//                            basicTestReport = new BasicTestReport();
//                            basicTestReport.setPageName(page);
//                        }
//                        long startTime = System.currentTimeMillis();
//                        boolean check = basicTest.isPageRenderingCorrectly(page);
//                        long timeTook = System.currentTimeMillis() - startTime;
//                        String savePath = imgPath;
//                        logger.info("Image save path: " + savePath);
//                        String sanitizedPageName = page.replaceAll("[^a-zA-Z0-9.-]", "_");
//                        if (sanitizedPageName.isEmpty()) {
//                            sanitizedPageName = "invalid_page_" + System.currentTimeMillis();
//                        }
//                        String screenshotFileName = sanitizedPageName + ".png";
//                        try {
//                            if (savePath == null || savePath.trim().isEmpty()) {
//                                throw new IllegalStateException("Image save path is null or empty");
//                            }
//                            Path saveDirPath = Paths.get(savePath);
//                            if (!Files.exists(saveDirPath)) {
//                                Files.createDirectories(saveDirPath);
//                            }
//                            if (!Files.isWritable(saveDirPath)) {
//                                throw new IOException("No write permissions for: " + savePath);
//                            }
//
//                            String fullScreenshotPath = Paths.get(savePath, screenshotFileName).toString();
//                            File destFile = new File(fullScreenshotPath);
//
//                            // Wait for page stability
//                            WebDriver driver = Base.getDriver();
//                            new WebDriverWait(driver, Duration.ofSeconds(10))
//                                    .until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
//                            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//                            if (destFile.exists()) {
//                                Files.delete(destFile.toPath());
//                            }
//                            Files.copy(screenshot.toPath(), destFile.toPath());
//                            basicTestReport.setImgPath(fullScreenshotPath);
//                        } catch (Exception e) {
//                            String errorMsg = "Screenshot failed for [" + page + "]: " + e.getMessage();
//                            logger.info(errorMsg+" "+ e);
//                        }
//
//                        basicTestReport.setLastExecutionStatus(check ? "Success" : "Failed");
//                        basicTestReport.setLastTimeTakenMS(String.valueOf(timeTook));
//                        if (!check) {
//                            basicTestReport.setComments("Page is not rendering correctly");
//                        } else {
//                            basicTestReport.setComments("Page is rendering correctly");
//                        }
//                        Date date = new Date();
//                        basicTestReport.setLastExecutionDate(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
//                        Long averageTime = basicTestRepository.findAverageTimeTakenMS(page);
//                        if (averageTime == null) {
//                            averageTime = 0L;
//                        }
//
//                        Integer count = basicTestRepository.findNoOfTest(page);
//                        if (count == null) {
//                            count = 0;
//                        }
//                        long newAverageTime = (averageTime * count + timeTook) / (count + 1);
//                        basicTestReport.setAverageTimeTakenMS(String.valueOf(newAverageTime));
//                        basicTestReport.setNoOfTest((int)(count + 1));
//                        basicTestReport.setLastExecutionBy(username);
//                        basicTestRepository.save(basicTestReport);
//                    }
//                }else{  basicTestReport.setLastExecutionStatus("Failed");
//                    basicTestReport.setComments("Login page is not rendering correctly");
//                    Integer countt = basicTestRepository.findNoOfTest("Login Page");
//                    if (countt == null) {
//                        countt = 0;
//                    }
//                    basicTestReport.setNoOfTest((int)(countt + 1));
//                    basicTestReport.setLastExecutionBy(username);
//                    Date mydate = new Date();
//                    basicTestReport.setLastExecutionDate(mydate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
//                    basicTestRepository.save(basicTestReport);
//                    return ResponseEntity.badRequest().body("Login failed for user: " + mwPlannerUserName);
//                }
//            }catch (Exception e){
//                return ResponseEntity.internalServerError().body("Error starting Basic Test: " + e.getMessage());
//            }
//            return ResponseEntity.ok("Basic Test started for user: " + username);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error starting Basic Test: " + e.getMessage());
//        }finally{
//            Base.tearDown();
//        }
//    }
    private Map<String,String> mapUserDetails(SampleUserCredentialsRepository sampleUserCredentialsRepository) {
        List<SampleUserCredentials> credentials = sampleUserCredentialsRepository.findAll();
        SampleUserCredentials mwPlanner = credentials.stream()
                .filter(cred -> "MW Planner".equals(cred.getDoneBy()))
                .findFirst()
                .orElse(null);
        String mwPlannerUserName = mwPlanner != null ? mwPlanner.getUserName() : null;
        String mwPlannerPassword = mwPlanner != null ? mwPlanner.getPassword() : null;
        assert mwPlannerUserName != null;
        Map<String, String> map = new HashMap<>();
        map.put(mwPlannerUserName, mwPlannerPassword);
        return map;
    }

    @PostMapping("/report-download")
    public ResponseEntity<String> downloadReport(@RequestBody Map<String, String> payload) {
        try{
            String otp;
            boolean pageCheck=false;
            BasicTest basicTest = new BasicTest();
            if(otpCheck.equals("true")){
                if (payload == null || !payload.containsKey("user") || payload.get("user").isEmpty()|| !payload.containsKey("otp")|| payload.get("otp").isEmpty()) {
                    return ResponseEntity.badRequest().body("Invalid payload: 'user' or 'otp' is required.");
                }
                otp=payload.get("otp");
                logger.info("OTP Login Work");
//                pageCheck=basicTest.isOtpLoginWork(otp);
                pageCheck=loginPage.otpLoginPage(otp,myDriver);

            }else{
                if (payload == null || !payload.containsKey("user") || payload.get("user").isEmpty()) {
                    return ResponseEntity.badRequest().body("Invalid payload: 'user' is required.");
                }
                pageCheck=true;
            }
//            String directory = payload.get("directory");//Download directory where file get downloaded
//            directory = directory.replace("\\", "\\\\");
            String directory=downloadPath;
            String user= payload.get("user");
            logger.info("Test Started for user: " + user);
            String sampleReports[]={"MW LB Report","UBR Report","Pop Report","Atom RA Report","WAN IP Report","DPR Report","CERAGON SOFT AT Report","ERICSSON SOFT AT Report","HUAWEI AT SOFT AT Report","AVIAT SOFT AT Report","Deployment Assignment Report","PRI Issue Data Report","PRI Email Report","MW LB Recon Report","LB Parameter Report","HOP Frequency Report","RFC Report","NEP Dismantle Link","Dismantle Material","Frequency Detail Report","Dismantle Track Report","Dismantle Report","Stock Dashboard Report","Order Summary Report","Stock Report","Item Code mapping Report","Traffic Report","Traffic Track Report"};
//        Map<String,String> map=mapUserDetails(sampleUserCredentialsRepository);
//        String mwPlannerUserName = map.keySet().stream().findFirst().orElse(null);
//        String mwPlannerPassword = map.get(mwPlannerUserName);
//            List<SampleUserCredentials> credentials;
//            try{
//                credentials = sampleUserCredentialsRepository.findByDoneByAndRole( user,"MW Planner");
//            }catch (Exception e){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("No credentials found for role: MW Planner and username: " + user);
//            }
//            if (credentials == null || credentials.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("No credentials found for role: MW Planner and username: " + user);
//            }
//            SampleUserCredentials mwPlanner = credentials.get(0);
//            String mwPlannerUserName = mwPlanner.getUserName();
//            String mwPlannerPassword = mwPlanner.getPassword();
            try{
//               boolean loginPageWork = basicTest.isLoginPageWorking(mwPlannerUserName, mwPlannerPassword);
                if(pageCheck){
                    DownloadReportTest downloadReportTest;
                    for(String sampleReport:sampleReports){
                        downloadReportTest = downloadReportTestRepository.findByReportName(sampleReport);
                        if (downloadReportTest == null) {
                            downloadReportTest = new DownloadReportTest();
                            downloadReportTest.setReportName(sampleReport);
                        }
                        String[] check= basicTest.isSampleReportDownloaded(sampleReport, directory,myDriver);
                        Long averageTime = downloadReportTestRepository.findAverageTimeTakenMS(sampleReport);
                        if (averageTime == null) {
                            averageTime = 0L;
                        }
                        Integer count = downloadReportTestRepository.findNoOfTest(sampleReport);
                        if (count == null) {
                            count = 0;
                        }
                        Integer successCount=downloadReportTestRepository.findNoOfSuccessTestCount(sampleReport);
                        int cnt = (successCount != null) ? successCount : 0;

                        if(check[0].equals("Success")) {
                            downloadReportTest.setDownloadedReportPath(check[2]);
                            downloadReportTest.setDownloadReportTestStatus("Success");
                            downloadReportTest.setComments("File downloaded successfully");
                            downloadReportTest.setNoOfSuccessTestCount((int)(cnt+1));
                            long tookTime = Long.parseLong(check[1]);
                            downloadReportTest.setLastTimeTakenMS(String.valueOf(tookTime));
                            long newAverageTime = (averageTime + tookTime) / (cnt + 1);
                            downloadReportTest.setAverageTimeTakenMS(String.valueOf(newAverageTime));
                        }else{
                            downloadReportTest.setDownloadedReportPath(check[2]);
                            downloadReportTest.setDownloadReportTestStatus("Failed");
                            downloadReportTest.setComments("File not downloaded");
                        }
                        Date date = new Date();
                        downloadReportTest.setLastExecutionDate(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                        downloadReportTest.setLastExecutionBy(user);
                        downloadReportTest.setNoOfTest((int)(count + 1));
                        downloadReportTestRepository.save(downloadReportTest);
                    }

                }
            }catch (Exception e){
                return ResponseEntity.internalServerError().body("Error starting Download report Test: " + e.getMessage());
            }

            return ResponseEntity.ok("Download Report Test Completed for user: " + user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error starting Download Report test: " + e.getMessage());
        }finally{
            Base.tearDown();
        }
    }
}
