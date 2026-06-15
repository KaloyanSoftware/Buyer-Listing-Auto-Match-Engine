package application.backend.auth;

import application.backend.auth.dto.AuthResponse;
import application.backend.auth.dto.LoginRequest;
import application.backend.auth.dto.RegisterRequest;
import application.backend.entity.Agency;
import application.backend.entity.User;
import application.backend.entity.UserRole;
import application.backend.repository.AgencyRepository;
import application.backend.repository.UserRepository;
import application.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AgencyRepository agencyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        Agency agency = Agency.builder()
            .name(request.agencyName())
            .build();
        agency = agencyRepository.save(agency);

        User admin = User.builder()
            .agency(agency)
            .email(request.adminEmail())
            .passwordHash(passwordEncoder.encode(request.password()))
            .fullName(request.adminFullName())
            .role(UserRole.ADMIN)
            .build();
        admin = userRepository.save(admin);

        String token = jwtService.generateToken(admin.getId(), agency.getId(), admin.getRole().name());
        return new AuthResponse(token, admin.getId(), agency.getId(), admin.getRole().name(), admin.getFullName());
    }

    public AuthResponse login(LoginRequest request) {
        // Email is globally unique across agencies in this implementation.
        // In a multi-tenant setup where email uniqueness is per-agency,
        // the login form would need to include an agency identifier.
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        UUID agencyId = user.getAgency().getId();
        String token = jwtService.generateToken(user.getId(), agencyId, user.getRole().name());
        return new AuthResponse(token, user.getId(), agencyId, user.getRole().name(), user.getFullName());
    }
}
