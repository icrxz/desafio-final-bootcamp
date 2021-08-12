package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;

import java.time.LocalDate;
import java.util.Optional;

public class BatchMock {

    public static BatchForm validBatchForm () {
        BatchForm batch = new BatchForm();
        batch.setCurrentQuantity(1);
        batch.setCurrentTemperature(100F);
        batch.setDueDate(LocalDate.now());
        batch.setManufacturingDate(LocalDate.now());
        batch.setInitialQuantity(1);
        batch.setMinimumTemperature(100F);
        batch.setProductId(ProductMock.productID.toString());
        return batch;
    }

    public static Batch validBatch () {
        Batch createdBatch = new Batch();
        createdBatch.setBatchId(1);
        createdBatch.setCurrentQuantity(1);
        createdBatch.setCurrentTemperature(100F);
        createdBatch.setDueDate(LocalDate.now());
        createdBatch.setManufacturingDate(LocalDate.now());
        createdBatch.setInitialQuantity(100);
        createdBatch.setMinimumTemperature(100F);
        createdBatch.setProduct(ProductMock.validProduct());
        return createdBatch;
    }
}
