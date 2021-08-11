package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {
    public static ProductResponse entityToResponse(Product product) {
        return ProductResponse.builder()
            .productId(product.getProductId())
            .name(product.getName())
            .type(product.getType())
            .value(product.getValue())
            .sellerId(product.getSeller().getUserId())
            .build();
    }

    public static List<ProductResponse> entityListToResponseList(List<Product> products) {
        return products.stream().map(ProductMapper::entityToResponse).collect(Collectors.toList());
    }
}
