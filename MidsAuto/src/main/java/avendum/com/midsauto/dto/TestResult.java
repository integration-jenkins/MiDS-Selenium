package avendum.com.midsauto.dto;

import lombok.Data;

@Data
public class TestResult {
    private Long testId;
    private String testName;
    private boolean success;
    private String executionStatus;
    private String errorMessage;
}
