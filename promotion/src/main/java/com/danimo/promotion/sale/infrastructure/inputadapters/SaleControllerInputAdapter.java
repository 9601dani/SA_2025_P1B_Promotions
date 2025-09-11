package com.danimo.promotion.sale.infrastructure.inputadapters;

import com.danimo.promotion.common.infrastructure.annotations.WebAdapter;
import com.danimo.promotion.sale.application.inputadapters.*;
import com.danimo.promotion.sale.application.usecases.createsale.CreateSaleDto;
import com.danimo.promotion.sale.domain.Sale;
import com.danimo.promotion.sale.infrastructure.inputadapters.dto.SaleChangeStatusDto;
import com.danimo.promotion.sale.infrastructure.inputadapters.dto.SaleRequestDto;
import com.danimo.promotion.sale.infrastructure.inputadapters.dto.SaleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Promotions", description = "Operaciones relacionadas a las promociones")
@RestController
@RequestMapping("/v1/promotions")
@WebAdapter
public class SaleControllerInputAdapter {
    private final CreateSaleInputPort createSaleInputPort;
    private final DisabledStatusSaleInputPort disabledStatusSaleInputPort;
    private final FindSaleByCodeInputPort findSaleByCodeInputPort;
    private final FindSaleByIdInputPort findSaleByIdInputPort;
    private final FindSaleByLocationIdInputPort findSaleByLocationIdInputPort;

    @Autowired
    public  SaleControllerInputAdapter(CreateSaleInputPort createSaleInputPort, DisabledStatusSaleInputPort disabledStatusSaleInputPort,
                                       FindSaleByCodeInputPort findSaleByCodeInputPort, FindSaleByIdInputPort findSaleByIdInputPort,
                                       FindSaleByLocationIdInputPort findSaleByLocationIdInputPort) {
        this.createSaleInputPort = createSaleInputPort;
        this.disabledStatusSaleInputPort = disabledStatusSaleInputPort;
        this.findSaleByCodeInputPort = findSaleByCodeInputPort;
        this.findSaleByIdInputPort = findSaleByIdInputPort;
        this.findSaleByLocationIdInputPort = findSaleByLocationIdInputPort;
    }

    @Operation(
            summary = "Crear nueva promocion",
            description = "Devuelve la información de la promocion correspondiente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promocion creada"),
            @ApiResponse(responseCode = "404", description = "Promocion no creada")
    })
    @PostMapping
    @Transactional
    public ResponseEntity<SaleResponseDto> createSale(@RequestBody SaleRequestDto dto){
        CreateSaleDto objectAdaptedFromRestToDomain = dto.toDomain();

        Sale sale = createSaleInputPort.createSale(objectAdaptedFromRestToDomain);

        SaleResponseDto response = SaleResponseDto.fromDomain(sale);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Obtener las promociones del establecimiento",
            description = "Devuelve la información de todas las promociones correspondientes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promociones encontradas"),
            @ApiResponse(responseCode = "404", description = "Promociones no encontradas")
    })
    @GetMapping("/location/{locationId}")
    @Transactional
    public ResponseEntity<List<SaleResponseDto>> getSalesByLocation(@PathVariable String  locationId) {
        List<SaleResponseDto> orders = findSaleByLocationIdInputPort.findByLocationId(UUID.fromString(locationId))
                .stream()
                .map(SaleResponseDto::fromDomain)
                .toList();

        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "Busca la promocion por codigo",
            description = "Devuelve la promocion si existe."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitacion encontrada"),
            @ApiResponse(responseCode = "404", description = "Habitacion no encontrada")
    })
    @GetMapping("/code/{code}")
    @Transactional
    public ResponseEntity<List<SaleResponseDto>> getSaleByCode(@PathVariable String code) {
        List<Sale> sales = findSaleByCodeInputPort.findByCode(code);
        return ResponseEntity.ok(sales.stream().map(SaleResponseDto::fromDomain).toList());
    }

    @Operation(
            summary = "Obtener la por id",
            description = "Devuelve la información de la promocion correspondientes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promocione encontrada"),
            @ApiResponse(responseCode = "404", description = "Promocione no encontrada")
    })
    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<SaleResponseDto> getSalesById(@PathVariable String  id) {
        Sale sales = findSaleByIdInputPort.findSale(UUID.fromString(id));

        return ResponseEntity.ok(SaleResponseDto.fromDomain(sales));
    }
    @Operation(
            summary = "Editar el estado de una promocion",
            description = "Devuelve la promocion actualizada, como ya pagada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promocion actualizada"),
            @ApiResponse(responseCode = "404", description = "Promocion no actualizada")
    })
    @PutMapping("/status")
    @Transactional
    public ResponseEntity<SaleResponseDto> updateStatus(@RequestBody SaleChangeStatusDto dto) {
        Sale order = disabledStatusSaleInputPort.changeStatus(dto.getId(), dto.getStatus());
        return ResponseEntity.ok(SaleResponseDto.fromDomain(order));
    }

}
