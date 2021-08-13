package com.mercadolibre.frescos_api_grupo_2_w2.exceptions;

public class BatchNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BatchNotFoundException(String message) {
        super(message);
    }

}
