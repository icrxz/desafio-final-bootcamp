package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrder.InboundOrderForm;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private ProductService productService;

    @Mock
    private SectionService sectionService;


    @BeforeEach
    public void setUp() throws Exception {
        inboundOrderRepository.deleteAll();
        inboundOrderService = new InboundOrderService(inboundOrderRepository, batchService, sectionService);
    }

    @Test
    @DisplayName("should return a InboundOrder if createInboundOrder succeeds")
    void createInboundOrder_succeeds() {
        //arrange
        InboundOrderForm form = InboundOrderMock.validInboundOrderForm();
        BatchForm batch = BatchMock.validBatchForm();

        form.setBatchStock(Arrays.asList(batch));

        Section createdSection = SectionMock.validSection();
        Product product = ProductMock.validProduct(null);
        Batch createdBatch = BatchMock.validBatch(product);
        InboundOrder newInboundOrder = InboundOrder.builder()
                .batchStock(Arrays.asList(createdBatch))
                .date(form.getOrderDate())
                .number(form.getOrderNumber())
                .section(createdSection)
                .build();

        given(sectionService.findSectionById(SectionMock.sectionId)).willReturn(createdSection);
        given(batchService.createBatch(any(), any(), any())).willReturn(createdBatch);
        given(inboundOrderRepository.save(any())).willReturn(newInboundOrder);

        InboundOrderResponse response = this.inboundOrderService.createInboundOrder(form);

        assertEquals(response.getOrderNumber(), form.getOrderNumber());
        assertEquals(response.getOrderDate(), form.getOrderDate());
        assertEquals(response.getWarehouseCode().toString(), form.getSection().getWarehouseCode());
        assertEquals(response.getSectionCode().toString(), form.getSection().getSectionCode());
        assertFalse(response.getSuccessBatches().isEmpty());
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

    @Test
    @DisplayName("should failed batches if quantity is bigger than the section stock")
    void createInboundOrder_failsWithoutStock() {
        InboundOrderForm form = InboundOrderMock.validInboundOrderForm();

        Section createdSection = SectionMock.validSection();
        createdSection.setMaxCapacity(10);
        Product product = ProductMock.validProduct(null);
        InboundOrder newInboundOrder = InboundOrder.builder()
                .batchStock(new ArrayList<>())
                .date(form.getOrderDate())
                .number(form.getOrderNumber())
                .section(createdSection)
                .build();

        BatchForm batch = BatchMock.validBatchForm();
        batch.setInitialQuantity(1000);
        batch.setCurrentQuantity(1000);
        batch.setProductId(product.getProductId().toString());
        form.setBatchStock(Arrays.asList(batch));

        given(sectionService.findSectionById(SectionMock.sectionId)).willReturn(createdSection);
        given(batchService.createBatch(batch, createdSection, newInboundOrder)).willThrow(new ApiException("400", "Product and Section mut be of the same type", 400));
        given(inboundOrderRepository.save(any())).willReturn(newInboundOrder);

        InboundOrderResponse response = this.inboundOrderService.createInboundOrder(form);

        assertEquals(response.getOrderNumber(), form.getOrderNumber());
        assertTrue(response.getSuccessBatches().isEmpty());
        assertFalse(response.getErrorMessages().isEmpty());
        assertFalse(response.getFailedBatches().isEmpty());
    }

    @Test
    @DisplayName("should failed batches if quantity is bigger than the section stock")
    void updateInboundOrder_failsWithoutStock() {
        InboundOrderForm form = InboundOrderMock.validInboundOrderForm();

        Section createdSection = SectionMock.validSection();
        createdSection.setMaxCapacity(10);
        Product product = ProductMock.validProduct(null);
        InboundOrder newInboundOrder = InboundOrder.builder()
                .batchStock(new ArrayList<>())
                .date(form.getOrderDate())
                .number(form.getOrderNumber())
                .section(createdSection)
                .build();

        BatchForm batch = BatchMock.validBatchForm();
        batch.setInitialQuantity(1000);
        batch.setCurrentQuantity(1000);
        batch.setProductId(product.getProductId().toString());
        form.setBatchStock(Arrays.asList(batch));

        given(sectionService.findSectionById(SectionMock.sectionId)).willReturn(createdSection);
        given(batchService.createBatch(batch, createdSection, newInboundOrder)).willThrow(new ApiException("400", "Product and Section mut be of the same type", 400));
        given(inboundOrderRepository.findById(newInboundOrder.getNumber())).willReturn(Optional.of(newInboundOrder));
        given(inboundOrderRepository.save(any())).willReturn(newInboundOrder);

        InboundOrderResponse response = this.inboundOrderService.updateInboundOrder(newInboundOrder.getNumber(), form);

        assertEquals(response.getOrderNumber(), form.getOrderNumber());
        assertTrue(response.getSuccessBatches().isEmpty());
        assertFalse(response.getErrorMessages().isEmpty());
        assertFalse(response.getFailedBatches().isEmpty());
    }
}
