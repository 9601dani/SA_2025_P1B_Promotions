package com.danimo.promotion.sale.application.usecases.findbylocation;

import com.danimo.promotion.common.application.annotations.UseCase;
import com.danimo.promotion.sale.application.inputadapters.FindSaleByLocationIdInputPort;
import com.danimo.promotion.sale.application.persistence.FindingSaleByLocationOutputPort;
import com.danimo.promotion.sale.domain.Sale;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@UseCase
public class FindSaleByLocationUseCase implements FindSaleByLocationIdInputPort {
    private final FindingSaleByLocationOutputPort findingSaleByLocationOutputPort;

    @Autowired
    public FindSaleByLocationUseCase(FindingSaleByLocationOutputPort findingSaleByLocationOutputPort) {
        this.findingSaleByLocationOutputPort = findingSaleByLocationOutputPort;
    }

    @Override
    public List<Sale> findByLocationId(UUID locationId) {
        return findingSaleByLocationOutputPort.findingSaleByLocations(locationId);
    }
}
