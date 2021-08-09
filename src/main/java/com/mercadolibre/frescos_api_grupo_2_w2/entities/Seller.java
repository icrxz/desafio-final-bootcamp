package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Seller extends User{
    private String cnpj;

    @OneToMany(mappedBy = "seller")
    private List<Product> products = new ArrayList<>();
}
