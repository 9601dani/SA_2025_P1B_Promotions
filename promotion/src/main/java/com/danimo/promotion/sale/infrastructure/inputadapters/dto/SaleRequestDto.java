package com.danimo.promotion.sale.infrastructure.inputadapters.dto;

import com.danimo.promotion.sale.application.usecases.createsale.CreateSaleDto;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class SaleRequestDto {
    private final String locationId;
    private final String code;
    private final String idClient;
    private final BigDecimal discountPercentage;
    private final String itemId;
    private final String targetType;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public CreateSaleDto toDomain(){
        return new CreateSaleDto(
                locationId,
                code,
                idClient,
                discountPercentage,
                itemId,
                targetType,
                startDate,
                endDate
        );
    }
}
