package com.avendum.midsautomate.selenium.tests;

import com.avendum.midsautomate.repository.AllMidsTestRepository;
import com.avendum.midsautomate.selenium.seleniumconfig.Base;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TestExecutor {
    private WebDriver driver;
public void testExcute(String name, String testerName, AllMidsTestRepository allMidsTestRepository){
    driver= Base.getDriver();
   if(name.equalsIgnoreCase("LB Success Report Upload")){
       LBReportTest lb= new LBReportTest(allMidsTestRepository);
       lb.successUpload(testerName);
   }else if(name.equalsIgnoreCase("LB Wrong Report Upload")){
       LBReportTest lb= new LBReportTest(allMidsTestRepository);
       lb.wrongFileUpload(testerName);
   }else if(name.equalsIgnoreCase("LB Page Functionality Check")){
       //Lb report upload working or not page get render from that page feature forking or not
       LBReportTest lb= new LBReportTest(allMidsTestRepository);
         lb.lbReportPageFunction(testerName);
   }else if(name.equalsIgnoreCase("LB MW Link Testing")){
       LBReportTest lb= new LBReportTest(allMidsTestRepository);


   }else if(name.equalsIgnoreCase("Soft AT Reject")){

   }else if(name.equalsIgnoreCase("Valid Count")){

   }else if(name.equalsIgnoreCase("Soft AT Accept")){

   }

}

}
