package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.services.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fresh-products/orders")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity createPurchaseOrder(@RequestBody @Valid PurchaseOrderForm purchaseOrderForm) {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderForm);

        return new ResponseEntity(purchaseOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity findPurchaseOrderById(@RequestParam UUID orderId) {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.findPurchaseOrderById(orderId);

        return new ResponseEntity(purchaseOrder, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity updatePurchaseOrder(
            @RequestParam UUID orderId,
            @RequestBody @Valid PurchaseOrderForm purchaseOrderForm
    ) {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.updatePurchaseOrder(orderId, purchaseOrderForm);

        return new ResponseEntity(purchaseOrder, HttpStatus.OK);
    }
}
