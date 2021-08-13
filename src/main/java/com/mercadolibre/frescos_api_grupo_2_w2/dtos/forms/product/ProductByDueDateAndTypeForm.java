package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.product;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import lombok.Data;

@Data
public class ProductByDueDateAndTypeForm {
    private Long days;

    private ProductTypeEnum type;

    //private Long order;
}
