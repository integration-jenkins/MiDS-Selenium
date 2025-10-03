package com.avendum.midsautomate.selenium.seleniumconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@ToString
public class TrafficShiftingResourceCredentials {
    private String planId;
    private String circle;
    private String path;
    private String checkHold;
    private HashMap<String, List<String>> dismantleUser;
}
