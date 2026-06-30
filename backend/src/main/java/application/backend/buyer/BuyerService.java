package application.backend.buyer;

import application.backend.buyer.dto.BuyerListItem;
import application.backend.buyer.dto.BuyerResponse;
import application.backend.buyer.dto.CreateBuyerRequest;
import application.backend.entity.Agency;
import application.backend.entity.Buyer;
import application.backend.entity.BuyerLocation;
import application.backend.entity.BuyerStatus;
import application.backend.repository.BuyerListingMatchRepository;
import application.backend.repository.BuyerRepository;
import application.backend.security.CurrentAgency;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final BuyerListingMatchRepository matchRepository;
    private final CurrentAgency currentAgency;
    private final EntityManager entityManager;

    @Transactional
    public BuyerResponse createBuyer(CreateBuyerRequest request) {
        UUID agencyId = currentAgency.getAgencyId();

        // Use a JPA proxy reference — avoids a SELECT but satisfies the FK constraint
        Agency agencyRef = entityManager.getReference(Agency.class, agencyId);

        Buyer buyer = Buyer.builder()
            .agency(agencyRef)
            .fullName(request.fullName())
            .email(request.email())
            .phone(request.phone())
            .budgetMin(request.budgetMin())
            .budgetMax(request.budgetMax())
            .propertyType(request.propertyType())
            .minSqm(request.minSqm())
            .minBedrooms(request.minBedrooms())
            .requiresElevator(request.requiresElevator())
            .requiresParking(request.requiresParking())
            .requiresGarden(request.requiresGarden())
            .avoidGroundFloor(request.avoidGroundFloor())
            .avoidTopFloor(request.avoidTopFloor())
            .timeline(request.timeline())
            .notes(request.notes())
            .status(BuyerStatus.ACTIVE)
            .build();

        List<BuyerLocation> locations = request.preferredLocations().stream()
            .map(name -> BuyerLocation.builder()
                .buyer(buyer)
                .locationName(name)
                .build())
            .toList();
        buyer.getPreferredLocations().addAll(locations);

        Buyer saved = buyerRepository.save(buyer);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BuyerListItem> listBuyers(Optional<BuyerStatus> status) {
        UUID agencyId = currentAgency.getAgencyId();

        List<Buyer> buyers = status
            .map(s -> buyerRepository.findByAgencyIdAndStatus(agencyId, s))
            .orElseGet(() -> buyerRepository.findByAgencyId(agencyId));

        return buyers.stream().map(this::toListItem).toList();
    }

    private BuyerListItem toListItem(Buyer b) {
        long count = matchRepository.countByBuyerId(b.getId());
        List<String> locations = b.getPreferredLocations().stream()
            .map(BuyerLocation::getLocationName)
            .toList();
        return new BuyerListItem(
            b.getId(), b.getFullName(), b.getEmail(), b.getPhone(),
            b.getStatus(), b.getBudgetMin(), b.getBudgetMax(), b.getPropertyType(),
            locations, b.getLastContactedAt(), count
        );
    }

    private BuyerResponse toResponse(Buyer b) {
        long count = matchRepository.countByBuyerId(b.getId());
        List<String> locations = b.getPreferredLocations().stream()
            .map(BuyerLocation::getLocationName)
            .toList();
        return new BuyerResponse(
            b.getId(), b.getFullName(), b.getEmail(), b.getPhone(),
            b.getStatus(), b.getBudgetMin(), b.getBudgetMax(), b.getPropertyType(),
            b.getMinSqm(), b.getMinBedrooms(),
            b.isRequiresElevator(), b.isRequiresParking(), b.isRequiresGarden(),
            b.isAvoidGroundFloor(), b.isAvoidTopFloor(),
            b.getTimeline(), b.getNotes(), locations,
            b.getLastContactedAt(), b.getCreatedAt(), count
        );
    }
}
