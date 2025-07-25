package com.avendum.midsautomate.repository;


import com.avendum.midsautomate.model.BasicTestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicTestRepository extends JpaRepository<BasicTestReport, Long> {

        BasicTestReport findByPageName(String page);

        @Query("SELECT b.averageTimeTakenMS FROM BasicTestReport b WHERE b.pageName = :page")
        Long findAverageTimeTakenMS(@Param("page") String page); // Changed return type to Long

        @Query("SELECT b.noOfTest FROM BasicTestReport b WHERE b.pageName = :page")
        Integer findNoOfTest(@Param("page") String page);

        @Query("SELECT COALESCE(b.noOfSuccessTestCount, 0) FROM BasicTestReport b WHERE b.pageName = :page")
        Integer findNoOfSuccessTestCount(@Param("page") String page);

}