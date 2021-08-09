package com.mercadolibre.frescos_api_grupo_2_w2.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BatchDTO {
    @NotNull
    @Min(value = 0)
    private long batchNumber;
    @NotNull
    private String productId;
    @NotNull
    private float currentTemperature;
    @NotNull
    private float minimumTemperature;
    @NotNull
    @Min(value = 0)
    private int initialQuantity;
    @NotNull
    @Min(value = 0)
    private int currentQuantity;
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate manufacturingDate;
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private LocalDateTime manufacturingTime;
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dueDate;
}
