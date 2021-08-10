package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import lombok.Data;
import java.util.List;

@Data
public class SellerForm extends UserForm {
    private String cnpj;

    @JsonIgnore
    private List<Product> products;
}
