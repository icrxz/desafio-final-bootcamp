package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse")
public class Warehouse{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID warehouseId;

    @OneToOne
    private Supervisor supervisor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "warehouse")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<Section> sections = new ArrayList<>();
}
