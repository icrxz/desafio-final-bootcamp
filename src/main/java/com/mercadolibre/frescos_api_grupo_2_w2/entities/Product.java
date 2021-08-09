package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private ProductTypeEnum type;

    @ManyToOne
    private Seller seller;
}
