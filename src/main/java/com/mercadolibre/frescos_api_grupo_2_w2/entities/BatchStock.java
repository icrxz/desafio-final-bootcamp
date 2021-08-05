package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class BatchStock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long number;

    @ManyToOne
    private Product product;

    @ManyToOne
    private InboundOrder inboundOrder;

    private Float currentTemperature;
    private Float minimumTemperature;
    private int initialQuantity;
    private int currentQuantity;
    private LocalDate manufacturingDate;
    private LocalDateTime manufacturingTime;
    private LocalDate dueDate;
}
