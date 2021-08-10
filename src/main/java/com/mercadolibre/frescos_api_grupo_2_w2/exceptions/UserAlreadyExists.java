package com.mercadolibre.frescos_api_grupo_2_w2.exceptions;

public class UserAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExists(String message) {
        super(message);
    }

    public UserAlreadyExists(Exception e) {
        super(e);
    }
}