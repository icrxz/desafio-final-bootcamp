package com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderProductResponse {
    private UUID productId;
    private String name;
    private BigDecimal value;
    private long quantity;
}
