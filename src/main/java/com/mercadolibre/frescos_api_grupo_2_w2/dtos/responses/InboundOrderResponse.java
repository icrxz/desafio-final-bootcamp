package com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundOrderResponse {
    private LocalDate orderDate;
    private long orderNumber;
    private UUID sectionCode;
    private UUID warehouseCode;
    private List<BatchResponse> successBatches;
    private List<BatchResponse> failedBatches;
    private List<String> errorMessages;
}
