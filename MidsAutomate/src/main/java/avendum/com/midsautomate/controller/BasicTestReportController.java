package avendum.com.midsautomate.controller;

import avendum.com.midsauto.controller.MWPlanner;
import avendum.com.midsauto.utils.BasicTest;
import avendum.com.midsautomate.model.BasicTestReport;
import avendum.com.midsautomate.model.DownloadReportTest;
import avendum.com.midsautomate.model.MidsTest;
import avendum.com.midsautomate.model.SampleUserCredentials;
import avendum.com.midsautomate.repository.BasicTestRepository;
import avendum.com.midsautomate.repository.DownloadReportTestRepository;
import avendum.com.midsautomate.repository.SampleUserCredentialsRepository;
import avendum.com.midsautomate.util.MapedSampleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/basic-report")
public class BasicTestReportController {
    @Autowired
    private BasicTestRepository basicTestRepository;

    @Autowired
    private SampleUserCredentialsRepository sampleUserCredentialsRepository;

    @Autowired
    private DownloadReportTestRepository downloadReportTestRepository;

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

    @PostMapping("/page-test")
    public ResponseEntity<String> startBasicTest(@RequestBody Map<String, String> payload){
        String username= payload.get("user");
        //Assignment Report  , "Mids Nep Dismantle",
        String pages[]={"Dashboard Page","Deployment Dashboard","RAN MW Page","LB Report Page","UBR LB Report Page","POP Info Page","Atom Summary Page","WAN IP Page","DPR Report Page","SOFT AT Page","Deploy Assignment Page","PRI Issue","PRI Reporting","LB Recon","LB Parameter","HOP Frequency Report","RFC Report","Plan Upload","NEP Dismantle","Dismantle Material","Soft AT Upload","Mids DPR Upload","Frequency Detail report","Frequency Detail upload","Dismantel Track","Dismantle Report","Dismantle Upload","Change Assign User","Create OEM Vendor","Stock Dashboard","Order Summary report","Stock report","Item Code mapping","MW Plan Delete","Traffic Upload","Traffic Report","Traffic Track","Central Remark upload"};
//        List<SampleUserCredentials> credentials = sampleUserCredentialsRepository.findAll();
//        SampleUserCredentials mwPlanner = credentials.stream()
//                .filter(cred -> "MW Planner".equals(cred.getDoneBy()))
//                .findFirst()
//                .orElse(null);
//        String mwPlannerUserName = mwPlanner != null ? mwPlanner.getUserName() : null;
//        String mwPlannerPassword = mwPlanner != null ? mwPlanner.getPassword() : null;
        Map<String,String> map=mapUserDetails(sampleUserCredentialsRepository);
        String mwPlannerUserName = map.keySet().stream().findFirst().orElse(null);
        String mwPlannerPassword = map.get(mwPlannerUserName);
        BasicTest basicTest = new BasicTest();
        try {
            long loginStartTime = System.currentTimeMillis();
            boolean loginPageWork = basicTest.isLoginPageWorking(mwPlannerUserName, mwPlannerPassword);
            long loginTimeTook = System.currentTimeMillis() - loginStartTime;
            BasicTestReport basicTestReport;
            //For Login
            basicTestReport = basicTestRepository.findByPageName("Login Page");
            if (basicTestReport == null) {
                basicTestReport = new BasicTestReport();
                basicTestReport.setPageName("Login Page");
            }
            if(loginPageWork){
                basicTestReport.setLastExecutionStatus("Success");
                basicTestReport.setLastTimeTakenMS(String.valueOf(loginTimeTook));
                basicTestReport.setComments("Login page is rendering correctly");
                Long avgLoginTime= basicTestRepository.findAverageTimeTakenMS("Login Page");
                if (avgLoginTime == null) {
                    avgLoginTime = 0L;
                }
                Integer countt = basicTestRepository.findNoOfTest("Login Page");
                if (countt == null) {
                    countt = 0;
                }
                long newLoginAverageTime = (avgLoginTime * countt + loginTimeTook) / (countt + 1);
                basicTestReport.setAverageTimeTakenMS(String.valueOf(newLoginAverageTime));
                basicTestReport.setNoOfTest((int)(countt + 1));
                basicTestReport.setLastExecutionBy(username);
                Date mydate = new Date();
                basicTestReport.setLastExecutionDate(mydate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                basicTestRepository.save(basicTestReport);
            for (String page : pages) {
                basicTestReport = basicTestRepository.findByPageName(page);
                if (basicTestReport == null) {
                    basicTestReport = new BasicTestReport();
                    basicTestReport.setPageName(page);
                }
                long startTime = System.currentTimeMillis();
                boolean check = basicTest.isPageRenderingCorrectly(page);
                long timeTook = System.currentTimeMillis() - startTime;
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
                long newAverageTime = (averageTime * count + timeTook) / (count + 1);
                basicTestReport.setAverageTimeTakenMS(String.valueOf(newAverageTime));
                basicTestReport.setNoOfTest((int)(count + 1));
                basicTestReport.setLastExecutionBy(username);
                basicTestRepository.save(basicTestReport);
            }
        }else{  basicTestReport.setLastExecutionStatus("Failed");
                basicTestReport.setComments("Login page is not rendering correctly");
                Integer countt = basicTestRepository.findNoOfTest("Login Page");
                if (countt == null) {
                    countt = 0;
                }
                basicTestReport.setNoOfTest((int)(countt + 1));
                basicTestReport.setLastExecutionBy(username);
                Date mydate = new Date();
                basicTestReport.setLastExecutionDate(mydate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                basicTestRepository.save(basicTestReport);
                return ResponseEntity.badRequest().body("Login failed for user: " + mwPlannerUserName);
            }

        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error starting Basic Test: " + e.getMessage());
        }
        return ResponseEntity.ok("Basic Test started for user: " + username);
    }
    private Map<String,String> mapUserDetails(SampleUserCredentialsRepository sampleUserCredentialsRepository) {
        List<SampleUserCredentials> credentials = sampleUserCredentialsRepository.findAll();
        SampleUserCredentials mwPlanner = credentials.stream()
                .filter(cred -> "MW Planner".equals(cred.getDoneBy()))
                .findFirst()
                .orElse(null);
        String mwPlannerUserName = mwPlanner != null ? mwPlanner.getUserName() : null;
        String mwPlannerPassword = mwPlanner != null ? mwPlanner.getPassword() : null;
        assert mwPlannerUserName != null;
        return new HashMap<>(Map.of(
                mwPlannerUserName, mwPlannerPassword
        ));
    }

    @PostMapping("/report-download")
    public ResponseEntity<String> downloadReport(@RequestBody Map<String, String> payload) {
        String directory = payload.get("directory");//Download directory where file get downloaded
        directory = directory.replace("\\", "\\\\");
        String user= payload.get("user");
        String sampleReports[]={"MW LB Report","UBR Report","Pop Report","Atom RA Report","WAN IP Report","DPR Report","CERAGON SOFT AT Report","ERICSSON SOFT AT Report","HUAWEI AT SOFT AT Report","AVIAT SOFT AT Report","Deployment Assignment Report","PRI Issue Data Report","PRI Email Report","MW LB Recon Report","LB Parameter Report","HOP Frequency Report","RFC Report","NEP Dismantle Link","Dismantle Material","Frequency Detail Report","Dismantle Track Report","Dismantle Report","Stock Dashboard Report","Order Summary Report","Stock Report","Item Code mapping Report","Traffic Report","Traffic Track Report"};
        Map<String,String> map=mapUserDetails(sampleUserCredentialsRepository);
        String mwPlannerUserName = map.keySet().stream().findFirst().orElse(null);
        String mwPlannerPassword = map.get(mwPlannerUserName);
        BasicTest basicTest = new BasicTest();
        try{
            boolean loginPageWork = basicTest.isLoginPageWorking(mwPlannerUserName, mwPlannerPassword);
            if(loginPageWork){
                for(String sampleReport:sampleReports){
                    DownloadReportTest downloadReportTest = downloadReportTestRepository.findByReportName(sampleReport);
                    if (downloadReportTest == null) {
                        downloadReportTest = new DownloadReportTest();
                        downloadReportTest.setReportName(sampleReport);
                    }
                     String[] check= basicTest.isSampleReportDownloaded(sampleReport, directory);
                    Long averageTime = downloadReportTestRepository.findAverageTimeTakenMS(sampleReport);
                    if (averageTime == null) {
                        averageTime = 0L;
                    }
                    Integer count = downloadReportTestRepository.findNoOfTest(sampleReport);
                    if (count == null) {
                        count = 0;
                    }

                    if(check[0].equals("Success")) {
                        downloadReportTest.setDownloadedReportPath(check[2]);
                        downloadReportTest.setDownloadReportTestStatus("Success");
                        downloadReportTest.setComments("File downloaded successfully");
                        long tookTime = Long.parseLong(check[1]);
                        downloadReportTest.setLastTimeTakenMS(String.valueOf(tookTime));
                        long newAverageTime = (averageTime * count + tookTime) / (count + 1);
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
    }
}
