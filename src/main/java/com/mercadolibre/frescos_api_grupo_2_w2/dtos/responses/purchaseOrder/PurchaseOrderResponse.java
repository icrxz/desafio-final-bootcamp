package com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponse {
    private UUID purchaseOrderId;
    private LocalDate date;
    private OrderStatusEnum status;
    private List<PurchaseOrderProductResponse> purchaseOrderItems;
    private BigDecimal totalPrice;
}
