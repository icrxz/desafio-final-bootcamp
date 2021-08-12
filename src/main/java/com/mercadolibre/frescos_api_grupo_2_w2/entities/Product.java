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
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private List<Batch> batches;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PurchaseOrderProduct> purchaseOrder;
}
