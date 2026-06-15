package application.backend.auth.dto;

import java.util.UUID;

public record AuthResponse(
    String token,
    UUID userId,
    UUID agencyId,
    String role,
    String fullName
) {}
