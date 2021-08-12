package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrder;

import java.math.BigDecimal;

public class PurchaseOrderMapper {
    public static PurchaseOrderResponse entityToResponse(PurchaseOrder purchaseOrder, BigDecimal orderValue) {
        return PurchaseOrderResponse.builder()
                .purchaseOrderId(purchaseOrder.getPurchaseOrderId())
                .purchaseOrderItems(PurchaseOrderProductMapper.entityListToResponseList(purchaseOrder.getProduct()))
                .date(purchaseOrder.getDate())
                .status(purchaseOrder.getStatus())
                .totalPrice(orderValue)
                .build();
    }
}
