package com.avendum.midsautomate.selenium.TestGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UploadTestContainer {

    private String userName;
    private String departmentName;
    private String errorMessage;
    private Integer successCount;
    private Integer rowNumber;
    private String planUploadStatus;

}
