package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public abstract class UserForm {
    @JsonIgnore
    private long userId;

    @NotNull(message = "O email deverá ser informado.")
    @NotBlank(message = "O email deverá ser informado.")
    @Email(message = "Formato de email inválido.")
    private String email;

    @NotNull(message = "O password deverá ser informado.")
    @NotBlank(message = "O password deverá ser informado.")
    private String password;
}
