package com.danimo.promotion.sale.infrastructure.outputadapters.persistence;

import com.danimo.promotion.sale.domain.*;
import com.danimo.promotion.sale.infrastructure.outputadapters.persistence.entity.SaleDbEntity;
import com.danimo.promotion.sale.infrastructure.outputadapters.persistence.entity.mapper.SalePersistenceMapper;
import com.danimo.promotion.sale.infrastructure.outputadapters.persistence.repository.SaleDbEntityJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleRepositoryOutputAdapterTest {

    private static final UUID SALE_ID = UUID.randomUUID();
    private static final UUID LOCATION_ID = UUID.randomUUID();
    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final String CODE = "PROMO_REPO";
    private static final String CLIENT_ID = "client1";
    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(10);
    private static final String TARGET_TYPE = "ANY";
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusDays(5);
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private SaleDbEntityJpaRepository repository;
    private SalePersistenceMapper mapper;
    private SaleRepositoryOutputAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(SaleDbEntityJpaRepository.class);
        mapper = mock(SalePersistenceMapper.class);
        adapter = new SaleRepositoryOutputAdapter(repository, mapper);
    }

    private Sale buildDomainSale() {
        return new Sale(
                SaleId.fromUUID(SALE_ID),
                LOCATION_ID,
                CODE,
                CLIENT_ID,
                SaleDiscountPercentage.fromBigDecimal(DISCOUNT),
                ITEM_ID,
                SaleTargetType.fromString(TARGET_TYPE),
                SaleCreateAt.fromLocalDateTime(CREATED_AT),
                SaleStatus.ACTIVE,
                new SalePeriod(START_DATE, END_DATE)
        );
    }

    private SaleDbEntity buildDbEntity() {
        return new SaleDbEntity(
                SALE_ID,
                LOCATION_ID,
                CODE,
                CLIENT_ID,
                DISCOUNT,
                ITEM_ID,
                SaleTargetType.fromString(TARGET_TYPE),
                CREATED_AT,
                SaleStatus.ACTIVE,
                START_DATE,
                END_DATE
        );
    }

    @Test
    void findingSaleById_returnsMappedSale() {
        // Arrange
        SaleDbEntity entity = buildDbEntity();
        Sale domain = buildDomainSale();
        when(repository.findById(SALE_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        // Act
        Optional<Sale> result = adapter.findingSaleById(SALE_ID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domain.getCode(), result.get().getCode());
        verify(repository).findById(SALE_ID);
        verify(mapper).toDomain(entity);
    }

    @Test
    void save_convertsAndSavesSale() {
        // Arrange
        Sale domainSale = buildDomainSale();
        SaleDbEntity entity = buildDbEntity();
        when(mapper.toDbEntity(domainSale)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domainSale);

        // Act
        Sale result = adapter.save(domainSale);

        // Assert
        assertNotNull(result);
        assertEquals(domainSale.getCode(), result.getCode());

        ArgumentCaptor<SaleDbEntity> captor = ArgumentCaptor.forClass(SaleDbEntity.class);
        verify(repository).save(captor.capture());
        assertEquals(entity.getCode(), captor.getValue().getCode());
    }

    @Test
    void findingSaleByCode_returnsMappedList() {
        // Arrange
        SaleDbEntity entity = buildDbEntity();
        Sale domain = buildDomainSale();
        when(repository.findByCode(CODE)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        // Act
        List<Sale> result = adapter.findingSaleByCode(CODE);

        // Assert
        assertEquals(1, result.size());
        assertEquals(domain.getCode(), result.get(0).getCode());

        verify(repository).findByCode(CODE);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findingSaleByLocations_returnsMappedList() {
        // Arrange
        SaleDbEntity entity = buildDbEntity();
        Sale domain = buildDomainSale();
        when(repository.findByLocationId(LOCATION_ID)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        // Act
        List<Sale> result = adapter.findingSaleByLocations(LOCATION_ID);

        // Assert
        assertEquals(1, result.size());
        assertEquals(domain.getLocationId(), result.get(0).getLocationId());

        verify(repository).findByLocationId(LOCATION_ID);
        verify(mapper).toDomain(entity);
    }
}
