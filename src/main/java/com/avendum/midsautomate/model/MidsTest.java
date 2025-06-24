package com.avendum.midsautomate.model;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Data
@Entity
@Table(name="mids_test")
public class MidsTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;
    private String testName;
    private String testDate;
    private String status;
    private String testUser;
    private String timeTakenSec;
    @Lob
    @Column
    private String comments;
}
