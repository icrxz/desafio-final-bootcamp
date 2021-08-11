package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class PurchaseOrderProductsForm {
    @NotNull
    @JsonProperty("product_id")
    private UUID productId;

    @NotNull
    @Min(value = 0)
    private int quantity;
}
