package com.mercadolibre.frescos_api_grupo_2_w2.repositories;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Buyer;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Optional<Buyer> findByEmail(String email);
}
