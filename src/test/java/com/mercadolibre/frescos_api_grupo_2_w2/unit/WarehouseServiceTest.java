package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.WarehouseResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.WarehouseRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SupervisorService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.WarehouseService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSupervisorMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.WarehouseMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {
    @InjectMocks
    private WarehouseService warehouseService;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private SupervisorService supervisorService;

    @Test
    @DisplayName("should return a Warehouse if findWarehouseById succeeds")
    void findWarehouseById_succeeds() {
        //arrange
        Warehouse validWarehouse = WarehouseMock.validWarehouse();
        given(warehouseRepository.findById(WarehouseMock.warehouseId)).willReturn(Optional.of(validWarehouse));

        // act
        Warehouse warehouse = this.warehouseService.findWarehouseById(WarehouseMock.warehouseId);

        // assert
        assertThat(warehouse).isEqualTo(validWarehouse);
    }

    @Test
    @DisplayName("should throw if findWarehouseById not find")
    void findSeller_notFoundSeller() {
        //arrange
        given(warehouseRepository.findById(WarehouseMock.warehouseId)).willReturn(Optional.empty());

        // assert
        assertThatThrownBy(() -> warehouseService.findWarehouseById(WarehouseMock.warehouseId))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("should throw if findWarehouseById not find")
    void createWarehouse_succeeds() {
        //arrange
        given(supervisorService.findSupervisor(UserSupervisorMock.validSupervisor().getUserId())).willReturn(UserSupervisorMock.validSupervisor());
        given(warehouseRepository.save(any())).willReturn(WarehouseMock.validWarehouse());

        //act
        WarehouseResponse response = warehouseService.createWarehouse(WarehouseMock.validWarehouseForm());

        // assert
        assertThat(response.getWarehouseId()).isEqualTo(WarehouseMock.validWarehouse().getWarehouseId());
    }

    @Test
    @DisplayName("should throw if findWarehouseById not find")
    void getWarehouses_succeeds() {
        //arrange
        given(warehouseRepository.findAll()).willReturn(Arrays.asList(WarehouseMock.validWarehouse()));

        //act
        List<Warehouse> responseList = warehouseService.getWarehouses();

        // assert
        assertFalse(responseList.isEmpty());
    }
}
