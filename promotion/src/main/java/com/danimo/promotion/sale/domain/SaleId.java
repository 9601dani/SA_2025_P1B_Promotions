package com.danimo.promotion.sale.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class SaleId {
    private UUID saleId;

    public SaleId(UUID saleId) {
        this.saleId = saleId;
    }

    public static SaleId fromUUID(UUID saleId) {
        return new SaleId(saleId);
    }

    public static SaleId generate(){
        return new SaleId(UUID.randomUUID());
    }
}
