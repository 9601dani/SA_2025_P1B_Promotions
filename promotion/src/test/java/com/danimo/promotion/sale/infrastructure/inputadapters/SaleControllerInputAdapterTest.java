package com.danimo.promotion.sale.infrastructure.inputadapters;

import com.danimo.promotion.sale.application.inputadapters.*;
import com.danimo.promotion.sale.application.usecases.createsale.CreateSaleDto;
import com.danimo.promotion.sale.domain.*;
import com.danimo.promotion.sale.infrastructure.inputadapters.dto.SaleChangeStatusDto;
import com.danimo.promotion.sale.infrastructure.inputadapters.dto.SaleRequestDto;
import com.danimo.promotion.sale.infrastructure.inputadapters.dto.SaleResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleControllerInputAdapterTest {

    private static final UUID SALE_ID = UUID.randomUUID();
    private static final UUID LOCATION_ID = UUID.randomUUID();
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final String CODE = "PROMO_CTRL";
    private static final String CLIENT_ID = "client1";
    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(10);
    private static final String TARGET_TYPE = "ANY";
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusDays(5);
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private CreateSaleInputPort createSaleInputPort;
    private DisabledStatusSaleInputPort disabledStatusSaleInputPort;
    private FindSaleByCodeInputPort findSaleByCodeInputPort;
    private FindSaleByIdInputPort findSaleByIdInputPort;
    private FindSaleByLocationIdInputPort findSaleByLocationIdInputPort;

    private SaleControllerInputAdapter controller;

    @BeforeEach
    void setUp() {
        createSaleInputPort = mock(CreateSaleInputPort.class);
        disabledStatusSaleInputPort = mock(DisabledStatusSaleInputPort.class);
        findSaleByCodeInputPort = mock(FindSaleByCodeInputPort.class);
        findSaleByIdInputPort = mock(FindSaleByIdInputPort.class);
        findSaleByLocationIdInputPort = mock(FindSaleByLocationIdInputPort.class);

        controller = new SaleControllerInputAdapter(
                createSaleInputPort,
                disabledStatusSaleInputPort,
                findSaleByCodeInputPort,
                findSaleByIdInputPort,
                findSaleByLocationIdInputPort
        );
    }

    private Sale buildSampleSale() {
        return new Sale(
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
    }

    @Test
    void createSale_returnsCreatedResponse() {
        // Arrange
        SaleRequestDto dto = new SaleRequestDto(
                LOCATION_ID.toString(),
                CODE,
                CLIENT_ID,
                DISCOUNT,
                ITEM_ID.toString(),
                TARGET_TYPE,
                START_DATE,
                END_DATE
        );
        Sale sale = buildSampleSale();
        when(createSaleInputPort.createSale(any(CreateSaleDto.class))).thenReturn(sale);

        // Act
        ResponseEntity<SaleResponseDto> response = controller.createSale(dto);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(sale.getCode(), response.getBody().getCode());

        ArgumentCaptor<CreateSaleDto> captor = ArgumentCaptor.forClass(CreateSaleDto.class);
        verify(createSaleInputPort).createSale(captor.capture());
        assertEquals(CODE, captor.getValue().getCode());
    }

    @Test
    void getSalesByLocation_returnsSalesList() {
        // Arrange
        Sale sale = buildSampleSale();
        when(findSaleByLocationIdInputPort.findByLocationId(LOCATION_ID)).thenReturn(List.of(sale));

        // Act
        ResponseEntity<List<SaleResponseDto>> response = controller.getSalesByLocation(LOCATION_ID.toString());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(CODE, response.getBody().get(0).getCode());

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(findSaleByLocationIdInputPort).findByLocationId(captor.capture());
        assertEquals(LOCATION_ID, captor.getValue());
    }

    @Test
    void getSaleByCode_returnsSalesList() {
        // Arrange
        Sale sale = buildSampleSale();
        when(findSaleByCodeInputPort.findByCode(CODE)).thenReturn(List.of(sale));

        // Act
        ResponseEntity<List<SaleResponseDto>> response = controller.getSaleByCode(CODE);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(CODE, response.getBody().get(0).getCode());

        verify(findSaleByCodeInputPort).findByCode(CODE);
    }

    @Test
    void getSalesById_returnsSale() {
        // Arrange
        Sale sale = buildSampleSale();
        when(findSaleByIdInputPort.findSale(SALE_ID)).thenReturn(sale);

        // Act
        ResponseEntity<SaleResponseDto> response = controller.getSalesById(SALE_ID.toString());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(CODE, response.getBody().getCode());

        verify(findSaleByIdInputPort).findSale(SALE_ID);
    }

    @Test
    void updateStatus_returnsUpdatedSale() {
        // Arrange
        SaleChangeStatusDto dto = new SaleChangeStatusDto(SALE_ID, "DISABLED");
        Sale updatedSale = buildSampleSale().changeStatus(SaleStatus.DISABLED);
        when(disabledStatusSaleInputPort.changeStatus(SALE_ID, "DISABLED")).thenReturn(updatedSale);

        // Act
        ResponseEntity<SaleResponseDto> response = controller.updateStatus(dto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("DISABLED", response.getBody().getStatus());

        verify(disabledStatusSaleInputPort).changeStatus(SALE_ID, "DISABLED");
    }
}
