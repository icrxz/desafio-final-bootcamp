package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class InboundOrderSectionForm {
    @NotNull
    @JsonProperty("section_code")
    private String sectionCode;
    @NotNull
    @JsonProperty("warehouse_code")
    private String warehouseCode;
}
