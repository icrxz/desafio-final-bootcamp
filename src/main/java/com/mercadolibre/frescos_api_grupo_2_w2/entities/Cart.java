package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cart{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID cartId;

    @ManyToMany
    private List<Product> products;
}
