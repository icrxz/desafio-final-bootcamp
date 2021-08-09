package com.mercadolibre.frescos_api_grupo_2_w2.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class InboundOrderSectionDTO {
    @NotNull
    private String sectionCode;
    @NotNull
    private String warehouseCode;
}
