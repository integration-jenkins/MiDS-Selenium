package avendum.com.midsauto;

public class AutomationUtils {
    public static String getTime() {
        return java.time.LocalTime.now().toString();
    }
}