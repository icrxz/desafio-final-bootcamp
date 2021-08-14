package com.mercadolibre.frescos_api_grupo_2_w2.repositories;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findBatchesByProduct(Product product);
    List<Batch> findBatchesByProduct_productId(UUID productId);
    List<Batch> findByDueDateLessThanEqualOrderByDueDateAsc(LocalDate dueDate);

    @Query("select b " +
             "from Batch b " +
            "where b.dueDate <= :dueDate " +
            " and b.product.type = :productType " +
            "order by b.dueDate asc")
    List<Batch> findDueDateLessAndProductType(LocalDate dueDate, ProductTypeEnum productType);

    List<Batch> findBatchesByProduct_productIdOrderByCurrentQuantityAsc (UUID productId);

    List<Batch> findBatchesByProduct_productIdOrderByDueDateAsc (UUID productId);
}
