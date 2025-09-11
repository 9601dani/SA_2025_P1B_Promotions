package com.danimo.promotion.sale.infrastructure.inputadapters.dto;

import com.danimo.promotion.sale.domain.Sale;
import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class SaleResponseDto {
    private final UUID id;
    private final UUID locationId;
    private final String code;
    private final String idClient;
    private final BigDecimal discountPercentage;
    private final UUID itemId;
    private final String targetType;
    private final LocalDateTime createdAt;
    private final String status;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public static SaleResponseDto fromDomain(Sale sale) {
        return new SaleResponseDto(
                sale.getId().getSaleId(),
                sale.getLocationId(),
                sale.getCode(),
                sale.getIdClient(),
                sale.getDiscountPercentage().getDiscountPercentage(),
                sale.getItemId(),
                sale.getTargetType().name(),
                sale.getCreatedAt().getCreateAt(),
                sale.getStatus().name(),
                sale.getSalePeriod().getStartDate(),
                sale.getSalePeriod().getEndDate()
        );
    }
}
