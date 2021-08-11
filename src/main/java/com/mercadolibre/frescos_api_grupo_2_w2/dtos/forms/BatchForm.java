package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BatchForm {
    @NotNull
    @Min(value = 0)
    @JsonProperty("batch_number")
    private long batchNumber;

    @NotNull
    @JsonProperty("product_id")
    private String productId;

    @NotNull
    @JsonProperty("current_temperature")
    private float currentTemperature;

    @NotNull
    @JsonProperty("minimum_temperature")
    private float minimumTemperature;

    @NotNull
    @Min(value = 0)
    @JsonProperty("initial_quantity")
    private int initialQuantity;

    @NotNull
    @Min(value = 0)
    @JsonProperty("current_quantity")
    private int currentQuantity;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("manufacturing_date")
    private LocalDate manufacturingDate;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonProperty("manufacturing_time")
    private LocalDateTime manufacturingTime;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("due_date")
    private LocalDate dueDate;
}
