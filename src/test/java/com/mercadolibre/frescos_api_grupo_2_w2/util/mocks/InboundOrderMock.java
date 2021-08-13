package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrder.InboundOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.InboundOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class InboundOrderMock {

    public static InboundOrderForm validInboundOrderForm () {
        InboundOrderForm form = new InboundOrderForm();
        form.setSection(SectionMock.validInboundOrderSectionForm());
        form.setOrderDate(LocalDate.now());
        form.setOrderNumber(1L);
        form.setBatchStock(Arrays.asList(BatchMock.validBatchForm()));
        return form;
    }

    public static InboundOrder validInboundOrder () {
        InboundOrder createdInboundOrder = new InboundOrder();
        createdInboundOrder.setSection(SectionMock.validSection());
        createdInboundOrder.setDate(LocalDate.now());
        createdInboundOrder.setNumber(1L);
        createdInboundOrder.setBatchStock(Arrays.asList(BatchMock.validBatch(null)));
        return createdInboundOrder;
    }
}
