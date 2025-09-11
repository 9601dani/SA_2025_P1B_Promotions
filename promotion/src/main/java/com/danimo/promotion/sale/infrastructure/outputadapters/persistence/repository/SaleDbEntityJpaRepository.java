package com.danimo.promotion.sale.infrastructure.outputadapters.persistence.repository;

import com.danimo.promotion.sale.infrastructure.outputadapters.persistence.entity.SaleDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaleDbEntityJpaRepository extends JpaRepository<SaleDbEntity, UUID> {
    List<SaleDbEntity> findByCode(String code);
    List<SaleDbEntity> findByLocationId(UUID locationId);
}
