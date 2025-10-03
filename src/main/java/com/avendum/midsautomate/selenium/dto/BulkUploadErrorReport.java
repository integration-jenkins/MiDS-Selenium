package com.avendum.midsautomate.selenium.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BulkUploadErrorReport {
    private String columnName;
    private String errorMessage;
    private Integer rowNumber;
}
