package com.danimo.promotion.sale.application.persistence;

import com.danimo.promotion.sale.domain.Sale;

public interface StoringSaleOutputPort {
    Sale save(Sale sale);
}
