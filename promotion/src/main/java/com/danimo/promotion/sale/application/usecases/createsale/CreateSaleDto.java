package com.danimo.promotion.sale.application.usecases.createsale;

import com.danimo.promotion.sale.domain.*;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class CreateSaleDto {
    private final String locationId;
    private final String code;
    private final String idClient;
    private final BigDecimal discountPercentage;
    private final String itemId;
    private final String targetType;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Sale toDomain(){
        return new Sale(
                SaleId.generate(),
                UUID.fromString(locationId),
                code.toUpperCase(),
                idClient,
                SaleDiscountPercentage.fromBigDecimal(discountPercentage),
                itemId != null ? UUID.fromString(itemId) : null,
                SaleTargetType.fromString(targetType),
                SaleCreateAt.generate(),
                SaleStatus.ACTIVE,
                SalePeriod.fromLocalDate(startDate,endDate)
        );
    }

}
