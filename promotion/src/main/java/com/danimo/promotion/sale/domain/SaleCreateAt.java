package com.danimo.promotion.sale.domain;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class SaleCreateAt {
    private final LocalDateTime createAt;

    public SaleCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public static SaleCreateAt generate(){
        return new SaleCreateAt(LocalDateTime.now());
    }

    public static SaleCreateAt fromLocalDateTime(LocalDateTime localDateTime) {
        return new SaleCreateAt(localDateTime);
    }
}
