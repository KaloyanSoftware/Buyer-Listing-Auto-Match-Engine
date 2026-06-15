package application.backend.repository;

import application.backend.entity.Listing;
import application.backend.entity.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ListingRepository extends JpaRepository<Listing, UUID> {

    List<Listing> findByAgencyIdAndStatus(UUID agencyId, ListingStatus status);

    List<Listing> findByAgencyIdOrderByCreatedAtDesc(UUID agencyId);
}
