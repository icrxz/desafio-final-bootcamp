package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.SectionResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.UserAlreadyExists;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SectionRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SupervisorRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.WarehouseRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SectionService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SupervisorService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.WarehouseService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSupervisorMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.WarehouseMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

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

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private SupervisorService supervisorService;

    @Mock
    SupervisorRepository supervisorRepository;

    @BeforeEach
    public void setUp() throws Exception {
        sectionRepository.deleteAll();
        supervisorService = new SupervisorService(supervisorRepository);
        warehouseService = new WarehouseService(warehouseRepository, supervisorService);
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
}
