package com.danimo.promotion.sale.domain;

import com.danimo.promotion.common.domain.annotations.DomainEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@DomainEntity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Sale {
    private final SaleId id;
    private final UUID locationId;
    private final String code;
    private final String idClient;
    private final SaleDiscountPercentage discountPercentage;
    private final UUID itemId; // este es el que usare para ver si es dishItem o room
    private final SaleTargetType targetType;
    private final SaleCreateAt createdAt;
    private final SaleStatus status;
    private final SalePeriod salePeriod;


    public Sale changeStatus(SaleStatus newStatus) {
        if (this.status == newStatus) {
            return this;
        }
        return new Sale(
                this.id,
                this.locationId,
                this.code,
                this.idClient,
                this.discountPercentage,
                this.itemId,
                this.targetType,
                this.createdAt,
                newStatus,
                this.salePeriod
        );
    }
}
