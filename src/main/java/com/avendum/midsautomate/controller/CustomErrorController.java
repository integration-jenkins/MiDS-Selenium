package com.avendum.midsautomate.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

//    @RequestMapping("/error")
//    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", request.getAttribute("jakarta.servlet.error.status_code"));
//        response.put("error", request.getAttribute("jakarta.servlet.error.message"));
//        return new ResponseEntity<>(response, HttpStatus.valueOf(
//                (Integer) request.getAttribute("jakarta.servlet.error.status_code")
//        ));
//    }
@RequestMapping("/error")
public String handleError() {
    // For SPA, forward to index.html to let React handle the error or route
    return "Not Able to Render Page";
}
}