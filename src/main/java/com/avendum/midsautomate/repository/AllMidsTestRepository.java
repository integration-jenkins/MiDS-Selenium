package com.avendum.midsautomate.repository;

import com.avendum.midsautomate.model.MidsTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllMidsTestRepository extends JpaRepository<MidsTest, Long> {
}