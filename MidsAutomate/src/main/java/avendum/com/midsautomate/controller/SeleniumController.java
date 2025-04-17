package avendum.com.midsautomate.controller;
//
import avendum.com.midsautomate.util.JwtUtil;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/selenium")
//public class SeleniumController {
//
//    @PostMapping("/run-test")
//    public ResponseEntity<String> runSeleniumTest(@RequestBody TestRequest request) {
//        try {
//            // Call the Launch class from MidsAuto
//            if ("MWPlanner".equals(request.getTestName())) {
//                Launch.MWPlanner(request.getParams().get("username"), request.getParams().get("password"));
//            } else if ("MSPartner".equals(request.getTestName())) {
//                Launch.MSPartner(request.getParams().get("username"), request.getParams().get("password"));
//            } else {
//                return ResponseEntity.badRequest().body("Invalid test name: " + request.getTestName());
//            }
//            return ResponseEntity.ok("Selenium test executed successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//        }
//    }
//}