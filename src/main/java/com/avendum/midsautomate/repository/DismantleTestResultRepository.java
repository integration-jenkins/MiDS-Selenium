package com.avendum.midsautomate.repository;

import com.avendum.midsautomate.model.DismantleTestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DismantleTestResultRepository extends JpaRepository<DismantleTestResult,Long> {

    List<DismantleTestResult> findAllByViewReport(String viewReport);
}
