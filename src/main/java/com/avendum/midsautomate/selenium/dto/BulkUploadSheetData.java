package com.avendum.midsautomate.selenium.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BulkUploadSheetData {
    private String columnName;
    private List<String> value;
    private String columnType;
    private Integer rowNumber;
}
