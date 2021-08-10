package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long number;
    private LocalDate date;

    @ManyToOne
    private Section section;

    @OneToMany(mappedBy = "inboundOrder")
    private List<Batch> batchStock = new ArrayList<>();
}
