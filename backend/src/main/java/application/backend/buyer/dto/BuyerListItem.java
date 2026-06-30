package application.backend.buyer.dto;

import application.backend.entity.BuyerStatus;
import application.backend.entity.PropertyType;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record BuyerListItem(
    UUID id,
    String fullName,
    String email,
    String phone,
    BuyerStatus status,
    BigDecimal budgetMin,
    BigDecimal budgetMax,
    PropertyType propertyType,
    List<String> preferredLocations,
    OffsetDateTime lastContactedAt,
    long matchCount
) {}
