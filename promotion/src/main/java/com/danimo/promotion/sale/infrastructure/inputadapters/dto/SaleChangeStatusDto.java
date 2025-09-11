package com.danimo.promotion.sale.infrastructure.inputadapters.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class SaleChangeStatusDto {
    private final UUID id;
    private final String status;
}
