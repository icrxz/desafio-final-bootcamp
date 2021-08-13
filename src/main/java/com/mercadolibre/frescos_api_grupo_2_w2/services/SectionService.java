package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.SectionMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.SectionResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.InboundOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Section;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ProductNotFoundException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final WarehouseService warehouseService;

    @Autowired
    public SectionService(SectionRepository sectionRepository, WarehouseService warehouseService) {
        this.sectionRepository = sectionRepository;
        this.warehouseService = warehouseService;
    }

    public Section findSectionById(UUID sectionId) {
        Section foundSection = sectionRepository.findById(sectionId).orElse(null);

        if (foundSection == null) {
            throw new ApiException("404", "Section not found with this id", 404);
        }

        return foundSection;
    }

    public SectionResponse createSection(SectionForm sectionForm) {
        Warehouse foundWarehouse = warehouseService.findWarehouseById(UUID.fromString(sectionForm.getWarehouseId()));

        Section newSection = Section.builder()
                .warehouse(foundWarehouse)
                .maxCapacity(sectionForm.getMaxCapacity())
                .productType(sectionForm.getProductType())
                .build();

        Section section = sectionRepository.save(newSection);
        return SectionMapper.entityToResponse(section);
    }

    public Long getSectionCurrentSize(UUID sectionId) {
        Section section = findSectionById(sectionId);

        List<Batch> batches = section.getOrders().stream().map(InboundOrder::getBatchStock).flatMap(Collection::stream).collect(Collectors.toList());

        return section.getMaxCapacity() - batches.stream().mapToLong(Batch::getCurrentQuantity).sum();
    }
}
