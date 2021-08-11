package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Section;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Batch createBatch(BatchForm batchForm, Section section) {
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
                .product(foundProduct)
                .build();

        checkSection(section, batch);

        return batchRepository.save(batch);
    }

        return createdBatch;
    }
}
