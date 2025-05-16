//package avendum.com.midsautomate;
//
//import avendum.com.midsauto.AutomationUtils;
//
//import avendum.com.midsauto.Launch;
//import lombok.Data;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/tests")
//public class TestController {
////    private String userId;
////    private String startPoint;
////    private String endPoint;
////    private String executionTime;
////    private String status;
//
//    @PostMapping
//    public ResponseEntity<String> saveTestData(@RequestBody TestData testData) {
//        // Process the received data (e.g., save to the database)
//        System.out.println("Received data: " + testData);
//        fun(testData.getStartPoint(), testData.getEndPoint());
//        return ResponseEntity.ok("Data received successfully");
//    }
//
//    // Define a DTO class for the incoming data
//    @Data
//    public static class TestData {
//        private String userId;
//        private String startPoint;
//        private String endPoint;
//        private String executionTime;
//        private String status;
//    }
//
//    //Calling Launch Method from MidsAuto Selenium Project
//    public  void fun(String startPoint,String endPoint){
//         Launch.dprPage(startPoint,endPoint);
//    }
//}