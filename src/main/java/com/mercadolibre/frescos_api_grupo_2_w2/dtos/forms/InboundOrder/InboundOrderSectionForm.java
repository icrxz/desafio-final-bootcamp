package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InboundOrderSectionForm {
    @NotNull
    @JsonProperty("section_code")
    private String sectionCode;

    @NotNull
    @JsonProperty("warehouse_code")
    private String warehouseCode;
}
