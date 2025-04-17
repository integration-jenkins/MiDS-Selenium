package avendum.com.midsautomate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MidsAutomateApplication {
    public static void main(String[] args) {
//        SpringApplication.run(MidsAutomateApplication.class, args);
        ConfigurableApplicationContext context =
                SpringApplication.run(MidsAutomateApplication.class, args);
        //Call Launh Method from MidsAuto Selenium Project


    }
}
