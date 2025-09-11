package com.danimo.promotion.sale.application.inputadapters;

import com.danimo.promotion.sale.application.usecases.createsale.CreateSaleDto;
import com.danimo.promotion.sale.domain.Sale;

public interface CreateSaleInputPort {
    Sale createSale(CreateSaleDto dto);
}
