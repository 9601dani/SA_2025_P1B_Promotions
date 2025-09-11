package com.danimo.promotion.sale.application.outputadapters.persistence;

import com.danimo.promotion.sale.domain.Sale;

public interface StoringSaleOutputPort {
    Sale save(Sale sale);
}
