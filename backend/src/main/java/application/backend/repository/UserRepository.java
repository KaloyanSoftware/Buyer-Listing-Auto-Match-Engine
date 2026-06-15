package application.backend.repository;

import application.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByAgencyIdAndEmail(UUID agencyId, String email);

    Optional<User> findByEmail(String email);
}
