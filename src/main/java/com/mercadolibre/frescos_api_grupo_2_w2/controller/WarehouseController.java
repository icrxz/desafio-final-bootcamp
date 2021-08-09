package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.WarehouseDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import com.mercadolibre.frescos_api_grupo_2_w2.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public ResponseEntity createWarehouse(@RequestBody @Valid WarehouseDTO warehouseDTO) {
        System.out.println("Supervisor warehouse id: " + warehouseDTO);
        Warehouse newWarehouse = warehouseService.createWarehouse(warehouseDTO);

        return new ResponseEntity(newWarehouse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getAllWarehouses() {
        return new ResponseEntity(warehouseService.getWarehouses(), HttpStatus.CREATED);
    }
}
