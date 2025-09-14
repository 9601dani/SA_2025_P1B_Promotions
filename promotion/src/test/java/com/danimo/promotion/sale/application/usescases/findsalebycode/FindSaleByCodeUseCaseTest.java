package com.danimo.promotion.sale.application.usescases.findsalebycode;

import com.danimo.promotion.sale.application.persistence.FindingSaleByCodeOutputPort;
import com.danimo.promotion.sale.application.usecases.findsalebycode.FindSaleByCodeUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindSaleByCodeUseCaseTest {

    private static final String CODE = "PROMO_CODE";
    private static final String CLIENT_ID = "client1";
    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(10);
        private static final String TARGET_TYPE = "ANY";
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusDays(5);

    private FindingSaleByCodeOutputPort findingSaleByCodeOutputPort;
    private FindSaleByCodeUseCase findSaleByCodeUseCase;

    @BeforeEach
    void setUp() {
        findingSaleByCodeOutputPort = mock(FindingSaleByCodeOutputPort.class);
        findSaleByCodeUseCase = new FindSaleByCodeUseCase(findingSaleByCodeOutputPort);
    }

    @Test
    void findByCode_returnsSalesFromPort() {
        // Arrange
        Sale sale = new Sale(
                SaleId.generate(),
                null,
                CODE,
                CLIENT_ID,
                SaleDiscountPercentage.fromBigDecimal(DISCOUNT),
                null,
                SaleTargetType.fromString(TARGET_TYPE),
                SaleCreateAt.generate(),
                SaleStatus.ACTIVE,
                SalePeriod.fromLocalDate(START_DATE, END_DATE)
        );
        List<Sale> expectedSales = List.of(sale);
        when(findingSaleByCodeOutputPort.findingSaleByCode(CODE)).thenReturn(expectedSales);

        // Act
        List<Sale> result = findSaleByCodeUseCase.findByCode(CODE);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CODE, result.get(0).getCode());

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(findingSaleByCodeOutputPort).findingSaleByCode(captor.capture());
        assertEquals(CODE, captor.getValue());
    }

    @Test
    void findByCode_emptyList_returnsEmpty() {
        // Arrange
        when(findingSaleByCodeOutputPort.findingSaleByCode(CODE)).thenReturn(List.of());

        // Act
        List<Sale> result = findSaleByCodeUseCase.findByCode(CODE);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
