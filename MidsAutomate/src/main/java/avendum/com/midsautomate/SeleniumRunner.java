package avendum.com.midsautomate;
 import avendum.com.midsauto.AutomationUtils;
public class SeleniumRunner {
    public static void main(String ar[]){
        String time = AutomationUtils.getTime();
        System.out.println("Started at: " + time);
    }
}
