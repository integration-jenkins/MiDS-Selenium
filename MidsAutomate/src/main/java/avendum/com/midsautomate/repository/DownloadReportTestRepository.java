package avendum.com.midsautomate.repository;
import avendum.com.midsautomate.model.DownloadReportTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DownloadReportTestRepository extends JpaRepository<DownloadReportTest, Long> {

    DownloadReportTest findByReportName(String sampleReport);

    @Query("SELECT d.averageTimeTakenMS FROM DownloadReportTest d WHERE d.reportName = :sampleReport")
    Long findAverageTimeTakenMS(@Param("sampleReport") String sampleReport);

    @Query("SELECT d.noOfTest FROM DownloadReportTest d WHERE d.reportName = :sampleReport")
    Integer findNoOfTest(@Param("sampleReport") String sampleReport);
}