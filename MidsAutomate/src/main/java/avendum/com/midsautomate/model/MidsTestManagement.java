package avendum.com.midsautomate.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name= "mids_test_management")
public class MidsTestManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String testName;
    private String pageName;
    private Integer successfulCount;
    private Integer failedCount;
    private String priority;
    private String lastExecutionUser;
    private LocalDateTime lastExecutionDate;
    private String executionStatus;
    private String DoneBy;
}