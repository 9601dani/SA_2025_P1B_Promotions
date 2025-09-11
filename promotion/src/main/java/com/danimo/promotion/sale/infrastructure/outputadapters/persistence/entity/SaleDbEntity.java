package com.danimo.promotion.sale.infrastructure.outputadapters.persistence.entity;

import com.danimo.promotion.sale.domain.SaleStatus;
import com.danimo.promotion.sale.domain.SaleTargetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "promotion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDbEntity {
    @Id
    private UUID id;
    @Column
    private UUID locationId;

    @Column
    private String code;

    @Column
    private String idClient;

    @Column
    private BigDecimal discountPercentage;

    @Column
    private UUID itemId;

    @Enumerated(EnumType.STRING)
    private SaleTargetType targetType;

    @Column
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private SaleStatus status;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;
}
