package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SellerForm extends UserForm {
    private String cnpj;
}
