package avendum.com.midsautomate.repository;

    import avendum.com.midsautomate.model.SampleUserCredentials;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;

@Repository
    public interface SampleUserCredentialsRepository extends JpaRepository<SampleUserCredentials, String> {
        List<SampleUserCredentials> findByDoneBy(String doneBy);
    }