package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.SectionResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SectionRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SectionService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.WarehouseService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.WarehouseMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {
    @InjectMocks
    private SectionService sectionService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private WarehouseService warehouseService;


    @BeforeEach
    public void setUp() throws Exception {
        sectionRepository.deleteAll();
        sectionService = new SectionService(sectionRepository, warehouseService);
    }

    @Test
    @DisplayName("should return a Section if findSectonBy succeeds")
    void findSectionById_succeeds() {
        //arrange
        UUID sectionId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");
        Section section = new Section();
        section.setSectionId(sectionId);
        section.setMaxCapacity(100);
        section.setProductType(ProductTypeEnum.CARNES);

        given(this.sectionRepository.findById(sectionId)).willReturn(Optional.of(section));

        // act
        Section findSection = this.sectionService.findSectionById(sectionId);

        // assert
        assertThat(findSection.getSectionId()).isEqualTo(sectionId);
    }

    @Test
    @DisplayName("should throws if id not belong to any Section")
    void findSectionById_sectionNotFound() {
        //arrange
        UUID sectionId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

        assertThatThrownBy(() -> sectionService.findSectionById(sectionId))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("should return a Section if createSection succeeds")
    void createSection_succeeds() {

        //arrange
        Warehouse warehouse = WarehouseMock.validWarehouse();

        //mock Section
        SectionForm sectionForm = new SectionForm();
        sectionForm.setMaxCapacity(100);
        sectionForm.setWarehouseId(warehouse.getWarehouseId().toString());
        sectionForm.setProductType(ProductTypeEnum.CARNES);

        UUID sectionId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");
        Section sectionResponse = new Section();
        sectionResponse.setSectionId(sectionId);
        sectionResponse.setWarehouse(warehouse);
        sectionResponse.setMaxCapacity(100);

        Section newSection = Section.builder()
                .warehouse(warehouse)
                .maxCapacity(sectionForm.getMaxCapacity())
                .productType(sectionForm.getProductType())
                .build();

        sectionResponse.setProductType(ProductTypeEnum.CARNES);

        given(this.warehouseService.findWarehouseById(warehouse.getWarehouseId())).willReturn(warehouse);
        given(this.sectionRepository.save(newSection)).willReturn(sectionResponse);

        SectionResponse createdSection = this.sectionService.createSection(sectionForm);

        // assert
        assertThat(createdSection.getSectionId()).isEqualTo(sectionId);
        assertThat(createdSection.getMaxCapacity()).isEqualTo(100);
        assertThat(createdSection.getWarehouseId()).isEqualTo(warehouse.getWarehouseId());
        assertThat(createdSection.getType()).isEqualTo(ProductTypeEnum.CARNES);
    }

    @Test
    @DisplayName("should throws if warehouse are not found")
    void createSection_warehouseNotFound() {
        UUID sectionId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

        assertThatThrownBy(() -> sectionService.findSectionById(sectionId))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("should return current size by section provided")
    void getSectionCurrentSize_succeeds() {
        UUID sectionId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

        Section section = new Section();
        section.setSectionId(sectionId);
        section.setMaxCapacity(100);
        section.setProductType(ProductTypeEnum.CARNES);
        section.setWarehouse(WarehouseMock.validWarehouse());
        section.setOrders(new ArrayList<>());

        given(this.sectionRepository.findById(sectionId)).willReturn(Optional.of(section));
        Long sectionCurrentSize = this.sectionService.getSectionCurrentSize(sectionId);
        assertThat(sectionCurrentSize).isEqualTo(100);
    }

    @Test
    @DisplayName("should throws if Section are not found")
    void getSectionCurrentSize_sectionNotFound() {
        UUID sectionId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

        assertThatThrownBy(() -> sectionService.getSectionCurrentSize(sectionId))
                .isInstanceOf(ApiException.class);
    }
}
