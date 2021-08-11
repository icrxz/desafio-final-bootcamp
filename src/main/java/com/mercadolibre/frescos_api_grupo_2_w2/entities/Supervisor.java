package com.mercadolibre.frescos_api_grupo_2_w2.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Supervisor extends User {

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "supervisor")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Warehouse warehouse;
}
