package com.danimo.promotion.sale.application.usescases.disablesale;

import com.danimo.promotion.common.application.exceptions.EntityNotFoundException;
import com.danimo.promotion.sale.application.persistence.FindingSaleByIdOutputPort;
import com.danimo.promotion.sale.application.persistence.StoringSaleOutputPort;
import com.danimo.promotion.sale.application.usecases.disablesale.DisableSaleUseCase;
import com.danimo.promotion.sale.domain.Sale;
import com.danimo.promotion.sale.domain.SaleId;
import com.danimo.promotion.sale.domain.SaleStatus;
import com.danimo.promotion.sale.domain.SaleDiscountPercentage;
import com.danimo.promotion.sale.domain.SaleTargetType;
import com.danimo.promotion.sale.domain.SaleCreateAt;
import com.danimo.promotion.sale.domain.SalePeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisableSaleUseCaseTest {

    private static final UUID SALE_ID = UUID.randomUUID();
    private static final UUID LOCATION_ID = UUID.randomUUID();
    private static final String CODE = "PROMO_DISABLE";
    private static final String CLIENT_ID = "client1";
    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(10);
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final String TARGET_TYPE = "ANY";
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusDays(5);

    private FindingSaleByIdOutputPort findingSaleByIdOutputPort;
    private StoringSaleOutputPort storingSaleOutputPort;

    private DisableSaleUseCase disableSaleUseCase;

    @BeforeEach
    void setUp() {
        findingSaleByIdOutputPort = mock(FindingSaleByIdOutputPort.class);
        storingSaleOutputPort = mock(StoringSaleOutputPort.class);
        disableSaleUseCase = new DisableSaleUseCase(findingSaleByIdOutputPort, storingSaleOutputPort);
    }

    @Test
    void changeStatus_saleNotFound_throwsEntityNotFoundException() {
        // Arrange
        when(findingSaleByIdOutputPort.findingSaleById(SALE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> disableSaleUseCase.changeStatus(SALE_ID, "INACTIVE"));
        assertEquals("No se encontro la promocion", ex.getMessage());
    }

    @Test
    void changeStatus_saleExists_changesStatusAndReturnsSale() {
        // Arrange
        Sale existingSale = new Sale(
                SaleId.generate(),
                LOCATION_ID,
                CODE,
                CLIENT_ID,
                SaleDiscountPercentage.fromBigDecimal(DISCOUNT),
                ITEM_ID,
                SaleTargetType.fromString(TARGET_TYPE),
                SaleCreateAt.generate(),
                SaleStatus.ACTIVE,
                SalePeriod.fromLocalDate(START_DATE, END_DATE)
        );

        when(findingSaleByIdOutputPort.findingSaleById(SALE_ID))
                .thenReturn(Optional.of(existingSale));

        Sale disabledSale = existingSale.changeStatus(SaleStatus.DISABLED);
        when(storingSaleOutputPort.save(any(Sale.class))).thenReturn(disabledSale);

        // Act
        Sale result = disableSaleUseCase.changeStatus(SALE_ID, "DISABLED");

        // Assert
        assertNotNull(result);
        assertEquals(SaleStatus.DISABLED, result.getStatus());

        ArgumentCaptor<Sale> captor = ArgumentCaptor.forClass(Sale.class);
        verify(storingSaleOutputPort).save(captor.capture());
        assertEquals(SaleStatus.DISABLED, captor.getValue().getStatus());
    }
}
