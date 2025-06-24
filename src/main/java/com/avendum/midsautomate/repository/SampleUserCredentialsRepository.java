package com.avendum.midsautomate.repository;

import com.avendum.midsautomate.model.SampleUserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface SampleUserCredentialsRepository extends JpaRepository<SampleUserCredentials,Long> {
        List<SampleUserCredentials> findByDoneBy(String doneBy);
        List<SampleUserCredentials> findByDoneByAndUserName(String doneBy, String username);
       List<SampleUserCredentials> findByDoneByAndRole(String doneBy, String role);
}