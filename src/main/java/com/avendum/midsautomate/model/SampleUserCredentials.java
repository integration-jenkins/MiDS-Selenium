package com.avendum.midsautomate.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "sample_users_credentials")
public class SampleUserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sample_user_seq")
    @SequenceGenerator(name = "sample_user_seq", sequenceName = "sample_user_sequence", allocationSize = 1)
    @Column(name = "sample_user_id")
    private Long sampleUserId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "done_by")
    private String doneBy;
    @Column(name = "password")
    private String password;
    @Column(name = "circle")
    private String circle;
    @Column(name = "role")
    private String role;
}