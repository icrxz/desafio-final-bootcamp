package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
public class Warehouse{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID warehouseId;

    @OneToOne
    private Supervisor supervisor;

    @OneToMany(mappedBy = "warehouse")
    private List<Section> sections;
}
