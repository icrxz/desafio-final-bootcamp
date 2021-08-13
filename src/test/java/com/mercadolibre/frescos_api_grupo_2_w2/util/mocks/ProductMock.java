package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;

import java.util.Optional;
import java.util.UUID;

public class ProductMock {
    public static UUID productID = UUID.fromString("189a308e-fac9-11eb-9a03-0242ac130003");

    public static Product validProduct () {
        Product product = new Product();
        product.setProductId(productID);
        product.setName("any_name");
        product.setSeller(UserSellerMock.validSeller(Optional.of(1L)));
        product.setType(ProductTypeEnum.FRESH);

        return product;

    }
}
