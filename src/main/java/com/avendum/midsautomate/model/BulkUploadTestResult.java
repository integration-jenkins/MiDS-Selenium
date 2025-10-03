package com.avendum.midsautomate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "BULK_UPLOAD_TEST_RESULT")
@Getter @Setter @ToString
public class BulkUploadTestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long Id;

    @Column(name = "TEST_SECTION")
    private String testSection;


}
