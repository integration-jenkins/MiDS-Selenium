package com.avendum.midsautomate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "DISMANTLE_TEST_RESULT")
public class DismantleTestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long Id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "DEPARTMENT_NAME")
    private String departmentName;

    @Column(name = "PLAN_STATUS")
    private String dismantleStatus;

    @Column(name = "TEST_STATUS")
    private String testStatus;

    @Column(name = "REMARKS")
    private String remark;

    @Column(name = "TEST_DATE")
    private LocalDate testDate;

    @Column(name = "TEST_ID")
    private String testId;

    @Column(name = "VIEW_REPORT")
    private String viewReport;
}
