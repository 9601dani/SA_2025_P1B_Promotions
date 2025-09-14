package com.danimo.promotion.sale.application.usescases.findbylocation;

import com.danimo.promotion.sale.application.persistence.FindingSaleByLocationOutputPort;
import com.danimo.promotion.sale.application.usecases.findbylocation.FindSaleByLocationUseCase;
import com.danimo.promotion.sale.domain.Sale;
import com.danimo.promotion.sale.domain.SaleId;
import com.danimo.promotion.sale.domain.SaleDiscountPercentage;
import com.danimo.promotion.sale.domain.SaleTargetType;
import com.danimo.promotion.sale.domain.SaleCreateAt;
import com.danimo.promotion.sale.domain.SalePeriod;
import com.danimo.promotion.sale.domain.SaleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindSaleByLocationUseCaseTest {

    private static final UUID LOCATION_ID = UUID.randomUUID();
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final String CODE = "PROMO_LOCATION";
    private static final String CLIENT_ID = "client1";
    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(10);
    private static final String TARGET_TYPE = "ANY";
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusDays(5);

    private FindingSaleByLocationOutputPort findingSaleByLocationOutputPort;
    private FindSaleByLocationUseCase findSaleByLocationUseCase;

    @BeforeEach
    void setUp() {
        findingSaleByLocationOutputPort = mock(FindingSaleByLocationOutputPort.class);
        findSaleByLocationUseCase = new FindSaleByLocationUseCase(findingSaleByLocationOutputPort);
    }

    @Test
    void findByLocationId_returnsSalesFromPort() {
        // Arrange
        Sale sale = new Sale(
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
        List<Sale> expectedSales = List.of(sale);
        when(findingSaleByLocationOutputPort.findingSaleByLocations(LOCATION_ID))
                .thenReturn(expectedSales);

        // Act
        List<Sale> result = findSaleByLocationUseCase.findByLocationId(LOCATION_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CODE, result.get(0).getCode());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(findingSaleByLocationOutputPort).findingSaleByLocations(captor.capture());
        assertEquals(LOCATION_ID, captor.getValue());
    }

    @Test
    void findByLocationId_emptyList_returnsEmpty() {
        // Arrange
        when(findingSaleByLocationOutputPort.findingSaleByLocations(LOCATION_ID))
                .thenReturn(List.of());

        // Act
        List<Sale> result = findSaleByLocationUseCase.findByLocationId(LOCATION_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
