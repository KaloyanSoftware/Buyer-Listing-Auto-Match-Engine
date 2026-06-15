package application.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentAgency {

    private final JwtService jwtService;

    public CurrentAgency(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public UUID getAgencyId() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtService.extractAgencyId(token);
    }

    public UUID getUserId() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtService.extractUserId(token);
    }

    public String getRole() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtService.extractRole(token);
    }
}
