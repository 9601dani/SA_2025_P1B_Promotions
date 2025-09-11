package com.danimo.promotion.sale.domain;

import lombok.Value;

import java.time.LocalDate;
@Value
public class SalePeriod {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public SalePeriod(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser despues de la fecha de fin");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static SalePeriod fromLocalDate(LocalDate startDate, LocalDate endDate) {
        return new SalePeriod(startDate, endDate);
    }
}
