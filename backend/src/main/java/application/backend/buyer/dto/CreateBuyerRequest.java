package application.backend.buyer.dto;

import application.backend.entity.BuyerTimeline;
import application.backend.entity.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record CreateBuyerRequest(
    @NotBlank String fullName,
    String email,
    String phone,
    BigDecimal budgetMin,
    @NotNull @DecimalMin("0") BigDecimal budgetMax,
    @NotNull PropertyType propertyType,
    Integer minSqm,
    Integer minBedrooms,
    boolean requiresElevator,
    boolean requiresParking,
    boolean requiresGarden,
    boolean avoidGroundFloor,
    boolean avoidTopFloor,
    BuyerTimeline timeline,
    String notes,
    @NotEmpty List<@NotBlank String> preferredLocations
) {}
