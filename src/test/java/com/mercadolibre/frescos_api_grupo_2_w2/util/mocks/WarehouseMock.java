package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.WarehouseForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;

import java.util.Optional;
import java.util.UUID;

public class WarehouseMock {
    public static UUID warehouseId = UUID.fromString("2b854498-fa35-11eb-9a03-0242ac130003");

    public static Warehouse validWarehouse () {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(UUID.fromString(warehouseId.toString()));
        warehouse.setSupervisor(UserSupervisorMock.validSupervisor());
        return warehouse;
    }

    public static WarehouseForm validWarehouseForm () {
        WarehouseForm warehouseForm = new WarehouseForm();
        warehouseForm.setSupervisorId(1L);
        return warehouseForm;
    }
}
