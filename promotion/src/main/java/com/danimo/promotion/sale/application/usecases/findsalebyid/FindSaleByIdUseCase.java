package com.danimo.promotion.sale.application.usecases.findsalebyid;

import com.danimo.promotion.common.application.annotations.UseCase;
import com.danimo.promotion.common.application.exceptions.EntityNotFoundException;
import com.danimo.promotion.sale.application.inputadapters.FindSaleByIdInputPort;
import com.danimo.promotion.sale.application.outputadapters.persistence.FindingSaleByIdOutputPort;
import com.danimo.promotion.sale.domain.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.UUID;

@UseCase
public class FindSaleByIdUseCase implements FindSaleByIdInputPort {
    private final FindingSaleByIdOutputPort findingSaleByIdOutputPort;

    @Autowired
    public FindSaleByIdUseCase(FindingSaleByIdOutputPort findingSaleByIdOutputPort) {
        this.findingSaleByIdOutputPort = findingSaleByIdOutputPort;
    }


    @Override
    public Sale findSale(UUID id) {
        return this.findingSaleByIdOutputPort.findingSaleById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la promocion"));
    }
}
