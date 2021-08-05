package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;
    private String name;
    private ProductTypeEnum type;

    @ManyToOne
    private Seller seller;
}
