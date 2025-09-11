package com.danimo.promotion.sale.application.outputadapters.persistence;

import com.danimo.promotion.sale.domain.Sale;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindingSaleByLocationOutputPort {
    List<Sale> findingSaleByLocations(UUID locationId);
}
