package com.mercadolibre.frescos_api_grupo_2_w2.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class InboundOrderDTO {
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate orderDate;
    @NotNull
    @Min(value = 0)
    private long orderNumber;
    @NotNull
    @Valid
    private InboundOrderSectionDTO section;
    @NotNull
    @NotBlank
    @Valid
    private List<BatchDTO> batchStock;
}
