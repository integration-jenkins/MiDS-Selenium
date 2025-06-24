package com.avendum.midsautomate.selenium.seleniumconfig;

import java.util.HashMap;
import java.util.Map;

public class SampleUsersCredentials {
    public Map<String,String> sampleUsers=new HashMap<>();
    public void addSampleUser(Map<String,String> map){
        this.sampleUsers=map;
    }
    public Map<String,String> getSampleUser(){
        return this.sampleUsers;
    }
}
