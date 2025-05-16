package avendum.com.midsautomate.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "basic_test_report")
public class BasicTestReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID basicTestId;

    @Column(name = "page_name")
    private String pageName;

    @Column(name = "last_time_taken_ms")
    private String lastTimeTakenMS;

    @Column(name = "average_time_taken_ms")
    private String averageTimeTakenMS;

    private String comments;

    @Column(name = "last_execution_date")
    private LocalDateTime lastExecutionDate;

    @Column(name = "last_execution_status")
    private String lastExecutionStatus;

    @Column(name = "last_execution_by")
    private String lastExecutionBy;

    @Column(name = "no_of_test")
    private int noOfTest;
}