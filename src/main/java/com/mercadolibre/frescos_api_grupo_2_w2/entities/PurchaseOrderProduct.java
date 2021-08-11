package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.constraints.PurchaseOrderProductId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PurchaseOrderProductId.class)
public class PurchaseOrderProduct {
    @Id
    @ManyToOne
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "purchaseOrderId")
    private PurchaseOrder purchaseOrder;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;

    private long quantity;
}
