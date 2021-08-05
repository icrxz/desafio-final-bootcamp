package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Warehouse{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID warehouseId;

    @OneToMany(mappedBy = "warehouse")
    private List<Section> sections;
}
