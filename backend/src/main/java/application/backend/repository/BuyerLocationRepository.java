package application.backend.repository;

import application.backend.entity.BuyerLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuyerLocationRepository extends JpaRepository<BuyerLocation, UUID> {
}
