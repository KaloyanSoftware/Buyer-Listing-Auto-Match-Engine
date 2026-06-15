package application.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank String agencyName,
    @NotBlank @Email String adminEmail,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String adminFullName
) {}
