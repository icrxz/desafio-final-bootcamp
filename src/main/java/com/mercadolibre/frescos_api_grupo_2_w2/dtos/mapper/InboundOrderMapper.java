package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.InboundOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.InboundOrder;

import java.util.List;

public class InboundOrderMapper {
    public static InboundOrderResponse inboundOrderToResponse(InboundOrder inboundOrder, List<BatchForm> failedBatches, List<String> errorMessages) {
        return new InboundOrderResponse(
                inboundOrder.getDate(),
                inboundOrder.getNumber(),
                inboundOrder.getSection().getSectionId(),
                inboundOrder.getSection().getWarehouse().getWarehouseId(),
                BatchMapper.batchListToListResponse(inboundOrder.getBatchStock()),
                BatchMapper.batchFormListToListResponse(failedBatches),
                errorMessages
        );
    }
}
