package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.InboundOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.InboundOrderRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        InboundOrderForm form = InboundOrderMock.validInboundOrderForm();
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
        assertEquals(response.getOrderNumber(), form.getOrderNumber());
        assertEquals(response.getOrderDate(), form.getOrderDate());
        assertEquals(response.getWarehouseCode().toString(), form.getSection().getWarehouseCode());
        assertEquals(response.getSectionCode().toString(), form.getSection().getSectionCode());
        assertTrue(!response.getSuccessBatches().isEmpty());
        assertTrue(response.getErrorMessages().isEmpty());
        assertTrue(response.getFailedBatches().isEmpty());
    }

    @Test
    @DisplayName("should throw if InboundOrder if section id provided not belong to any section")
    void createInboundOrder_sectionNotFound() {
        given(sectionService.findSectionById(SectionMock.sectionId)).willThrow(new ApiException("404", "Section not found with this id", 404));
        assertThatThrownBy(() -> inboundOrderService.createInboundOrder(InboundOrderMock.validInboundOrderForm()))
                .isInstanceOf(ApiException.class);
    }
}
