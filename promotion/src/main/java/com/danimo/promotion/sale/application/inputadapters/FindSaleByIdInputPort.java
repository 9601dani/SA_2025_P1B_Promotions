package com.danimo.promotion.sale.application.inputadapters;

import com.danimo.promotion.sale.domain.Sale;

import java.util.UUID;

public interface FindSaleByIdInputPort {
    Sale findSale(UUID id);
}
