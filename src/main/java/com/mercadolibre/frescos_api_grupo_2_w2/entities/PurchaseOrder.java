package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class PurchaseOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID purchaseOrderId;
    private LocalDate date;
    private OrderStatusEnum status;

    @ManyToOne
    private Buyer buyer;

    @ManyToMany
    private List<Product> products;
}
