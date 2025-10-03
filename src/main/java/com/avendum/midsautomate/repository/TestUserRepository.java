package com.avendum.midsautomate.repository;

import com.avendum.midsautomate.model.TestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestUserRepository extends JpaRepository<TestUser,Long> {
}
