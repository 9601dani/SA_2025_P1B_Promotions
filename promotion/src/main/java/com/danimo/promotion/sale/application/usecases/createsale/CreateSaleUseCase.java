package com.danimo.promotion.sale.application.usecases.createsale;

import com.danimo.promotion.common.application.annotations.UseCase;
import com.danimo.promotion.common.application.exceptions.EntityAlreadyExistException;
import com.danimo.promotion.sale.application.inputadapters.CreateSaleInputPort;
import com.danimo.promotion.sale.application.persistence.FindingSaleByCodeOutputPort;
import com.danimo.promotion.sale.application.persistence.StoringSaleOutputPort;
import com.danimo.promotion.sale.application.rest.ExistItemOutputPort;
import com.danimo.promotion.sale.application.rest.ExistLocationOutputPort;
import com.danimo.promotion.sale.domain.Sale;
import com.danimo.promotion.sale.domain.SaleStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@UseCase
public class CreateSaleUseCase implements CreateSaleInputPort {
    private final FindingSaleByCodeOutputPort findingSaleByCodeOutputPort;
    private final ExistItemOutputPort existItemOutputPort;
    private final ExistLocationOutputPort existLocationOutputPort;
    private final StoringSaleOutputPort storingSaleOutputPort;

    @Autowired
    public CreateSaleUseCase(FindingSaleByCodeOutputPort findingSaleByCodeOutputPort,ExistItemOutputPort existItemOutputPort,
                             ExistLocationOutputPort existLocationOutputPort, StoringSaleOutputPort storingSaleOutputPort) {
        this.findingSaleByCodeOutputPort = findingSaleByCodeOutputPort;
        this.existItemOutputPort = existItemOutputPort;
        this.existLocationOutputPort = existLocationOutputPort;
        this.storingSaleOutputPort = storingSaleOutputPort;
    }

    @Override
    public Sale createSale(CreateSaleDto dto) {
        List<Sale> existingSales = findingSaleByCodeOutputPort
                .findingSaleByCode(dto.getCode().toUpperCase());

        boolean hasActive = existingSales.stream()
                .anyMatch(sale -> sale.getStatus() == SaleStatus.ACTIVE);

        if (hasActive) {
            throw new IllegalArgumentException("Ya existe una promoción activa con el código: " + dto.getCode());
        }

        if(dto.getItemId()!=null) {
            if(!existItemOutputPort.existItem(UUID.fromString(dto.getItemId()))) {
                throw new EntityAlreadyExistException("El item no existe");
            }
        }

        if(!existLocationOutputPort.existLocation(UUID.fromString(dto.getLocationId()))) {
            throw new EntityAlreadyExistException("La sucursal no existe");
        }

        Sale sale = dto.toDomain();

        return storingSaleOutputPort.save(sale);
    }
}
