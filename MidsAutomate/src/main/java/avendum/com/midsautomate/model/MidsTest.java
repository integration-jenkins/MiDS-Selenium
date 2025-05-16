package avendum.com.midsautomate.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="mids_test")
public class MidsTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;
    private String testName;
    private String testDate;
    private String status;
    private String testUser;
    private String timeTakenSec;
    private String comments;
}
