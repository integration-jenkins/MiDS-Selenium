package com.avendum.midsautomate.util;


import com.avendum.midsautomate.model.SampleUserCredentials;
import com.avendum.midsautomate.repository.SampleUserCredentialsRepository;
import com.avendum.midsautomate.selenium.seleniumconfig.Base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MapedSampleUser {

    private static final Logger logger = Logger.getLogger(MapedSampleUser.class.getName());
    public Map<String, String> getMapUserDetails(SampleUserCredentialsRepository sampleUserCredentialsRepository) {
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
            logger.info("MW Planner User: " + mwPlannerUserName + ", Password: " + mwPlannerPassword);
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
           logger.info("MS Partner User: " + msPartnerUserName + ", Password: " + msPartnerPassword);
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
            logger.info("Operation Team User: " + operationTeamUserName + ", Password: " + operationTeamPassword);
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
            logger.info("INC Partner User: " + incPartnerUserName + ", Password: " + incPartnerPassword);
            map.put(incPartnerUserName ,incPartnerPassword);
        }
        return map;
    }
}
