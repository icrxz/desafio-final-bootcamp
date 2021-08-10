package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.WarehouseForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.WarehouseMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.WarehouseResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import com.mercadolibre.frescos_api_grupo_2_w2.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity createWarehouse(@RequestBody @Valid WarehouseForm warehouseForm) {
        WarehouseResponse newWarehouse = warehouseService.createWarehouse(warehouseForm);

        return new ResponseEntity(newWarehouse, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity getAllWarehouses() {
        List<WarehouseResponse> warehouses = WarehouseMapper.entityListToResponseList(warehouseService.getWarehouses());

        return new ResponseEntity(warehouses, HttpStatus.CREATED);
    }
}
