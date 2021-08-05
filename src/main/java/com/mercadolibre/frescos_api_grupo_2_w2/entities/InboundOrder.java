package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class InboundOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long number;
    private LocalDate date;

    @ManyToOne
    private Section section;

    @OneToMany(mappedBy = "inboundOrder")
    private List<BatchStock> batchStock;
}
