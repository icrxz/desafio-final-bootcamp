package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.WarehouseForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.WarehouseMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.warehouse.WarehouseResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.warehouse.WarehousesWithProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fresh-products/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public ResponseEntity<WarehouseResponse> createWarehouse(@RequestBody @Valid WarehouseForm warehouseForm) {
        WarehouseResponse newWarehouse = warehouseService.createWarehouse(warehouseForm);

        return new ResponseEntity<>(newWarehouse, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses() {
        List<WarehouseResponse> warehouses = WarehouseMapper.entityListToResponseList(warehouseService.getWarehouses());

        return new ResponseEntity<>(warehouses, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<WarehousesWithProductResponse> getWarehousesByProductId(@RequestParam UUID productId) {
        WarehousesWithProductResponse warehouses = warehouseService.getWarehousesByProduct(productId);

        return new ResponseEntity<>(warehouses, HttpStatus.OK);
    }
}
