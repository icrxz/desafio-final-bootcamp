package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.InboundOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Section;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderBatch;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ProductNotFoundException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BatchService {
    private final BatchRepository batchRepository;
    private final ProductService productService;
    private final SectionService sectionService;

    @Autowired
    public BatchService(BatchRepository batchRepository, ProductService productService, SectionService sectionService) {
        this.batchRepository = batchRepository;
        this.productService = productService;
        this.sectionService = sectionService;
    }

    private void checkSection(Section section, Batch batch) {
        Long sectionCurrentSize = sectionService.getSectionCurrentSize(section.getSectionId());

        if (section.getProductType() != batch.getProduct().getType()) {
            throw new ApiException("400", "Product and Section mut be of the same type", 400);
        } else if (sectionCurrentSize - batch.getCurrentQuantity() < 0) {
            throw new ApiException("400", "Section don't have enough capacity", 400);
        }
    }

    public Batch createBatch(BatchForm batchForm, Section section, InboundOrder inboundOrder) {
        Product foundProduct = productService.findProductById(UUID.fromString(batchForm.getProductId()));

        Batch batch = Batch.builder()
                .currentQuantity(batchForm.getCurrentQuantity())
                .currentTemperature(batchForm.getCurrentTemperature())
                .dueDate(batchForm.getDueDate())
                .initialQuantity(batchForm.getInitialQuantity())
                .manufacturingDate(batchForm.getManufacturingDate())
                .manufacturingTime(batchForm.getManufacturingTime())
                .minimumTemperature(batchForm.getMinimumTemperature())
                .number(batchForm.getBatchNumber())
                .inboundOrder(inboundOrder)
                .product(foundProduct)
                .build();

        checkSection(section, batch);

        return batchRepository.save(batch);
    }

    public Batch updateBatch(BatchForm batchForm, Long batchId, Section section) {
        Product foundProduct = productService.findProductById(UUID.fromString(batchForm.getProductId()));
        Batch foundBatch = findBatchById(batchId);

        foundBatch.setCurrentQuantity(batchForm.getCurrentQuantity());
        foundBatch.setCurrentTemperature(batchForm.getCurrentTemperature());
        foundBatch.setDueDate(batchForm.getDueDate());
        foundBatch.setInitialQuantity(batchForm.getInitialQuantity());
        foundBatch.setManufacturingDate(batchForm.getManufacturingDate());
        foundBatch.setManufacturingTime(batchForm.getManufacturingTime());
        foundBatch.setMinimumTemperature(batchForm.getMinimumTemperature());
        foundBatch.setNumber(batchForm.getBatchNumber());
        foundBatch.setProduct(foundProduct);

        checkSection(section, foundBatch);

        return batchRepository.save(foundBatch);
    }

    public Batch findBatchById(Long batchId) {
        return batchRepository.findById(batchId).orElse(null);
    }


    public List<Batch> findBatchesByProduct(UUID productId) {
        List<Batch> batchList = batchRepository.findBatchesByProduct_productId(productId);

        if (batchList.size() <= 0) {
            throw new ProductNotFoundException("Product not found in Batch");
        }
        return batchList;
    }

    public List<Batch> findBatchesByProductOrder(UUID productId, OrderBatch orderBatch) {

        List<Batch> batchList = new ArrayList<>();
        if (orderBatch == OrderBatch.C) {
           batchList = batchRepository.findBatchesByProduct_productIdOrderByCurrentQuantityAsc(productId);
        } else if (orderBatch == OrderBatch.F) {
            batchList = batchRepository.findBatchesByProduct_productIdOrderByDueDateAsc(productId);
        } else {
            if (productId == null && orderBatch == null) {
               throw new ProductNotFoundException("Product not found in Batch");
            }
        }

        return batchList;
    }

    public List<Batch> dueDateBatch(long days) {
        LocalDate date = LocalDate.now().plusDays(days);
        return this.batchRepository.findByDueDateLessThanEqualOrderByDueDateAsc(date);
    }

    public List<Batch> dueDateAndProductTypeBatch(Long day, ProductTypeEnum type) {
        LocalDate date = LocalDate.now().plusDays(day);
        return this.batchRepository.findDueDateLessAndProductType(date, type);
    }
}
