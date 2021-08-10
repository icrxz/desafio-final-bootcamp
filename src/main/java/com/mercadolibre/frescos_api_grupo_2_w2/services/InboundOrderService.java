package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.InboundOrderMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.InboundOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.InboundOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InboundOrderService {
    private final InboundOrderRepository inboundOrderRepository;
    private final BatchService batchService;
    private final WarehouseService warehouseService;
    private final SectionService sectionService;

    @Autowired
    public InboundOrderService(
            InboundOrderRepository inboundOrderRepository,
            BatchService batchService,
            WarehouseService warehouseService,
            SectionService sectionService
    ) {
        this.inboundOrderRepository = inboundOrderRepository;
        this.batchService = batchService;
        this.warehouseService = warehouseService;
        this.sectionService = sectionService;
    }

    public InboundOrderResponse createInboundOrder(InboundOrderForm inboundOrderForm) {
        Section section = sectionService.findSectionById(UUID.fromString(inboundOrderForm.getSection().getSectionCode()));
        List<Batch> successBatches = new ArrayList<>();
        List<BatchForm> failedBatches = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        inboundOrderForm.getBatchStock().forEach(batch -> {
            try {
                Batch createdBatch = batchService.createBatch(batch, section);

                successBatches.add(createdBatch);
            } catch (Exception ex) {
                failedBatches.add(batch);
                errorMessages.add(String.format("Error in batch %d: %s", batch.getBatchNumber(), ex.getMessage()));
            }
        });

        InboundOrder newInboundOrder = InboundOrder.builder()
                .batchStock(successBatches)
                .date(inboundOrderForm.getOrderDate())
                .number(inboundOrderForm.getOrderNumber())
                .section(section)
                .build();
        InboundOrder createdInboundOrder = inboundOrderRepository.save(newInboundOrder);

        return InboundOrderMapper.inboundOrderToResponse(createdInboundOrder, failedBatches, errorMessages);
    }
}
