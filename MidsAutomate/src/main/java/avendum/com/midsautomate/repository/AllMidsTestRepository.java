package avendum.com.midsautomate.repository;

import avendum.com.midsautomate.model.MidsTest;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AllMidsTestRepository extends JpaRepository<MidsTest, Long> {
    // Additional custom query methods (if any) can be added here
}
