package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.BatchListResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.BatchResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class BatchMapper {
    public static BatchResponse batchEntityToResponse(Batch batch) {
        return new BatchResponse(
                batch.getNumber(),
                batch.getProduct().getProductId(),
                batch.getCurrentTemperature(),
                batch.getMinimumTemperature(),
                batch.getInitialQuantity(),
                batch.getCurrentQuantity(),
                batch.getManufacturingDate(),
                batch.getManufacturingTime(),
                batch.getDueDate()
        );
    }

    public static List<BatchResponse> batchListToListResponse(List<Batch> batches) {
        return batches.stream().map(BatchMapper::batchEntityToResponse).collect(Collectors.toList());
    }

    public static BatchResponse batchFormToResponse(BatchForm batchForm) {
        return new BatchResponse(
                batchForm.getBatchNumber(),
                UUID.fromString(batchForm.getProductId()),
                batchForm.getCurrentTemperature(),
                batchForm.getMinimumTemperature(),
                batchForm.getInitialQuantity(),
                batchForm.getCurrentQuantity(),
                batchForm.getManufacturingDate(),
                batchForm.getManufacturingTime(),
                batchForm.getDueDate()
        );
    }

    public static List<BatchResponse> batchFormListToListResponse(List<BatchForm> batches) {
        return batches.stream().map(BatchMapper::batchFormToResponse).collect(Collectors.toList());
    }

    public static BatchListResponse batchListEntityToResponse(Batch batch) {
        return new BatchListResponse(
                batch.getNumber(),
                batch.getCurrentQuantity(),
                batch.getDueDate()
        );
    }

    public static List<BatchListResponse> batchRolToListResponse(List<Batch> batches) {
        return batches.stream().map(BatchMapper::batchListEntityToResponse).collect(Collectors.toList());
    }
}
