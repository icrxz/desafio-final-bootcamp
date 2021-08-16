package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.SectionResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Section;

public class SectionMapper {
    public static SectionResponse entityToResponse(Section section) {
        return new SectionResponse(section.getSectionId(), section.getMaxCapacity(), section.getProductType().getDescription(), section.getWarehouse().getWarehouseId());
    }
}
