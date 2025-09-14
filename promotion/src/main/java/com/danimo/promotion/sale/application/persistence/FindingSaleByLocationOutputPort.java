package com.danimo.promotion.sale.application.persistence;

import com.danimo.promotion.sale.domain.Sale;

import java.util.List;
import java.util.UUID;

public interface FindingSaleByLocationOutputPort {
    List<Sale> findingSaleByLocations(UUID locationId);
}
