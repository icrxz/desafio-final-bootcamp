package com.mercadolibre.frescos_api_grupo_2_w2.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SectionDTO {
    @NotNull
    @Min(value = 0)
    private long maxCapacity;

    @NotNull
    private String productType;

    @NotNull
    private String warehouseId;

    public long getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public SectionDTO() {
    }

    public SectionDTO(long maxCapacity, String productType, String warehouseId) {
        this.maxCapacity = maxCapacity;
        this.productType = productType;
        this.warehouseId = warehouseId;
    }
}
