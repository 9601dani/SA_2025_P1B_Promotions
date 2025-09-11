package com.danimo.promotion.sale.infrastructure.outputadapters.persistence;

import com.danimo.promotion.common.infrastructure.annotations.PersistenceAdapter;
import com.danimo.promotion.sale.application.outputadapters.persistence.FindingSaleByCodeOutputPort;
import com.danimo.promotion.sale.application.outputadapters.persistence.FindingSaleByIdOutputPort;
import com.danimo.promotion.sale.application.outputadapters.persistence.FindingSaleByLocationOutputPort;
import com.danimo.promotion.sale.application.outputadapters.persistence.StoringSaleOutputPort;
import com.danimo.promotion.sale.domain.Sale;
import com.danimo.promotion.sale.infrastructure.outputadapters.persistence.entity.SaleDbEntity;
import com.danimo.promotion.sale.infrastructure.outputadapters.persistence.entity.mapper.SalePersistenceMapper;
import com.danimo.promotion.sale.infrastructure.outputadapters.persistence.repository.SaleDbEntityJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
public class SaleRepositoryOutputAdapter implements FindingSaleByCodeOutputPort, FindingSaleByIdOutputPort,
        FindingSaleByLocationOutputPort, StoringSaleOutputPort {
    private final SaleDbEntityJpaRepository saleDbEntityJpaRepository;
    private final SalePersistenceMapper salePersistenceMapper;

    @Autowired
    public SaleRepositoryOutputAdapter(SaleDbEntityJpaRepository saleDbEntityJpaRepository, SalePersistenceMapper salePersistenceMapper) {
        this.saleDbEntityJpaRepository = saleDbEntityJpaRepository;
        this.salePersistenceMapper = salePersistenceMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Sale> findingSaleById(UUID id) {
        return saleDbEntityJpaRepository.findById(id)
                .map(salePersistenceMapper::toDomain);
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Sale save(Sale sale) {
        SaleDbEntity saleDbEntity = salePersistenceMapper.toDbEntity(sale);

        SaleDbEntity saleDbEntitySaved = saleDbEntityJpaRepository.save(saleDbEntity);

        return salePersistenceMapper.toDomain(saleDbEntitySaved);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> findingSaleByCode(String code) {
        return saleDbEntityJpaRepository.findByCode(code)
                .stream()
                .map(salePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sale> findingSaleByLocations(UUID locationId) {
        return saleDbEntityJpaRepository.findByLocationId(locationId)
                .stream()
                .map(salePersistenceMapper::toDomain)
                .toList();
    }
}
