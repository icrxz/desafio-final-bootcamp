package com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderStatusEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseOrderForm {
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @NotNull
    @JsonProperty("buyer_id")
    private long buyerId;

    @NotNull
    private OrderStatusEnum status;

    @Valid
    @NotNull
    private List<PurchaseOrderProductsForm> products = new ArrayList<>();
}
