package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_order")
public class PurchaseOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID purchaseOrderId;
    private LocalDate date;
    private OrderStatusEnum status;

    @ManyToOne
    private Buyer buyer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrder")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<PurchaseOrderProduct> product = new ArrayList<>();
}
