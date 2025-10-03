package com.avendum.midsautomate.repository;

import com.avendum.midsautomate.model.TrafficShiftingResultData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficShiftingResultDataRepository extends JpaRepository<TrafficShiftingResultData,Long> {
}
