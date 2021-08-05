package com.mercadolibre.frescos_api_grupo_2_w2.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class SupervisorDTO extends UserDTO{

    @JsonIgnore
    private Warehouse warehouse;
}
