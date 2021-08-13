package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.InboundOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.InboundOrderRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SupervisorRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class InboundOrderServiceTest {
    @InjectMocks
    private InboundOrderService inboundOrderService;

    @Mock
    private InboundOrderRepository inboundOrderRepository;

    @Mock
    private BatchService batchService;

    @Mock
    private WarehouseService warehouseService;

    @Mock
    private SectionService sectionService;

    @BeforeEach
    public void setUp() throws Exception {
        inboundOrderRepository.deleteAll();
        inboundOrderService = new InboundOrderService(inboundOrderRepository, batchService, warehouseService, sectionService);
    }

    @Test
    @DisplayName("should return a InboundOrder if createInboundOrder succeeds")
    void createInboundOrder_succeeds() {
        //arrange

        //form
        InboundOrderForm form = InboundOrderMock.validInboundOrderForm();

        //product
       Product product = ProductMock.validProduct();

        //batchStock
        BatchForm batch = BatchMock.validBatchForm();

        form.setBatchStock(
                Arrays.asList(batch)
        );

        //Batch createdBatch
        Batch createdBatch = BatchMock.validBatch();

        InboundOrder newInboundOrder = InboundOrder.builder()
                .batchStock(Arrays.asList(createdBatch))
                .date(form.getOrderDate())
                .number(form.getOrderNumber())
                .section(SectionMock.validSection())
                .build();

        given(sectionService.findSectionById(SectionMock.sectionId)).willReturn(SectionMock.validSection());
        given(batchService.createBatch(batch, SectionMock.validSection())).willReturn(createdBatch);
        given(inboundOrderRepository.save(newInboundOrder)).willReturn(InboundOrderMock.validInboundOrder());

        InboundOrderResponse response =  this.inboundOrderService.createInboundOrder(form);
    }
}
