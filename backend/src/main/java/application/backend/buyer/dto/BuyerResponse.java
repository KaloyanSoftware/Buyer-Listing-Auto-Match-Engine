package application.backend.buyer.dto;

import application.backend.entity.BuyerStatus;
import application.backend.entity.BuyerTimeline;
import application.backend.entity.PropertyType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record BuyerResponse(
    UUID id,
    String fullName,
    String email,
    String phone,
    BuyerStatus status,
    BigDecimal budgetMin,
    BigDecimal budgetMax,
    PropertyType propertyType,
    Integer minSqm,
    Integer minBedrooms,
    boolean requiresElevator,
    boolean requiresParking,
    boolean requiresGarden,
    boolean avoidGroundFloor,
    boolean avoidTopFloor,
    BuyerTimeline timeline,
    String notes,
    List<String> preferredLocations,
    OffsetDateTime lastContactedAt,
    OffsetDateTime createdAt,
    long matchCount
) {}
