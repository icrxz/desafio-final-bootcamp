package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID sectionId;

    private long maxCapacity;

    private ProductTypeEnum productType;

    @ManyToOne
    private Warehouse warehouse;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<InboundOrder> orders = new ArrayList<>();
}
