package com.danimo.promotion.sale.application.usecases.disablesale;

import com.danimo.promotion.common.application.annotations.UseCase;
import com.danimo.promotion.common.application.exceptions.EntityNotFoundException;
import com.danimo.promotion.sale.application.inputadapters.DisabledStatusSaleInputPort;
import com.danimo.promotion.sale.application.outputadapters.persistence.FindingSaleByIdOutputPort;
import com.danimo.promotion.sale.application.outputadapters.persistence.StoringSaleOutputPort;
import com.danimo.promotion.sale.domain.Sale;
import com.danimo.promotion.sale.domain.SaleStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@UseCase
public class DisableSaleUseCase implements DisabledStatusSaleInputPort {
    private final FindingSaleByIdOutputPort findingSaleByIdOutputPort;
    private final StoringSaleOutputPort storingSaleOutputPort;

    @Autowired
    public DisableSaleUseCase(FindingSaleByIdOutputPort findingSaleByIdOutputPort, StoringSaleOutputPort storingSaleOutputPort) {
        this.findingSaleByIdOutputPort = findingSaleByIdOutputPort;
        this.storingSaleOutputPort = storingSaleOutputPort;
    }

    @Override
    public Sale changeStatus(UUID id, String status) {
        Sale currentSale = findingSaleByIdOutputPort.findingSaleById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la promocion"));

        Sale newSale = currentSale.changeStatus(SaleStatus.valueOf(status));

        return storingSaleOutputPort.save(newSale);
    }
}
