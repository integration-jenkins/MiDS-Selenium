package com.avendum.midsautomate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TRAFFIC_SHIFTING_TEST_HISTORY")
@Setter @Getter @ToString
public class TrafficShiftingTestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TEST_ID")
    private String testId;

    @Column(name = "UNIQUE_PLAN_ID")
    private String uniquePlanId;

    @Column(name = "TEST_STATUS")
    private String testStatus;

    @Column(name = "COMPLETION_TIME")
    private Long completionTime;

    @Column(name = "TOTAL_PASS_CASE")
    private Integer totalPassCase;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "TEST_TYPE")
    private String testType;

    @Column(name = "TEST_DATE")
    private LocalDate testDate;

    @Column(name = "TS_RESULT")
    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, orphanRemoval = true)
    List<TrafficShiftingResultData> trafficShiftingResultData = new ArrayList<>();
}
