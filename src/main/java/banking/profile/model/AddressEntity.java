package banking.profile.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",  nullable = false)
    private ClientProfileEntity clientProfile;

    @Column(length = 50, nullable = false)
    private String type;

    @Column(length = 100, nullable = false)
    private String country;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(length = 20)
    private String postalCode;
}
