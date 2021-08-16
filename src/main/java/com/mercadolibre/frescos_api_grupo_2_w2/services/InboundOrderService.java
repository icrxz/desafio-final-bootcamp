package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrder.InboundOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.InboundOrderMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.InboundOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
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
    private final SectionService sectionService;

    @Autowired
    public InboundOrderService(
            InboundOrderRepository inboundOrderRepository,
            BatchService batchService,
            SectionService sectionService
    ) {
        this.inboundOrderRepository = inboundOrderRepository;
        this.batchService = batchService;
        this.sectionService = sectionService;
    }

    public InboundOrderResponse createInboundOrder(InboundOrderForm inboundOrderForm) {
        Section section = sectionService.findSectionById(UUID.fromString(inboundOrderForm.getSection().getSectionCode()));
        List<Batch> successBatches = new ArrayList<>();
        List<BatchForm> failedBatches = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        InboundOrder newInboundOrder = InboundOrder.builder()
                .date(inboundOrderForm.getOrderDate())
                .number(inboundOrderForm.getOrderNumber())
                .section(section)
                .build();
        InboundOrder createdInboundOrder = inboundOrderRepository.save(newInboundOrder);

        inboundOrderForm.getBatchStock().forEach(batch -> {
            try {
                Batch createdBatch = batchService.createBatch(batch, section, createdInboundOrder);

                successBatches.add(createdBatch);
            } catch (Exception ex) {
                failedBatches.add(batch);
                errorMessages.add(String.format("Error in batch %d: %s", batch.getBatchNumber(), ex.getMessage()));
            }
        });

        createdInboundOrder.setBatchStock(successBatches);

        return InboundOrderMapper.inboundOrderToResponse(createdInboundOrder, failedBatches, errorMessages);
    }

    public InboundOrderResponse updateInboundOrder(Long inboundOrderId, InboundOrderForm inboundOrderForm) {
        Section section = sectionService.findSectionById(UUID.fromString(inboundOrderForm.getSection().getSectionCode()));
        InboundOrder foundInboundOrder = inboundOrderRepository.findById(inboundOrderId).orElse(null);
        List<Batch> successBatches = new ArrayList<>();
        List<BatchForm> failedBatches = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        if (foundInboundOrder == null)
            throw new ApiException("404", "Inbound Order not found", 404);

        inboundOrderForm.getBatchStock().forEach(batch -> {
            try {
                Batch foundBatch = batchService.findBatchById(batch.getBatchNumber());
                Batch newBatch;

                if (foundBatch == null) {
                    newBatch = batchService.createBatch(batch, section, foundInboundOrder);
                } else {
                    newBatch = batchService.updateBatch(batch, foundBatch.getBatchId(), section);
                }

                successBatches.add(newBatch);
            } catch (Exception ex) {
                failedBatches.add(batch);
                errorMessages.add(String.format("Error in batch %d: %s", batch.getBatchNumber(), ex.getMessage()));
            }
        });

        foundInboundOrder.setSection(section);
        foundInboundOrder.setDate(inboundOrderForm.getOrderDate());

        InboundOrder updatedInboundOrder  = inboundOrderRepository.save(foundInboundOrder);
        updatedInboundOrder.setBatchStock(successBatches);

        return InboundOrderMapper.inboundOrderToResponse(updatedInboundOrder, failedBatches, errorMessages);
    }
}
