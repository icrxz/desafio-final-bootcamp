package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Buyer extends User{
    private String cpf;

    @OneToOne
    private Cart cart;

    @OneToMany(mappedBy = "buyer")
    private List<PurchaseOrder> purchaseOrders;
}
