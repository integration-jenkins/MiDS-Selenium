package com.avendum.midsautomate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "DISMANTLE_TEST_HISTORY")
@Getter @Setter @ToString @NoArgsConstructor
public class DismantleTestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TEST_ID")
    private String testId;

    @Column(name = "TEST_STATUS")
    private String testStatus;

    @Column(name = "TEST_TYPE")
    private String testType;

    @Column(name = "COMPLETION_TIME")
    private Long completionTime;

    @Column(name = "TOTAL_PASS_CASE")
    private Integer totalPassCase;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "VIEW_REPORT")
    private String viewReport;
}
