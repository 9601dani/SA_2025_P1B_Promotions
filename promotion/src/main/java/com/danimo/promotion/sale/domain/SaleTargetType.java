package com.danimo.promotion.sale.domain;

public enum SaleTargetType {
    DISH,
    ROOM,
    ANY;

    public static SaleTargetType fromString(String type) {
        if (type == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }
        try {
            return SaleTargetType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de la promocion inválido: " + type);
        }
    }
}
