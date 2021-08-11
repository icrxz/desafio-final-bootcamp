package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import java.util.UUID;

public class ProductMock {
    public static Product create (){
        UUID productId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

        Product product = new Product();
        product.setName("any_name");
        product.setType(ProductTypeEnum.FRESH);
        product.setProductId(productId);

        return product;

    }
}
