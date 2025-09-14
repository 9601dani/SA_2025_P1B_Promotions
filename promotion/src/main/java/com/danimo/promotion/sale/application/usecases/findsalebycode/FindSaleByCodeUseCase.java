package com.danimo.promotion.sale.application.usecases.findsalebycode;

import com.danimo.promotion.common.application.annotations.UseCase;
import com.danimo.promotion.sale.application.inputadapters.FindSaleByCodeInputPort;
import com.danimo.promotion.sale.application.persistence.FindingSaleByCodeOutputPort;
import com.danimo.promotion.sale.domain.Sale;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class FindSaleByCodeUseCase implements FindSaleByCodeInputPort {
    private final FindingSaleByCodeOutputPort findingSaleByCodeOutputPort;

    @Autowired
    public FindSaleByCodeUseCase(FindingSaleByCodeOutputPort findingSaleByCodeOutputPort) {
        this.findingSaleByCodeOutputPort = findingSaleByCodeOutputPort;
    }


    @Override
    public List<Sale> findByCode(String code) {
        return findingSaleByCodeOutputPort.findingSaleByCode(code);
    }
}
