package com.danimo.promotion.sale.application.usescases.findsalebyid;

import com.danimo.promotion.common.application.exceptions.EntityNotFoundException;
import com.danimo.promotion.sale.application.persistence.FindingSaleByIdOutputPort;
import com.danimo.promotion.sale.application.usecases.findsalebyid.FindSaleByIdUseCase;
import com.danimo.promotion.sale.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindSaleByIdUseCaseTest {

    private static final UUID SALE_ID = UUID.randomUUID();
    private static final UUID LOCATION_ID = UUID.randomUUID();
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final String CODE = "PROMO_ID";
    private static final String CLIENT_ID = "client1";
    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(10);
    private static final String TARGET_TYPE = "ANY";
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusDays(5);

    private FindingSaleByIdOutputPort findingSaleByIdOutputPort;
    private FindSaleByIdUseCase findSaleByIdUseCase;

    @BeforeEach
    void setUp() {
        findingSaleByIdOutputPort = mock(FindingSaleByIdOutputPort.class);
        findSaleByIdUseCase = new FindSaleByIdUseCase(findingSaleByIdOutputPort);
    }

    @Test
    void findSale_saleNotFound_throwsEntityNotFoundException() {
        // Arrange
        when(findingSaleByIdOutputPort.findingSaleById(SALE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> findSaleByIdUseCase.findSale(SALE_ID));
        assertEquals("No se encontro la promocion", ex.getMessage());
    }

    @Test
    void findSale_saleExists_returnsSale() {
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
        when(findingSaleByIdOutputPort.findingSaleById(SALE_ID)).thenReturn(Optional.of(existingSale));

        // Act
        Sale result = findSaleByIdUseCase.findSale(SALE_ID);

        // Assert
        assertNotNull(result);
        assertEquals(CODE, result.getCode());
        assertEquals(LOCATION_ID, result.getLocationId());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(findingSaleByIdOutputPort).findingSaleById(captor.capture());
        assertEquals(SALE_ID, captor.getValue());
    }
}
