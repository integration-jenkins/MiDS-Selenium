package com.avendum.midsautomate.controller;

import com.avendum.midsautomate.model.MidsTest;
import com.avendum.midsautomate.model.MidsTestManagement;
import com.avendum.midsautomate.model.SampleUserCredentials;
import com.avendum.midsautomate.repository.AllMidsTestRepository;
import com.avendum.midsautomate.repository.MidsTestRepository;
import com.avendum.midsautomate.repository.SampleUserCredentialsRepository;
import com.avendum.midsautomate.selenium.Start;
import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import com.avendum.midsautomate.selenium.seleniumconfig.SampleUsersCredentials;
import com.avendum.midsautomate.selenium.seleniumcontroller.MWPlanner;
import com.avendum.midsautomate.selenium.seleniumpages.Login;
import com.avendum.midsautomate.selenium.tests.TestExecutor;
import com.avendum.midsautomate.selenium.utils.BasicTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/midstests")
@CrossOrigin(origins = "*", maxAge = 3600)
@Service
public class MidsTestController {
    @Autowired
    private MidsTestRepository repository;
    private static final Logger logger = Logger.getLogger(MidsTestController.class.getName());

    @Value("${opt.required}")
    private String otpCheck;

    @Autowired
    private AllMidsTestRepository allMidsTestRepository;




    @Autowired
    private MidsTestRepository midstest;
    @Autowired
    private SampleUserCredentialsRepository sampleUserCredentialsRepository;

    @PostMapping("/add-mids-test")
    public ResponseEntity<MidsTestManagement> addTest(@RequestBody MidsTestManagement test) {
        logger.info("Test Data "+test);
        MidsTestManagement savedTest = midstest.save(test);
        return ResponseEntity.ok(savedTest);
    }


    @PostMapping("/login-mids")
    public ResponseEntity<String> loginMids(@RequestBody Map<String, String> payload) {
        try{
            if (payload == null || !payload.containsKey("user") || payload.get("user").isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid payload: 'user' is missing.");
            }
            String username= payload.get("user");
            String loginUser=payload.get("username");
            String loginUserPassword=payload.get("password");
            logger.info("Test Started for user: " + username);
            BasicTest basicTest = new BasicTest();
            try{
                basicTest.isLoginPageWorking(loginUser, loginUserPassword);
            }catch (Exception e){
                Base.tearDown();
                logger.info("Failed to Login "+e);
                return ResponseEntity.badRequest().body("Failed");
            }
        }catch (Exception e){
            Base.tearDown();
            logger.info("Failed to login: "+e);
            return ResponseEntity.badRequest().body("Failed");
        }
        if(!otpCheck.equals("true")){
            return ResponseEntity.ok("With out Otp test");
        }else{
            return ResponseEntity.ok("Otp test");
        }
    }

//    @GetMapping
//    public List<MidsTestManagement> getAllTests() {
//        System.out.println("Fetching all tests "+repository.findAll());
//        return repository.findAll();
//    }
    @GetMapping
    public ResponseEntity<List<MidsTestManagement>> getAllTests() {
        try {
            List<MidsTestManagement> tests = repository.findAll();
            return ResponseEntity.ok(tests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<MidsTest>> getAllMidsTests() {
        try {
            List<MidsTest> tests = allMidsTestRepository.findAll();
            return ResponseEntity.ok(tests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/page/{pageName}")
    public List<MidsTestManagement> getTestsByPage(@PathVariable String pageName) {
        return repository.findByPageName(pageName);
    }

    @PostMapping("/mwDprPage")
    public ResponseEntity<String> mwDprTest(@RequestBody Map<String, String> payload) {
        try {
            if(payload == null || payload.isEmpty()) {
                return ResponseEntity.badRequest().body("Payload cannot be null or empty");
            }

            String startPoint = payload.get("startPoint");
            String endPoint = payload.get("endPoint");
            String userName = payload.get("user");

            // Fetch user credentials for different users from the SampleUserCredentials table
//            List<SampleUserCredentials> credentials = sampleUserCredentialsRepository.findAll();
//            Map<String, String> map = new HashMap<>();
//            // For MW Planner
//            SampleUserCredentials mwPlanner = credentials.stream()
//                    .filter(cred -> "MW Planner".equals(cred.getDoneBy()))
//                    .findFirst()
//                    .orElse(null);
//            String mwPlannerUserName = mwPlanner != null ? mwPlanner.getUserName() : null;
//            String mwPlannerPassword = mwPlanner != null ? mwPlanner.getPassword() : null;
//            if (mwPlanner != null) {
//                logger.info("MW Planner User: " + mwPlannerUserName + ", Password: " + mwPlannerPassword);
//                map.put(mwPlannerUserName, mwPlannerPassword);
//            }
//
//            // For MS Partner
//            SampleUserCredentials msPartner = credentials.stream()
//                    .filter(cred -> "MS Partner".equals(cred.getDoneBy()))
//                    .findFirst()
//                    .orElse(null);
//            String msPartnerUserName = msPartner != null ? msPartner.getUserName() : null;
//            String msPartnerPassword = msPartner != null ? msPartner.getPassword() : null;
//            if (msPartner != null) {
//                logger.info("MS Partner User: " + msPartnerUserName + ", Password: " + msPartnerPassword);
//                map.put(msPartnerUserName, msPartnerPassword);
//            }
//
//            // For Operation Team
//            SampleUserCredentials operationTeam = credentials.stream()
//                    .filter(cred -> "Operation Team".equals(cred.getDoneBy()))
//                    .findFirst()
//                    .orElse(null);
//            String operationTeamUserName = operationTeam != null ? operationTeam.getUserName() : null;
//            String operationTeamPassword = operationTeam != null ? operationTeam.getPassword() : null;
//            if (operationTeam != null) {
//                logger.info("Operation Team User: " + operationTeamUserName + ", Password: " + operationTeamPassword);
//                map.put(operationTeamUserName, operationTeamPassword);
//            }
//
//            //For INC Partner
//            SampleUserCredentials incPartner = credentials.stream()
//                    .filter(cred -> "INC Partner".equals(cred.getDoneBy()))
//                    .findFirst()
//                    .orElse(null);
//            String incPartnerUserName = incPartner != null ? incPartner.getUserName() : null;
//            String incPartnerPassword = incPartner != null ? incPartner.getPassword() : null;
//            if (incPartner != null) {
//                logger.info("INC Partner User: " + incPartnerUserName + ", Password: " + incPartnerPassword);
//                map.put(incPartnerUserName, incPartnerPassword);
//            }


            //From Here i gone take value for different team member
            List<SampleUserCredentials> credentials;
            //Fow MW Planner
            String mwPlannerUserName;
            String mwPlannerPassword;
            try{
                credentials = sampleUserCredentialsRepository.findByDoneByAndRole(userName, "MW Planner");
                logger.info("MW Planner User: " + credentials.get(0).getUserName() + ", Password: " + credentials.get(0).getPassword());
                mwPlannerUserName = credentials.get(0).getUserName();
                mwPlannerPassword = credentials.get(0).getPassword();
            }catch (Exception e){
                logger.info("Error in the credentials"+e);
                return ResponseEntity.badRequest().body("No Data Found of MW Planner user "+userName);
            }
//            //For MS Partner
            String msPartnerUserName;
            String msPartnerPassword;
            try{
                credentials = sampleUserCredentialsRepository.findByDoneByAndRole(userName, "MS Partner");
                logger.info("MS Partner User: " + credentials.get(0).getUserName() + ", Password: " + credentials.get(0).getPassword());
                msPartnerUserName = credentials.get(0).getUserName();
                msPartnerPassword = credentials.get(0).getPassword();
            }catch (Exception e){
                logger.info("Error in the credentials"+e);
                return ResponseEntity.badRequest().body("No Data Found of MS Partner user "+userName);
            }
            //For Operation Team
            String operationTeamUserName;
            String operationTeamPassword;
            try{
                credentials = sampleUserCredentialsRepository.findByDoneByAndRole(userName, "Operation Team");
                logger.info("Operation Team User: " + credentials.get(0).getUserName() + ", Password: " + credentials.get(0).getPassword());
                operationTeamUserName = credentials.get(0).getUserName();
                operationTeamPassword = credentials.get(0).getPassword();
            }catch (Exception e){
                logger.info("Error in the credentials"+e);
                return ResponseEntity.badRequest().body("No Data Found of Operation Team user "+userName);
            }
            //For INC Partner
            String incPartnerUserName;
            String incPartnerPassword;
            try{
                credentials = sampleUserCredentialsRepository.findByDoneByAndRole(userName, "I&C Partner");
                logger.info("INC Partner User: " + credentials.get(0).getUserName() + ", Password: " + credentials.get(0).getPassword());
                incPartnerUserName = credentials.get(0).getUserName();
                incPartnerPassword = credentials.get(0).getPassword();
            }catch (Exception e){
                logger.info("Error in the credentials"+e);
                return ResponseEntity.badRequest().body("No Data Found of INC Partner user "+userName);
            }
            Map<String, String> map = new HashMap<>();
            map.put(mwPlannerUserName, mwPlannerPassword);
            map.put(msPartnerUserName, msPartnerPassword);
            map.put(operationTeamUserName, operationTeamPassword);
            map.put(incPartnerUserName, incPartnerPassword);


            SampleUsersCredentials sampleUsersCredentials = new SampleUsersCredentials();
            sampleUsersCredentials.addSampleUser(map);
            MidsTest midsTest = new MidsTest();
            midsTest.setTestName(startPoint + " to " + endPoint);
            Date date = new Date();
            String dateString = String.format("%1$td-%1$tm-%1$tY", date);
            midsTest.setTestDate(dateString);
            midsTest.setStatus("In Progress");
            midsTest.setTestUser(userName);
            MWPlanner mw = new MWPlanner();
            try {
//                mw.MWPlannerLogin(mwPlannerUserName, mwPlannerPassword);
                long startTime = System.currentTimeMillis();
                mw.MWPlannerDPRTrack(startPoint, endPoint);
                long endTime = System.currentTimeMillis();
                long timeTakenSec = (endTime - startTime) / 1000;
                midsTest.setTimeTakenSec(String.valueOf(timeTakenSec));
                midsTest.setStatus("Success");
                midsTest.setComments("Test executed successfully");
                logger.info("Test executed successfully");
            } catch (Exception e) {
                e.printStackTrace();
                midsTest.setStatus("Failed");
                midsTest.setComments("Test execution failed: " + e.getMessage());
                return ResponseEntity.status(500).body("Error occurred while executing the test: " + e.getMessage());
            }
            allMidsTestRepository.save(midsTest);
            return ResponseEntity.ok("Successfully executed the test");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred while executing the test: " + e.getMessage());
        }finally{
            Base.tearDown();
        }
    }

    @PostMapping("/executeTests")
    public ResponseEntity<String> executeTests(@RequestBody Map<String, Object> payload) {
        try {
            String otp;
            boolean pageCheck=false;
            BasicTest basicTest = new BasicTest();
            if(otpCheck.equals("true")){
                if (payload == null || !payload.containsKey("user") || !payload.containsKey("otp")) {
                    return ResponseEntity.badRequest().body("Invalid payload: 'user' or 'otp' is required.");
                }
                otp=(String)payload.get("otp");
                logger.info("OTP Login Work");
                pageCheck=basicTest.isOtpLoginWork(otp);
            }else{
                if (payload == null || !payload.containsKey("user") ) {
                    return ResponseEntity.badRequest().body("Invalid payload: 'user' is required.");
                }
                pageCheck=true;
            }
            if(!pageCheck){
                return ResponseEntity.badRequest().body("Invalid payload: 'OTP' is required.");
            }
            String executionMode = (String) payload.get("executionMode");
//            String userName = (String) payload.get("userName");
//            String password = (String) payload.get("password");
            String testerName = (String) payload.getOrDefault("testerName", "Guest");
            if (executionMode == null || executionMode.isEmpty()) {
                return ResponseEntity.badRequest().body("Execution mode is required");
            }
            List<Map<String, Object>> tests=null;
            Map<String,Object> singleTest = Collections.emptyMap();
            if ("automatic".equals(executionMode)) {
                tests = (List<Map<String, Object>>) payload.get("tests");
            } else if ("manual".equals(executionMode)) {
                singleTest = (Map<String, Object>) payload.get("tests");
            }

            //Now i have to login as the user
//            try {
//                Login login = new Login();
//                login.Login(userName, password);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
//            }
            logger.info("Execution Mode: " + executionMode);

            if ("automatic".equals(executionMode) && tests != null) {
                for (Map<String, Object> test : tests) {
                    midsTestManagementFunction(test, testerName);
                }
            } else if ("manual".equals(executionMode)) {
                logger.info("Manual testing");
                midsTestManagementFunction(singleTest,testerName);
            }
            return ResponseEntity.ok("Execution Successfully Completed");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred while executing the tests: " + e.getMessage());
        }finally{
            Base.tearDown();
        }
    }

    public void midsTestManagementFunction(Map<String, Object> test,String testerName){
        try{
        Long testId = ((Number) test.get("id")).longValue();
        MidsTestManagement midsTestManagement = repository.findById(testId).orElse(null);
        String testName = (String) test.get("testName");
        Date date = new Date();
        String dateString = String.format("%1$td-%1$tm-%1$tY", date);

        if (midsTestManagement != null) {
            midsTestManagement.setExecutionStatus("In Progress");
            midsTestManagement.setLastExecutionDate(dateString);
            midsTestManagement.setLastExecutionUser(testerName);
            try {
                TestExecutor testExecutor = new TestExecutor();
                testExecutor.testExcute(testName, testerName, allMidsTestRepository);
                midsTestManagement.setExecutionStatus("Success");
                int successfulCount = midsTestManagement.getSuccessfulCount() != null ? midsTestManagement.getSuccessfulCount() + 1 : 1;
                midsTestManagement.setSuccessfulCount(successfulCount);
                logger.info("Test executed successfully: " + testName);
            } catch (Exception e) {
                int failedCount = midsTestManagement.getFailedCount() != null ? midsTestManagement.getFailedCount() + 1 : 1;
                midsTestManagement.setFailedCount(failedCount);
                midsTestManagement.setExecutionStatus("Failed");
            }
            repository.save(midsTestManagement);
        }
        }catch (Exception e){
            Base.tearDown();
        }
    }

}