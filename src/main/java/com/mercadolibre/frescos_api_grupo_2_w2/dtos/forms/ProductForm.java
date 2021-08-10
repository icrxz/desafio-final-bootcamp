package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductForm {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private ProductTypeEnum type;

    @NotNull
    @JsonProperty("seller_id")
    private long sellerId;
}
