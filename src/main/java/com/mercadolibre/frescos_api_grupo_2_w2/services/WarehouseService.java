package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.WarehouseDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
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
            //TODO throw custom exception
        }

        return foundWarehouse;
    }

    public Warehouse createWarehouse(WarehouseDTO warehouseDTO) {
        Supervisor foundSupervisor = supervisorService.findSupervisor(warehouseDTO.getSupervisorId());

        Warehouse newWarehouse = Warehouse.builder().supervisor(foundSupervisor).build();

        return warehouseRepository.save(newWarehouse);
    }

    public List<Warehouse> getWarehouses() { return warehouseRepository.findAll(); }
}
