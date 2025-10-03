package com.avendum.midsautomate.selenium.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@ToString
public class DismantleDataResource {

    private String hopId;
    private String circle;
    private String partnerAllocationName;
    private HashMap<String, List<String>> dismantleUser = new HashMap<>();
    private String filePath;
    private String department;
    private Integer passCount;

}
