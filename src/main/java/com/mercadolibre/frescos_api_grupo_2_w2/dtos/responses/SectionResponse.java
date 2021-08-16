package com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {
    private UUID sectionId;
    private long maxCapacity;
    private String type;
    private UUID warehouseId;
}
