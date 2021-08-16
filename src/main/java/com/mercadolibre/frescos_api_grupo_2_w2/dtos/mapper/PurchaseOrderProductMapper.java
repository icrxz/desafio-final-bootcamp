package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrderProduct;

import java.util.List;
import java.util.stream.Collectors;

public class PurchaseOrderProductMapper {
    public static PurchaseOrderProductResponse entityToResponse(PurchaseOrderProduct purchaseOrderProduct) {
        return PurchaseOrderProductResponse.builder()
                .productId(purchaseOrderProduct.getProduct().getProductId())
                .name(purchaseOrderProduct.getProduct().getName())
                .quantity(purchaseOrderProduct.getQuantity())
                .value(purchaseOrderProduct.getProduct().getValue())
                .build();
    }

    public static List<PurchaseOrderProductResponse> entityListToResponseList(List<PurchaseOrderProduct> products) {
        return products.stream().map(PurchaseOrderProductMapper::entityToResponse).collect(Collectors.toList());
    }
}
