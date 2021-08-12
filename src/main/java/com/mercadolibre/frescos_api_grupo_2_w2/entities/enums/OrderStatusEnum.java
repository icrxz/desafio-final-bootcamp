package com.mercadolibre.frescos_api_grupo_2_w2.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    CONFIRMING(0, "Confirmando"),
    SORTING(1, "Separando"),
    TRANSPORT(2, "Transporte"),
    DELIVERED(3, "Entregue");

    private final Integer code;
    private final String description;

    public static OrderStatusEnum toEnum(Integer code) {
        if (code == null) {
            return null;
        }

        for (OrderStatusEnum orderStatus : OrderStatusEnum.values()) {
            if (code.equals(orderStatus.getCode())) {
                return orderStatus;
            }
        }

        throw new IllegalArgumentException("Order Status not valid: " + code);
    }
}
