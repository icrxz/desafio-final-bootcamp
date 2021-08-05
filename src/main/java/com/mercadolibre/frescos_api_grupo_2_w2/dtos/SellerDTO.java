package com.mercadolibre.frescos_api_grupo_2_w2.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import lombok.Data;
import java.util.List;

@Data
public class SellerDTO extends UserDTO{
    private String cnpj;

    @JsonIgnore
    private List<Product> products;
}
