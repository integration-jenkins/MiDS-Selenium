package avendum.com.midsautomate.controller;

import avendum.com.midsautomate.model.SampleUserCredentials;
import avendum.com.midsautomate.repository.SampleUserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    //Adding the user in DB
    @PostMapping("/addUser")
    public ResponseEntity<?> addUsers(@RequestBody List<SampleUserCredentials> credentialsList) {
        try {
            // Validate input
            if (credentialsList == null || credentialsList.isEmpty()) {
                return ResponseEntity.badRequest().body("No credentials provided");
            }

            List<SampleUserCredentials> savedCredentials = new ArrayList<>();

            for (SampleUserCredentials cred : credentialsList) {
                // Validate required fields
                if (cred.getUserName() == null || cred.getUserName().isEmpty() ||
                        cred.getPassword() == null || cred.getPassword().isEmpty() ||
                        cred.getDoneBy() == null || cred.getDoneBy().isEmpty()||
                        cred.getCircle() == null || cred.getCircle().isEmpty()) {
                    return ResponseEntity.badRequest().body("Missing required fields in one or more credentials");
                }

                // Check for existing credential with the same doneBy
               List<SampleUserCredentials> existingCredentials = sampleUserCredentialsRepository.findByDoneBy(cred.getDoneBy());

                // Delete if exists to maintain unique doneBy constraint
                existingCredentials.forEach(sampleUserCredentialsRepository::delete);

                // Save new credential
                SampleUserCredentials savedCred = sampleUserCredentialsRepository.save(cred);
                savedCredentials.add(savedCred);
            }

            return ResponseEntity.ok(savedCredentials);

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate userName or doneBy detected: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request: " + e.getMessage());
        }
    }
}