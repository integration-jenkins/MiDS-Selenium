package avendum.com.midsautomate.repository;
// MidsTestRepository.java

import avendum.com.midsautomate.model.MidsTestManagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MidsTestRepository extends JpaRepository<MidsTestManagement, Long> {
    List<MidsTestManagement> findByPageName(String pageName);
}