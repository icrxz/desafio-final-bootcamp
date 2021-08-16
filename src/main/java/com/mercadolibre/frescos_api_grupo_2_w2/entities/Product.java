package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;

    private String name;

    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private ProductTypeEnum type;

    @ManyToOne
    private Seller seller;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<Batch> batches = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<PurchaseOrderProduct> purchaseOrder = new ArrayList<>();
}
