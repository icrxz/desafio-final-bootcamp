package com.mercadolibre.frescos_api_grupo_2_w2.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductTypeEnum {
    FRESH("FS", "Fresco"),
    REFRIGERATED("RF", "Refrigerado"),
    FROZEN("FF", "Congelado");

    private final String code;
    private final String description;

    public static ProductTypeEnum toEnum(String code) {
        if (code == null) {
            return null;
        }

        for (ProductTypeEnum productType : ProductTypeEnum.values()) {
            if (code.equals(productType.getCode())) {
                return productType;
            }
        }

        throw new IllegalArgumentException("Product type not valid: " + code);
    }
}
