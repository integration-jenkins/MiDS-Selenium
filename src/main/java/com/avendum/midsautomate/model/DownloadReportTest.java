package com.avendum.midsautomate.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

    @Data
    @Entity
    @Table(name = "download_report_test")
    public class DownloadReportTest {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID downloadReportTestID;

        @Column(name = "report_name", nullable = false)
        private String reportName;

        @Column(name = "last_time_taken_ms")
        private String lastTimeTakenMS;

        @Column(name = "average_time_taken_ms")
        private String averageTimeTakenMS;

        @Column(name = "download_report_test_status")
        private String downloadReportTestStatus;

        @Column(name = "downloaded_report_path")
        private String downloadedReportPath;

        private String comments;

        @Column(name = "last_execution_date")
        private LocalDateTime lastExecutionDate;

        @Column(name = "last_execution_by")
        private String lastExecutionBy;

        @Column(name = "no_of_test")
        private int noOfTest;

        @Column(name = "no_of_success_test_count")
        private Integer noOfSuccessTestCount;
    }