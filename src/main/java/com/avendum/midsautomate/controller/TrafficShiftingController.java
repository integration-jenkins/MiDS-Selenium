package com.avendum.midsautomate.controller;

import com.avendum.midsautomate.enums.TestStatus;
import com.avendum.midsautomate.enums.TrafficShiftingRemark;
import com.avendum.midsautomate.model.*;
import com.avendum.midsautomate.repository.TestUserRepository;
import com.avendum.midsautomate.repository.TrafficShiftingResultDataRepository;
import com.avendum.midsautomate.repository.TrafficShiftingTestHistoryRepository;
import com.avendum.midsautomate.selenium.dto.BulkRequestData;
import com.avendum.midsautomate.selenium.dto.BulkUploadErrorReport;
import com.avendum.midsautomate.selenium.seleniumconfig.TrafficShiftingResourceCredentials;
import com.avendum.midsautomate.util.TrafficShiftingUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.avendum.midsautomate.util.TrafficShiftingUtility.*;

@RestController
@RequestMapping("/api/trafficShifting")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrafficShiftingController {

    private static final Logger log = LoggerFactory.getLogger(TrafficShiftingController.class);

    @Value("${window.driver.path}")
    private String driverPath;

    @Value("${window.browser.binary}")
    private String binaryPath;

    @Value("${mids.project.url}")
    private String projectUrl;

    @Autowired
    private TrafficShiftingTestHistoryRepository trafficShiftingTestHistoryRepository;

    @Autowired
    private TrafficShiftingResultDataRepository trafficShiftingResultDataRepository;

    @Autowired
    private TestUserRepository testUserRepository;

    @PostMapping("/workflow")
    public ResponseEntity<TrafficShiftingTestHistory> testDismantleFlow(@RequestBody TrafficShiftingResourceCredentials resource) {
        TrafficShiftingTestHistory history = new TrafficShiftingTestHistory();
        List<TrafficShiftingResultData> trafficShiftingResultData = new ArrayList<>();

        log.info("Resource = " + resource);
        history.setTestType("WORK_FLOW_TEST");
        history.setTestDate(LocalDate.now());

        try {
            // align the user for
            HashMap<String, List<String>> userMap = new HashMap<>();
            Map<String, List<String>> userDetails = resource.getDismantleUser();
            for (int i = 0; i < userDetails.get("name").size(); i++) {
                String dep = userDetails.get("department").get(i);
                String name = userDetails.get("name").get(i);
                String password = userDetails.get("password").get(i);

                userMap.put(dep, Arrays.asList(name, password));
            }

            log.info("All Test Users = " + userMap);

            history.setTrafficShiftingResultData(trafficShiftingResultData);
            String uniqueKey = generateUniquePlanId(trafficShiftingTestHistoryRepository, resource.getPlanId());
            history.setUniquePlanId(uniqueKey);
            resource.setDismantleUser(userMap);

            long start = System.currentTimeMillis();
            boolean test = false;
            try {
                test = testTSWorkFlow(
                        trafficShiftingResultData, resource, driverPath, binaryPath, uniqueKey, projectUrl
                );
            } catch (Exception e) {
                e.printStackTrace();
                TrafficShiftingResultData entry = new TrafficShiftingResultData();
                entry.setDepartment("Not Assigned");
                entry.setUserName("Not Assigned");
                entry.setUniquePlanId(uniqueKey);
                entry.setTestStatus(TestStatus.FAILED.name());
                entry.setPlanId(resource.getPlanId());
                entry.setPlanStatus(TestStatus.FAILED.name());
                entry.setRemark("Issue in System");
                trafficShiftingResultData.add(entry);
            }

            long end = System.currentTimeMillis();
            long totalExecutionTime = (long) (Math.abs(end - start) / 1000.0);

            log.info("Traffic Shifting plan workflow test at " + totalExecutionTime + "sec");
            history.setCompletionTime(totalExecutionTime);
            history.setTestStatus(
                    test ? TestStatus.PASSED.name() : TestStatus.FAILED.name()
            );
            history.setRemark(
                    test ? TrafficShiftingRemark.ALL_TEST_PASS.getStatus() : TrafficShiftingRemark.TEST_FAILED.getStatus()
            );
            history.setTotalPassCase(
                    test ? trafficShiftingResultData.size() : trafficShiftingResultData.size() - 1
            );
            history.setTestId(resource.getPlanId());

            trafficShiftingTestHistoryRepository.save(history);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(history, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @PostMapping("/bulkUploadTest")
    public ResponseEntity<TrafficShiftingTestHistory> getTrafficShiftingBulkUploadTestHistory(
            @RequestBody TrafficShiftingResourceCredentials resource
    ){
        TrafficShiftingTestHistory history = new TrafficShiftingTestHistory();
        List<TrafficShiftingResultData> trafficShiftingResultData = new ArrayList<>();

        log.info("Resource = " + resource);

        try {
            // align the user for
            HashMap<String, List<String>> userMap = new HashMap<>();
            Map<String, List<String>> userDetails = resource.getDismantleUser();
            String department = "";
            history.setTestType("BULK_UPLOAD_TEST");
            history.setTestDate(LocalDate.now());

            log.info("User Details = "+userDetails);

            for (int i = 0; i < userDetails.get("name").size(); i++) {
                String dep = userDetails.get("department").get(i);
                String name = userDetails.get("name").get(i);
                String password = userDetails.get("password").get(i);

                userMap.put(dep, Arrays.asList(name, password));
                department = dep;
            }

            log.info("All Test Users = " + userMap);

            history.setTrafficShiftingResultData(trafficShiftingResultData);
            String uniqueKey = generateUniquePlanId(trafficShiftingTestHistoryRepository, resource.getPlanId());
            history.setUniquePlanId(uniqueKey);

            resource.setDismantleUser(userMap);

            long start = System.currentTimeMillis();
            boolean test = false;
            try {
                test = bulkUploadTest(
                        trafficShiftingResultData, resource, driverPath, binaryPath, uniqueKey, projectUrl,department
                );
            } catch (Exception e) {
                e.printStackTrace();
                TrafficShiftingResultData entry = new TrafficShiftingResultData();
                entry.setDepartment("Not Assigned");
                entry.setUserName("Not Assigned");
                entry.setUniquePlanId(uniqueKey);
                entry.setTestStatus(TestStatus.FAILED.name());
                entry.setPlanId(resource.getCircle()+uniqueKey);
                entry.setPlanStatus(TestStatus.FAILED.name());
                entry.setRemark("Issue in System");
                trafficShiftingResultData.add(entry);
            }

            long end = System.currentTimeMillis();
            long totalExecutionTime = (long) (Math.abs(end - start) / 1000.0);

            log.info("Traffic Shifting Bulk Upload test at " + totalExecutionTime + "sec");
            history.setCompletionTime(totalExecutionTime);
            history.setTestStatus(
                    test ? TestStatus.PASSED.name() : TestStatus.FAILED.name()
            );
            history.setRemark(
                    test ? TrafficShiftingRemark.ALL_TEST_PASS.getStatus() : TrafficShiftingRemark.TEST_FAILED.getStatus()
            );
            history.setTotalPassCase(
                    test ? trafficShiftingResultData.size() : trafficShiftingResultData.size() - 1
            );
            history.setTestId(resource.getCircle()+uniqueKey);

            trafficShiftingTestHistoryRepository.save(history);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(history, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(history, HttpStatus.OK);

    }

    @GetMapping("/testResult")
    public ResponseEntity<List<TrafficShiftingTestHistory>> getTrafficShiftingTestHistoryData() {
        List<TrafficShiftingTestHistory> result = new ArrayList<>();
        try {
            List<TrafficShiftingTestHistory> trafficShiftingTestHistories = trafficShiftingTestHistoryRepository.findAll();
            List<TrafficShiftingResultData> trafficShiftingResultData = trafficShiftingResultDataRepository.findAll();

            Map<String, List<TrafficShiftingResultData>> tsDataMap = trafficShiftingResultData.stream()
                    .filter(i -> i != null && i.getUniquePlanId() != null)
                    .collect(Collectors.groupingBy(TrafficShiftingResultData::getUniquePlanId));

            for (TrafficShiftingTestHistory historys : trafficShiftingTestHistories) {
                List<TrafficShiftingResultData> children =
                        tsDataMap.getOrDefault(historys.getUniquePlanId(), Collections.emptyList());
                historys.setTrafficShiftingResultData(children);
                result.add(historys);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/testCode")
    public ResponseEntity<List<TestUser>> testApiCode() {
        List<TestUser> result = new ArrayList<>();
        try {

            result = testUserRepository.findAll();

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<TestUser>> getUserDetails() {
        List<TestUser> result = new ArrayList<>();
        try {

            result = testUserRepository.findAll();

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/setUser")
    public ResponseEntity<String> setUserDetails(@RequestBody List<TestUser> users) {
        String resp = "User Details Not Inserted";
        try {
            List<TestUser> currentUsers = testUserRepository.findAll();

            Set<String> existingKeys = currentUsers.stream()
                    .map(u -> u.getDepartment() + "|" + u.getName())
                    .collect(Collectors.toSet());

            List<TestUser> uniqueNewUsers = new ArrayList<>();
            List<TestUser> duplicates = new ArrayList<>();

            for (TestUser newUser : users) {
                String key = newUser.getDepartment() + "|" + newUser.getName();
                if (existingKeys.contains(key)) {
                    duplicates.add(newUser);
                } else {
                    uniqueNewUsers.add(newUser);
                    existingKeys.add(key); // prevent duplicates in same request
                }
            }

            if (!uniqueNewUsers.isEmpty()) {
                testUserRepository.saveAll(uniqueNewUsers);
            }

            if (!duplicates.isEmpty()) {
                resp = "Some users not inserted (duplicates found): "
                        + duplicates.size() + " duplicate(s)";
            } else if (!uniqueNewUsers.isEmpty()) {
                resp = "User Details Inserted";
            } else {
                resp = "No new users inserted (all were duplicates)";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("/testBulkUploadSheet")
    public ResponseEntity<List<BulkUploadErrorReport>> getBulkUploadSheet(
            @RequestBody BulkRequestData data) {

        List<BulkUploadErrorReport> resp = new ArrayList<>();
        try {
            resp.addAll(TrafficShiftingUtility.validateBulkUploadSheet(data.getPath(), data.getCircle()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


}
