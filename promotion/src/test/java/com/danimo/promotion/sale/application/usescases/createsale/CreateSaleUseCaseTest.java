package com.danimo.promotion.sale.application.usescases.createsale;

import com.danimo.promotion.common.application.exceptions.EntityAlreadyExistException;
import com.danimo.promotion.sale.application.persistence.FindingSaleByCodeOutputPort;
import com.danimo.promotion.sale.application.persistence.StoringSaleOutputPort;
import com.danimo.promotion.sale.application.rest.ExistItemOutputPort;
import com.danimo.promotion.sale.application.rest.ExistLocationOutputPort;
import com.danimo.promotion.sale.application.usecases.createsale.CreateSaleDto;
import com.danimo.promotion.sale.application.usecases.createsale.CreateSaleUseCase;
import com.danimo.promotion.sale.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateSaleUseCaseTest {

    private static final UUID LOCATION_ID = UUID.randomUUID();
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final String CODE_ACTIVE = "PROMO_ACTIVE";
    private static final String CODE_NEW = "PROMO_NEW";
    private static final String CLIENT_ID = "client1";
    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(10);
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusDays(5);

    private FindingSaleByCodeOutputPort findingSaleByCodeOutputPort;
    private ExistItemOutputPort existItemOutputPort;
    private ExistLocationOutputPort existLocationOutputPort;
    private StoringSaleOutputPort storingSaleOutputPort;

    private CreateSaleUseCase createSaleUseCase;

    @BeforeEach
    void setUp() {
        findingSaleByCodeOutputPort = mock(FindingSaleByCodeOutputPort.class);
        existItemOutputPort = mock(ExistItemOutputPort.class);
        existLocationOutputPort = mock(ExistLocationOutputPort.class);
        storingSaleOutputPort = mock(StoringSaleOutputPort.class);

        createSaleUseCase = new CreateSaleUseCase(
                findingSaleByCodeOutputPort,
                existItemOutputPort,
                existLocationOutputPort,
                storingSaleOutputPort
        );
    }

    @Test
    void createSale_existingActiveSale_throwsIllegalArgumentException() {
        // Arrange
        CreateSaleDto dto = new CreateSaleDto(
                LOCATION_ID.toString(),
                CODE_ACTIVE,
                CLIENT_ID,
                DISCOUNT,
                null,
                "ANY",
                START_DATE,
                END_DATE
        );

        Sale existingSale = dto.toDomain().changeStatus(SaleStatus.ACTIVE);
        when(findingSaleByCodeOutputPort.findingSaleByCode(CODE_ACTIVE.toUpperCase()))
                .thenReturn(List.of(existingSale));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> createSaleUseCase.createSale(dto));
        assertTrue(ex.getMessage().contains("Ya existe una promoción activa"));
    }

    @Test
    void createSale_itemDoesNotExist_throwsEntityAlreadyExistException() {
        // Arrange
        CreateSaleDto dto = new CreateSaleDto(
                LOCATION_ID.toString(),
                CODE_NEW,
                CLIENT_ID,
                DISCOUNT,
                ITEM_ID.toString(),
                "GENERAL",
                START_DATE,
                END_DATE
        );

        when(findingSaleByCodeOutputPort.findingSaleByCode(CODE_NEW.toUpperCase()))
                .thenReturn(Collections.emptyList());
        when(existItemOutputPort.existItem(ITEM_ID)).thenReturn(false);

        // Act & Assert
        EntityAlreadyExistException ex = assertThrows(EntityAlreadyExistException.class,
                () -> createSaleUseCase.createSale(dto));
        assertEquals("El item no existe", ex.getMessage());
    }

    @Test
    void createSale_locationDoesNotExist_throwsEntityAlreadyExistException() {
        // Arrange
        CreateSaleDto dto = new CreateSaleDto(
                LOCATION_ID.toString(),
                CODE_NEW,
                CLIENT_ID,
                DISCOUNT,
                null,
                "GENERAL",
                START_DATE,
                END_DATE
        );

        when(findingSaleByCodeOutputPort.findingSaleByCode(CODE_NEW.toUpperCase()))
                .thenReturn(Collections.emptyList());
        when(existLocationOutputPort.existLocation(LOCATION_ID)).thenReturn(false);

        // Act & Assert
        EntityAlreadyExistException ex = assertThrows(EntityAlreadyExistException.class,
                () -> createSaleUseCase.createSale(dto));
        assertEquals("La sucursal no existe", ex.getMessage());
    }

    @Test
    void createSale_successfulWithoutItem_returnsSavedSale() {
        // Arrange
        CreateSaleDto dto = new CreateSaleDto(
                LOCATION_ID.toString(),
                CODE_NEW,
                CLIENT_ID,
                DISCOUNT,
                null,
                "ANY",
                START_DATE,
                END_DATE
        );

        when(findingSaleByCodeOutputPort.findingSaleByCode(CODE_NEW.toUpperCase()))
                .thenReturn(Collections.emptyList());
        when(existLocationOutputPort.existLocation(LOCATION_ID)).thenReturn(true);

        Sale expectedSale = dto.toDomain();
        when(storingSaleOutputPort.save(any(Sale.class))).thenReturn(expectedSale);

        // Act
        Sale result = createSaleUseCase.createSale(dto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSale.getCode(), result.getCode());
        assertEquals(expectedSale.getLocationId(), result.getLocationId());

        ArgumentCaptor<Sale> captor = ArgumentCaptor.forClass(Sale.class);
        verify(storingSaleOutputPort).save(captor.capture());
        assertEquals(expectedSale.getCode(), captor.getValue().getCode());
    }

    @Test
    void createSale_successfulWithItem_returnsSavedSale() {
        // Arrange
        CreateSaleDto dto = new CreateSaleDto(
                LOCATION_ID.toString(),
                CODE_NEW,
                CLIENT_ID,
                DISCOUNT,
                ITEM_ID.toString(),
                "ANY",
                START_DATE,
                END_DATE
        );

        when(findingSaleByCodeOutputPort.findingSaleByCode(CODE_NEW.toUpperCase()))
                .thenReturn(Collections.emptyList());
        when(existItemOutputPort.existItem(ITEM_ID)).thenReturn(true);
        when(existLocationOutputPort.existLocation(LOCATION_ID)).thenReturn(true);

        Sale expectedSale = dto.toDomain();
        when(storingSaleOutputPort.save(any(Sale.class))).thenReturn(expectedSale);

        // Act
        Sale result = createSaleUseCase.createSale(dto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSale.getCode(), result.getCode());
        assertEquals(expectedSale.getLocationId(), result.getLocationId());
        assertEquals(expectedSale.getItemId(), result.getItemId());

        ArgumentCaptor<Sale> captor = ArgumentCaptor.forClass(Sale.class);
        verify(storingSaleOutputPort).save(captor.capture());
        assertEquals(expectedSale.getCode(), captor.getValue().getCode());
        assertEquals(expectedSale.getItemId(), captor.getValue().getItemId());
    }
}
