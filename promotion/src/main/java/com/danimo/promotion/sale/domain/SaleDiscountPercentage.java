package com.danimo.promotion.sale.domain;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class SaleDiscountPercentage {
    private final BigDecimal discountPercentage;

    public SaleDiscountPercentage(BigDecimal discountPercentage) {
        if(discountPercentage == null || discountPercentage.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("El porcentaje de descuento no puede ser nulo");
        }
        this.discountPercentage = discountPercentage;
    }

    public static SaleDiscountPercentage fromBigDecimal(BigDecimal discountPercentage) {
        return new SaleDiscountPercentage(discountPercentage);
    }
}
