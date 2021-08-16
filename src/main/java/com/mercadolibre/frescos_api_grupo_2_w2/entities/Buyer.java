package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Buyer extends User{
    private String cpf;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
}
