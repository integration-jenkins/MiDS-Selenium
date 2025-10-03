package com.avendum.midsautomate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "TRAFFIC_SHIFTING_RESULT_DATA")
@Getter @Setter @ToString
public class TrafficShiftingResultData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PLAN_ID")
    private String planId;

    @Column(name = "UNIQUE_PLAN_ID")
    private String uniquePlanId;

    @Column(name = "PLAN_STATUS")
    private String planStatus;

    @Column(name = "TEST_STATUS")
    private String testStatus;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "REMARK")
    private String remark;

    @ManyToOne
    @JoinColumn(name = "TS_HISTORY_ID")
    private TrafficShiftingTestHistory history;

}
