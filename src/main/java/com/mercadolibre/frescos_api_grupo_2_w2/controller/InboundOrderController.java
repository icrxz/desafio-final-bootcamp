package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrder.InboundOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.InboundOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.services.InboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fresh-products/inboundorder")
public class InboundOrderController {
    private final InboundOrderService inboundOrderService;

    @Autowired
    public InboundOrderController(InboundOrderService inboundOrderService) {
        this.inboundOrderService = inboundOrderService;
    }

    @PostMapping
    public ResponseEntity<InboundOrderResponse> createInboundOrder(@RequestBody @Valid InboundOrderForm inboundOrderForm) {
        InboundOrderResponse newInboundOrder = inboundOrderService.createInboundOrder(inboundOrderForm);

        return new ResponseEntity<>(newInboundOrder, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<InboundOrderResponse> updateInboundOrder(
            @RequestParam Long orderId,
            @RequestBody @Valid InboundOrderForm inboundOrderForm
    ) {
        InboundOrderResponse inboundOrder = inboundOrderService.updateInboundOrder(orderId, inboundOrderForm);

        return new ResponseEntity<>(inboundOrder, HttpStatus.OK);
    }
}
