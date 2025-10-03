package com.avendum.midsautomate.repository;

import com.avendum.midsautomate.model.DismantleTestHistory;
import com.avendum.midsautomate.model.DismantleTestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DismantleTestHistoryRepository extends JpaRepository<DismantleTestHistory,Long> {

}
