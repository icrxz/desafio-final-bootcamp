package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.BatchDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.InboundOrderDTO;
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

    public InboundOrder createInboundOrder(InboundOrderDTO inboundOrderDTO) {
        Section section = sectionService.findSectionById(UUID.fromString(inboundOrderDTO.getSection().getSectionCode()));
        Warehouse warehouse = warehouseService.findWarehouseById(UUID.fromString(inboundOrderDTO.getSection().getWarehouseCode()));
        List<Batch> successBatches = new ArrayList<>();
        List<BatchDTO> failedBatches = new ArrayList<>();

        inboundOrderDTO.getBatchStock().forEach(bs -> {
            try {
                Batch createdBatch = batchService.createBatch(bs, section);

                successBatches.add(createdBatch);
            } catch (Exception exception) {
                failedBatches.add(bs);
            }
        });

        InboundOrder newInboundOrder = InboundOrder.builder()
                .batchStock(successBatches)
                .date(inboundOrderDTO.getOrderDate())
                .number(inboundOrderDTO.getOrderNumber())
                .section(section)
                .build();

        return inboundOrderRepository.save(newInboundOrder);
    }
}
