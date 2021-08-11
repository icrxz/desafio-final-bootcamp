package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.WarehouseRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.WarehouseMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

    @InjectMocks
    private WarehouseService warehouseService;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private SupervisorService supervisorService;

    @BeforeEach
    public void setUp() throws Exception {
        warehouseService = new WarehouseService(warehouseRepository, supervisorService);
    }

    @Test
    @DisplayName("should return a warehouse if ID succeeds")
    void findWarehouseByIdTest () {
        UUID warehouseId = UUID.fromString("2b854498-fa35-11eb-9a03-0242ac130003");
        given(warehouseRepository.findById(warehouseId)).willReturn(java.util.Optional.of(WarehouseMock.create()));
        Warehouse warehouseTest = warehouseService.findWarehouseById(warehouseId);

        assertEquals(warehouseId, warehouseTest.getWarehouseId());

    }


}