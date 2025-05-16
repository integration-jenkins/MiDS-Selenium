package avendum.com.midsautomate.controller;

import avendum.com.midsauto.Start;
import avendum.com.midsauto.config.SampleUsersCredentials;
import avendum.com.midsauto.controller.MWPlanner;
import avendum.com.midsautomate.model.MidsTest;
import avendum.com.midsautomate.model.MidsTestManagement;
import avendum.com.midsautomate.model.SampleUserCredentials;
import avendum.com.midsautomate.repository.AllMidsTestRepository;
import avendum.com.midsautomate.repository.MidsTestRepository;
import avendum.com.midsautomate.repository.SampleUserCredentialsRepository;
import io.cucumber.java.it.Data;
import io.cucumber.java.it.Ma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/midstests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MidsTestController {
    @Autowired
    private MidsTestRepository repository;

    @Autowired
    private AllMidsTestRepository allMidsTestRepository;
    @Autowired
    private SampleUserCredentialsRepository sampleUserCredentialsRepository;

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
        String startPoint=payload.get("startPoint");
        String endPoint=payload.get("endPoint");
        String userName=payload.get("userName");
        // Fetch user credentials for different users from the SampleUserCredentials table
        List<SampleUserCredentials> credentials = sampleUserCredentialsRepository.findAll();
        Map<String,String> map=new HashMap<>();
        // For MW Planner
        SampleUserCredentials mwPlanner = credentials.stream()
                .filter(cred -> "MW Planner".equals(cred.getDoneBy()))
                .findFirst()
                .orElse(null);
        String mwPlannerUserName = mwPlanner != null ? mwPlanner.getUserName() : null;
        String mwPlannerPassword = mwPlanner != null ? mwPlanner.getPassword() : null;
        if(mwPlanner!=null){
            System.out.println("MW Planner User: " + mwPlannerUserName + ", Password: " + mwPlannerPassword);
            map.put(mwPlannerUserName ,mwPlannerPassword);
        }

        // For MS Partner
        SampleUserCredentials msPartner = credentials.stream()
                .filter(cred -> "MS Partner".equals(cred.getDoneBy()))
                .findFirst()
                .orElse(null);
        String msPartnerUserName = msPartner != null ? msPartner.getUserName() : null;
        String msPartnerPassword = msPartner != null ? msPartner.getPassword() : null;
        if(msPartner!=null) {
            System.out.println("MS Partner User: " + msPartnerUserName + ", Password: " + msPartnerPassword);
            map.put(msPartnerUserName ,msPartnerPassword);
        }

        // For Operation Team
        SampleUserCredentials operationTeam = credentials.stream()
                .filter(cred -> "Operation Team".equals(cred.getDoneBy()))
                .findFirst()
                .orElse(null);
        String operationTeamUserName = operationTeam != null ? operationTeam.getUserName() : null;
        String operationTeamPassword = operationTeam != null ? operationTeam.getPassword() : null;
        if(operationTeam!=null) {
            System.out.println("Operation Team User: " + operationTeamUserName + ", Password: " + operationTeamPassword);
            map.put(operationTeamUserName ,operationTeamPassword);
        }

        //For INC Partner
        SampleUserCredentials incPartner = credentials.stream()
                .filter(cred -> "INC Partner".equals(cred.getDoneBy()))
                .findFirst()
                .orElse(null);
        String incPartnerUserName = incPartner != null ? incPartner.getUserName() : null;
        String incPartnerPassword = incPartner != null ? incPartner.getPassword() : null;
        if(incPartner!=null) {
            System.out.println("INC Partner User: " + incPartnerUserName + ", Password: " + incPartnerPassword);
            map.put(incPartnerUserName ,incPartnerPassword);
        }
        SampleUsersCredentials sampleUsersCredentials = new SampleUsersCredentials();
        sampleUsersCredentials.addSampleUser(map);
        MidsTest midsTest = new MidsTest();
        midsTest.setTestName(startPoint+" to "+endPoint);
        Date date = new Date();
       String dateString = String.format("%1$td-%1$tm-%1$tY", date);
        midsTest.setTestDate(dateString);
        midsTest.setStatus("In Progress");
        midsTest.setTestUser(userName);
        MWPlanner mw=new MWPlanner();
        try {
            mw.MWPlannerLogin(mwPlannerUserName,mwPlannerPassword);
            long startTime = System.currentTimeMillis();
            mw.MWPlannerDPRTrack(startPoint, endPoint);
            long endTime = System.currentTimeMillis();
            long timeTakenSec = (endTime - startTime) / 1000;
            midsTest.setTimeTakenSec(String.valueOf(timeTakenSec));
            midsTest.setStatus("Success");
            midsTest.setComments("Test executed successfully");
            System.out.println("Test executed successfully");
        }catch (Exception e){
            e.printStackTrace();
            midsTest.setStatus("Failed");
            midsTest.setComments("Test execution failed: " + e.getMessage());
            return ResponseEntity.status(500).body("Error occurred while executing the test: " + e.getMessage());
        }
        allMidsTestRepository.save(midsTest);
        return ResponseEntity.ok("Successfully executed the test");
    }

    @PostMapping("/executeTests")
    public ResponseEntity<String> executeTests(@RequestBody Map<String, Object> payload) {
        String executionMode = (String) payload.get("executionMode");
        String userName=(String) payload.get("userName");
        String password=(String) payload.get("password");
        List<Map<String, Object>> tests = (List<Map<String, Object>>) payload.get("tests");

        //Now i have to login as the user
        Start start = new Start();
        try {
            start.UserLogin(userName, password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Execution Mode: " + executionMode);
        System.out.println("Tests: " + tests);

        if(executionMode.equals("automatic")){
           for(Map<String, Object> test : tests) {
                Long testId = ((Number) test.get("id")).longValue();
                String testName = (String) test.get("name");
                MidsTestManagement midsTestManagement = repository.findById(testId).orElse(null);
                if (midsTestManagement != null) {
                    midsTestManagement.setExecutionStatus("In Progress");
                    repository.save(midsTestManagement);
                    //Now call the test executor of selenium project
                    //In selenium we pass the testName in the Testutils class which according to the class perform the testing

                }
            }
        }
        return ResponseEntity.ok("Execution started successfully");
    }
}