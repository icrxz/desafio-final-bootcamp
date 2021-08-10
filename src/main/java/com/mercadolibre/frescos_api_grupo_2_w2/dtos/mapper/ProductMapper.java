package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;

public class ProductMapper {
    public static ProductResponse entityToResponse(Product product) {
        return new ProductResponse(product.getProductId(), product.getName(), product.getType(), product.getSeller().getUserId());
    }
}
