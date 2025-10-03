package com.avendum.midsautomate.controller;

import com.avendum.midsautomate.model.DismantleTestHistory;
import com.avendum.midsautomate.model.DismantleTestResult;
import com.avendum.midsautomate.repository.DismantleTestHistoryRepository;
import com.avendum.midsautomate.repository.DismantleTestResultRepository;
import com.avendum.midsautomate.selenium.dto.DismantleDataResource;
import com.avendum.midsautomate.util.DismantleUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import static com.avendum.midsautomate.util.DismantleUtility.bulkUploadTesting;

@RestController
@RequestMapping("/api/dismantleTest")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DismantleController {

    @Autowired
    private DismantleTestResultRepository dismantleTestResultRepository;

    @Autowired
    private DismantleTestHistoryRepository dismantleTestHistoryRepository;

    @Value("${window.driver.path}")
    private String driverPath;

    @Value("${window.browser.binary}")
    private String binaryPath;

    @PostMapping("/workflow")
    public ResponseEntity<List<DismantleTestResult>> testDismantleFlow(@RequestBody DismantleDataResource resource) {
        String response = "";
        List<DismantleTestResult> testResults = new ArrayList<>();
        try {
            DismantleTestHistory dismantleHistory = new DismantleTestHistory();

            long start = System.currentTimeMillis();
            boolean workFlowTested = DismantleUtility.testOverallPlanExecutionFlow(
                    resource, testResults, dismantleHistory, driverPath, binaryPath);
            long end = System.currentTimeMillis();

            long totalExecutionTime = Math.abs(end - start) / 100;

            dismantleHistory.setTestId(resource.getHopId());
            dismantleHistory.setTestStatus(workFlowTested ? "ALL TEST PASSED" : "TEST FAILED");
            dismantleHistory.setTotalPassCase(testResults.size() - 1);
            dismantleHistory.setCompletionTime(totalExecutionTime);

            response = "DISMANTLE WORKFLOW TESTING COMPLETE";
            System.out.println("Records = " + testResults);
            System.out.println("History = " + dismantleHistory);

            String outputRemark = testResults.stream()
                    .filter(result -> "FAIL".equalsIgnoreCase(result.getTestStatus()))
                    .map(DismantleTestResult::getRemark)
                    .findFirst()
                    .orElse("No Issue, System in working condition");

            long passCount = testResults.stream()
                    .filter(result -> "PASS".equalsIgnoreCase(result.getTestStatus()))
                    .count();

            String linkedId = resource.getHopId()+passCount;

            testResults.forEach(i -> i.setViewReport(linkedId));

            dismantleTestResultRepository.saveAll(testResults);

            dismantleHistory.setRemark(outputRemark);
            dismantleHistory.setTotalPassCase((int)passCount);
            dismantleHistory.setTestType("WORK FLOW TEST");
            dismantleHistory.setViewReport(linkedId);

            dismantleTestHistoryRepository.save(dismantleHistory);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(testResults,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(testResults, HttpStatus.OK);
    }

    @PostMapping("/bulkTest")
    public ResponseEntity<List<DismantleTestResult>> bulkUploadTest(@RequestBody DismantleDataResource resource) {
        List<DismantleTestResult> testResults = new ArrayList<>();
        try {
            DismantleTestHistory dismantleHistory = new DismantleTestHistory();

            long start = System.currentTimeMillis();
            boolean workFlowTested =  bulkUploadTesting(resource,testResults,resource.getDepartment(),driverPath, binaryPath);
            long end = System.currentTimeMillis();

            long totalExecutionTime = Math.abs(end - start)/100;

            dismantleHistory.setTestId(resource.getHopId());
            dismantleHistory.setTestStatus(workFlowTested ? "ALL TEST PASSED" : "TEST FAILED");
            dismantleHistory.setCompletionTime(totalExecutionTime);

            System.out.println("Records = " + testResults);
            System.out.println("History = " + dismantleHistory);

            String outputRemark = testResults.stream()
                    .filter(result -> "FAIL".equalsIgnoreCase(result.getTestStatus()))
                    .map(DismantleTestResult::getRemark)
                    .findFirst()
                    .orElse("No Issue, Bulk Upload in working condition");

            long passCount = testResults.stream()
                    .filter(result -> "PASS".equalsIgnoreCase(result.getTestStatus()))
                    .count();

            String linkedId = resource.getHopId()+passCount;

            testResults.forEach(i -> i.setViewReport(linkedId));

            dismantleHistory.setRemark(outputRemark);
            dismantleHistory.setTotalPassCase((int)passCount);
            dismantleHistory.setTestType("Bulk Upload Test");
            dismantleHistory.setViewReport(linkedId);

            dismantleTestResultRepository.saveAll(testResults);
            dismantleTestHistoryRepository.save(dismantleHistory);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(testResults,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(testResults, HttpStatus.OK);
    }

    @GetMapping("testResult")
    public ResponseEntity<List<DismantleTestHistory>> getDismantleTestHistoryData() {
        List<DismantleTestHistory> result = new ArrayList<>();
        try{
            result.addAll(dismantleTestHistoryRepository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/viewReport")
    public ResponseEntity<List<DismantleTestResult>> getDismantleTestResultData(
            @RequestParam String viewReportId) {
        List<DismantleTestResult> result;
        try {
            result = dismantleTestResultRepository.findAllByViewReport(viewReportId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




}
