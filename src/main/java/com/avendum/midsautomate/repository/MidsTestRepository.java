package com.avendum.midsautomate.repository;
// MidsTestRepository.java

import com.avendum.midsautomate.model.MidsTestManagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MidsTestRepository extends JpaRepository<MidsTestManagement, Long> {
    List<MidsTestManagement> findByPageName(String pageName);
}