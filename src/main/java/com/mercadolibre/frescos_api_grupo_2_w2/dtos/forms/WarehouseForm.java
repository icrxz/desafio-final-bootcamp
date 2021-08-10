package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class WarehouseForm {
    @NotNull
    @Min(value = 0)
    private long supervisorId;
}
