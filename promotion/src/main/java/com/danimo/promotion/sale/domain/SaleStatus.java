package com.danimo.promotion.sale.domain;

public enum SaleStatus {
    ACTIVE,
    DISABLED;

    public static SaleStatus fromString(String status) {
        if (status == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }
        try {
            return SaleStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Estado de la promocion inválido: " + status);
        }
    }
}
