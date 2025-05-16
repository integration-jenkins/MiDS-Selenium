package avendum.com.midsautomate.util;

import avendum.com.midsauto.utils.BasicTest;
import avendum.com.midsautomate.model.BasicTestReport;

public class BasicTestGenerate {
   private String pages[]={"Dashboard Page","Deployment Dashboard","MW Plan Tracking Page","LB Report Page","DPR Report Page","UBR LB Report Page","RAN MW Page","POP Info Page","Atom Summary Page","WAN IP Page","SOFT AT Page","Deploy Assignment Page"};
   public void startTest() throws InterruptedException {
       BasicTestReport basicTestReport = new BasicTestReport();
       BasicTest basicTest = new BasicTest();
         for (String page : pages) {
              basicTestReport.setPageName(page);
              // Call the method to check if the page is rendering correctly
              boolean isRenderingCorrect = basicTest.isPageRenderingCorrectly(page);
              if (isRenderingCorrect) {
                basicTestReport.setLastExecutionStatus("Success");
              } else {
                basicTestReport.setLastExecutionStatus("Failed");
              }
         }
   }

}
