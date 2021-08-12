package com.mercadolibre.frescos_api_grupo_2_w2.entities.constraints;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class PurchaseOrderProductId implements Serializable {
    private UUID purchaseOrder;
    private UUID product;
}
