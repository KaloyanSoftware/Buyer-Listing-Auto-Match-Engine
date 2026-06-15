package application.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "buyer_locations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @Column(name = "location_name", nullable = false, length = 200)
    private String locationName;
}
