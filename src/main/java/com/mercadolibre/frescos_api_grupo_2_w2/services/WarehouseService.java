package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.WarehouseForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.WarehouseMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.WarehouseResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final SupervisorService supervisorService;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository, SupervisorService supervisorService) {
        this.warehouseRepository = warehouseRepository;
        this.supervisorService = supervisorService;
    }

    public Warehouse findWarehouseById(UUID warehouseId) {
        Warehouse foundWarehouse = warehouseRepository.findById(warehouseId).orElse(null);

        if (foundWarehouse == null) {
            throw new ApiException("404", "Warehouse not found with this id", 404);
        }

        return foundWarehouse;
    }

    public WarehouseResponse createWarehouse(WarehouseForm warehouseForm) {
        Supervisor foundSupervisor = supervisorService.findSupervisor(warehouseForm.getSupervisorId());

        Warehouse newWarehouse = Warehouse.builder().supervisor(foundSupervisor).build();

        return WarehouseMapper.entityToResponse(warehouseRepository.save(newWarehouse));
    }

    public List<Warehouse> getWarehouses() { return warehouseRepository.findAll(); }
}
