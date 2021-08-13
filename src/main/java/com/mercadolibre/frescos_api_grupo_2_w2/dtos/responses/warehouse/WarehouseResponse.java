package com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse {
    private UUID warehouseId;
    private long supervisorId;
}
