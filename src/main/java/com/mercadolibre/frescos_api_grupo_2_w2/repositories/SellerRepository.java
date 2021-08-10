package com.mercadolibre.frescos_api_grupo_2_w2.repositories;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
