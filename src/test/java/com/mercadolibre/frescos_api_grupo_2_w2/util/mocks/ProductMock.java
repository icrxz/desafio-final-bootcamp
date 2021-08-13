package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductMock {

    public static UUID productID = UUID.fromString("189a308e-fac9-11eb-9a03-0242ac130003");

    public static Product validProduct (UUID receivedProductId) {
        Product product = new Product();

        if (receivedProductId != null) {
            product.setProductId(receivedProductId);
        } else {
            product.setProductId(productID);
        }
        product.setName("any_name");
        product.setValue(BigDecimal.valueOf(15.50));
        product.setSeller(UserSellerMock.validSeller(1L));
        product.setType(ProductTypeEnum.FRESH);

        return product;
    }
}
