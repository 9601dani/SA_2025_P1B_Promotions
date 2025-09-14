package com.danimo.promotion.sale.application.persistence;

import com.danimo.promotion.sale.domain.Sale;

import java.util.List;

public interface FindingSaleByCodeOutputPort {
    List<Sale> findingSaleByCode(String code);
}
