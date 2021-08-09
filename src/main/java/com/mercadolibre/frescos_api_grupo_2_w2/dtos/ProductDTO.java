package com.mercadolibre.frescos_api_grupo_2_w2.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductDTO {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private String type;

    @NotNull
    private long sellerId;
}
