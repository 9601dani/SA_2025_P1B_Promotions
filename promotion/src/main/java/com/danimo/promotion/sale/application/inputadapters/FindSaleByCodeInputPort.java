package com.danimo.promotion.sale.application.inputadapters;

import com.danimo.promotion.sale.domain.Sale;

import java.util.List;

public interface FindSaleByCodeInputPort {
    List<Sale> findByCode(String code);
}
