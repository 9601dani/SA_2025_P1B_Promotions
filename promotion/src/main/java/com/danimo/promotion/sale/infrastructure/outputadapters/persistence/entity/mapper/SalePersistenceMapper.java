package com.danimo.promotion.sale.infrastructure.outputadapters.persistence.entity.mapper;

import com.danimo.promotion.sale.domain.*;
import com.danimo.promotion.sale.infrastructure.outputadapters.persistence.entity.SaleDbEntity;
import org.springframework.stereotype.Component;

@Component
public class SalePersistenceMapper {
    public Sale toDomain(SaleDbEntity dbEntity){
        if(dbEntity == null) return null;

        return new Sale(
                SaleId.fromUUID(dbEntity.getId()),
                dbEntity.getLocationId(),
                dbEntity.getCode(),
                dbEntity.getIdClient(),
                SaleDiscountPercentage.fromBigDecimal(dbEntity.getDiscountPercentage()),
                dbEntity.getItemId(),
                dbEntity.getTargetType(),
                SaleCreateAt.fromLocalDateTime(dbEntity.getCreatedAt()),
                dbEntity.getStatus(),
                new SalePeriod(dbEntity.getStartDate(), dbEntity.getEndDate())
        );
    }

    public SaleDbEntity toDbEntity(Sale domain){
        if(domain == null) return null;

        return new SaleDbEntity(
                domain.getId().getSaleId(),
                domain.getLocationId(),
                domain.getCode(),
                domain.getIdClient(),
                domain.getDiscountPercentage().getDiscountPercentage(),
                domain.getItemId(),
                domain.getTargetType(),
                domain.getCreatedAt().getCreateAt(),
                domain.getStatus(),
                domain.getSalePeriod().getStartDate(),
                domain.getSalePeriod().getEndDate()
        );
    }
}
