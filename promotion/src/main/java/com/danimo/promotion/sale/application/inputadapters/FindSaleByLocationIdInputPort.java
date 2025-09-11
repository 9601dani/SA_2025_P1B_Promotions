package com.danimo.promotion.sale.application.inputadapters;

import com.danimo.promotion.sale.domain.Sale;

import java.util.List;
import java.util.UUID;

public interface FindSaleByLocationIdInputPort {
    List<Sale> findByLocationId(UUID locationId);
}
