package application.backend.repository;

import application.backend.entity.AgentAction;
import application.backend.entity.BuyerListingMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BuyerListingMatchRepository extends JpaRepository<BuyerListingMatch, UUID> {

    List<BuyerListingMatch> findByListingIdOrderByMatchScoreDesc(UUID listingId);

    List<BuyerListingMatch> findByBuyerId(UUID buyerId);

    List<BuyerListingMatch> findByAgencyIdAndAgentAction(UUID agencyId, AgentAction action);

    long countByAgencyId(UUID agencyId);

    long countByAgencyIdAndAgentAction(UUID agencyId, AgentAction action);

    long countByBuyerId(UUID buyerId);
}
