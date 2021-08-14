package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.WarehouseForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.WarehouseMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.warehouse.WarehouseResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.warehouse.WarehouseWithQuantityResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.warehouse.WarehousesWithProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.BatchRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final SupervisorService supervisorService;
    private final ProductService productService;
    private final BatchRepository batchRepository;

    @Autowired
    public WarehouseService(
            WarehouseRepository warehouseRepository,
            SupervisorService supervisorService,
            ProductService productService,
            BatchRepository batchRepository
    ) {
        this.warehouseRepository = warehouseRepository;
        this.supervisorService = supervisorService;
        this.productService = productService;
        this.batchRepository = batchRepository;
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

    public WarehousesWithProductResponse getWarehousesByProduct(UUID productId) {
        productService.findProductById(productId);

        List<Batch> batchesByProduct = batchRepository.findBatchesByProduct_productId(productId);

        if (batchesByProduct.size() <= 0) {
            throw new ApiException("404", "Warehouses with product " + productId + " not found", 404);
        }

        Map<UUID, List<Batch>> batchesByWarehouse = batchesByProduct.stream().collect(Collectors.groupingBy(batch -> {
            return batch.getInboundOrder().getSection().getWarehouse().getWarehouseId();
        }));

        List<WarehouseWithQuantityResponse> warehouses = batchesByWarehouse.entrySet().stream().map(warehouse -> {
            long productQuantity = this.getWarehouseProductQuantity(warehouse.getValue());

            return new WarehouseWithQuantityResponse(warehouse.getKey(), productQuantity);
        }).collect(Collectors.toList());

        return new WarehousesWithProductResponse(productId, warehouses);
    }

    private Long getWarehouseProductQuantity(List<Batch> batches) {
        return batches.stream().mapToLong(Batch::getCurrentQuantity).sum();
    }
}
