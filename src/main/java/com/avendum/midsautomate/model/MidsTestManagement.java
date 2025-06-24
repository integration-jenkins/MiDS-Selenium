package com.avendum.midsautomate.model;

import lombok.Data;

import javax.persistence.*;

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
    private String lastExecutionDate;
    private String executionStatus;
    private String executionType;
    private String DoneBy;
}