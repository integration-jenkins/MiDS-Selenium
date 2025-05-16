package avendum.com.midsautomate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private  String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Column(name = "created_date", updatable = false)
    private String createdDate;
    @Column(name = "updated_date")
    private String updatedDate;
}
