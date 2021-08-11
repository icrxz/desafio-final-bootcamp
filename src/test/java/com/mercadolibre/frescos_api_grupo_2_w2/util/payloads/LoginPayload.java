package com.mercadolibre.frescos_api_grupo_2_w2.util.payloads;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginPayload {
    private String email;
    private String password;

    public LoginPayload(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
