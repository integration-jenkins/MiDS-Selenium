package avendum.com.midsauto.tests;

import avendum.com.midsauto.config.Base;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class TestExecutor {
    private WebDriver driver;
public List<String> testExcute(String name){
    driver=Base.getDriver();
    List<String> testResults = new ArrayList<>();
    try {

       if(name.equals("PHY AT Manual Rejection")){
           System.out.println("Executing test: " + name);

       }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return testResults;
}

}
