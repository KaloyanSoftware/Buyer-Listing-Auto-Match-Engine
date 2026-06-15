package application.backend.repository;

import application.backend.entity.Buyer;
import application.backend.entity.BuyerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BuyerRepository extends JpaRepository<Buyer, UUID> {

    List<Buyer> findByAgencyIdAndStatus(UUID agencyId, BuyerStatus status);

    List<Buyer> findByAgencyId(UUID agencyId);
}
