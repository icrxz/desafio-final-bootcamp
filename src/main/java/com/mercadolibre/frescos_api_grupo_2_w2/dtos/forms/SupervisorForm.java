package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SupervisorForm extends UserForm {

    @JsonIgnore
    private Warehouse warehouse;
}
