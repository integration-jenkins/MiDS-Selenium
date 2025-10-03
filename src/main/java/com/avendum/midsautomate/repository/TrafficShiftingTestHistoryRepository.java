package com.avendum.midsautomate.repository;

import com.avendum.midsautomate.model.TrafficShiftingTestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrafficShiftingTestHistoryRepository extends JpaRepository<TrafficShiftingTestHistory,Long>{
}
