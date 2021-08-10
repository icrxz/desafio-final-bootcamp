package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.WarehouseResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;

import java.util.List;
import java.util.stream.Collectors;

public class WarehouseMapper {
    public static WarehouseResponse entityToResponse(Warehouse warehouse) {
        return new WarehouseResponse(warehouse.getWarehouseId(), warehouse.getSupervisor().getUserId());
    }

    public static List<WarehouseResponse> entityListToResponseList(List<Warehouse> warehouses) {
        return warehouses.stream().map(WarehouseMapper::entityToResponse).collect(Collectors.toList());
    }
}
