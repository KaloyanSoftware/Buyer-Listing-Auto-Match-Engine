package application.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "buyer_listing_matches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerListingMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @Column(name = "match_score", nullable = false)
    private int matchScore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "matched_criteria", columnDefinition = "jsonb")
    private Map<String, Boolean> matchedCriteria;

    @Enumerated(EnumType.STRING)
    @Column(name = "agent_action", nullable = false, length = 20)
    private AgentAction agentAction;

    @Column(name = "notified_at")
    private OffsetDateTime notifiedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
