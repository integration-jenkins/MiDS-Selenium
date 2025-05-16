package avendum.com.midsautomate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SampleUserCredentials {
    @Id
    private String userName;
    private String password;
    private String doneBy;
    private String circle;
}