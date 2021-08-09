package com.mercadolibre.frescos_api_grupo_2_w2.repositories;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
}
