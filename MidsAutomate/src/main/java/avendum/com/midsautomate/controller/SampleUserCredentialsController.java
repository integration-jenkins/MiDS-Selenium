package avendum.com.midsautomate.controller;

import avendum.com.midsautomate.model.SampleUserCredentials;
import avendum.com.midsautomate.repository.SampleUserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sampleUserCredentials")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SampleUserCredentialsController {
    @Autowired
    private SampleUserCredentialsRepository sampleUserCredentialsRepository;

    //Returning the sampleUserName and Password according to the role from SampleUseCredenetional Table i mean to stated SampleUserCredentials findByRole(String role); fro Repository
    @PostMapping("/getUserCredentials")
    public List<String> getUserCredentials(@RequestBody Map<String, String> request) {
        List<String> userCredentials = new ArrayList<>();
        String doneBy = request.get("doneBy");
//        System.out.println("Done By: " + doneBy);
        List<SampleUserCredentials> sampleUserCredentialsList = sampleUserCredentialsRepository.findByDoneBy(doneBy);
//        System.out.println("SampleUserCredentials List: " + sampleUserCredentialsList);
        for (SampleUserCredentials sampleUserCredentials : sampleUserCredentialsList) {
            userCredentials.add(sampleUserCredentials.getUserName());
            userCredentials.add(sampleUserCredentials.getPassword());
        }
        return userCredentials;
    }

}
