package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrderProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseOrderMapper {
    public static PurchaseOrderResponse entityToResponse(PurchaseOrder purchaseOrder, BigDecimal orderValue) {
        return PurchaseOrderResponse.builder()
                .purchaseOrderId(purchaseOrder.getPurchaseOrderId())
                .purchaseOrderItems(PurchaseOrderProductMapper.entityListToResponseList(purchaseOrder.getProduct()))
                .date(purchaseOrder.getDate())
                .status(purchaseOrder.getStatus().getDescription())
                .totalPrice(orderValue)
                .build();
    }

    public static PurchaseOrderResponse entityToResponse(PurchaseOrder purchaseOrder) {
        return PurchaseOrderResponse.builder()
                .purchaseOrderId(purchaseOrder.getPurchaseOrderId())
                .purchaseOrderItems(PurchaseOrderProductMapper.entityListToResponseList(purchaseOrder.getProduct()))
                .date(purchaseOrder.getDate())
                .status(purchaseOrder.getStatus().getDescription())
                .build();
    }

    public static List<PurchaseOrderResponse> entityListToResponseList(List<PurchaseOrder> purchaseOrders) {
        return purchaseOrders.stream().map(PurchaseOrderMapper::entityToResponse).collect(Collectors.toList());
    }
}
