package com.mercadolibre.frescos_api_grupo_2_w2.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    @NotNull
    @Min(value = 0)
    @JsonProperty("max_capacity")
    private long maxCapacity;

    @NotNull
    @JsonProperty("product_type")
    private ProductTypeEnum productType;

    @NotEmpty
    @JsonProperty("warehouse_id")
    private String warehouseId;
}
