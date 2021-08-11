package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrderSectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.SectionResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Section;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

public class SectionMock {
    public static UUID sectionId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

    public static SectionForm validSectionForm () {
        SectionForm sectionForm = new SectionForm();
        sectionForm.setMaxCapacity(100);
        sectionForm.setWarehouseId(WarehouseMock.validWarehouse().getWarehouseId().toString());
        sectionForm.setProductType(ProductTypeEnum.CARNES);
        return sectionForm;
    }

    public static Section validSection () {
        Section section = new Section();
        section.setSectionId(sectionId);
        section.setWarehouse(WarehouseMock.validWarehouse());
        section.setMaxCapacity(100);
        section.setProductType(ProductTypeEnum.CARNES);
        return section;
    }

    public static InboundOrderSectionForm validInboundOrderSectionForm() {
        InboundOrderSectionForm form = new InboundOrderSectionForm();
        form.setSectionCode(sectionId.toString());
        form.setWarehouseCode(WarehouseMock.warehouseId.toString());
        return form;
    }
}
