package application.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "buyers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Buyer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_agent_id")
    private User assignedAgent;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(length = 320)
    private String email;

    @Column(length = 30)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BuyerStatus status;

    @Column(name = "budget_min", precision = 12, scale = 2)
    private BigDecimal budgetMin;

    @Column(name = "budget_max", nullable = false, precision = 12, scale = 2)
    private BigDecimal budgetMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false, length = 20)
    private PropertyType propertyType;

    @Column(name = "min_sqm")
    private Integer minSqm;

    @Column(name = "min_bedrooms")
    private Integer minBedrooms;

    @Column(name = "requires_elevator", nullable = false)
    private boolean requiresElevator;

    @Column(name = "requires_parking", nullable = false)
    private boolean requiresParking;

    @Column(name = "requires_garden", nullable = false)
    private boolean requiresGarden;

    @Column(name = "avoid_ground_floor", nullable = false)
    private boolean avoidGroundFloor;

    @Column(name = "avoid_top_floor", nullable = false)
    private boolean avoidTopFloor;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private BuyerTimeline timeline;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_contacted_at")
    private OffsetDateTime lastContactedAt;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BuyerLocation> preferredLocations = new ArrayList<>();
}
