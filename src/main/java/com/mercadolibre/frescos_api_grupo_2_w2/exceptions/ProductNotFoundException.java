package com.mercadolibre.frescos_api_grupo_2_w2.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message) {
        super(message);
    }

}
