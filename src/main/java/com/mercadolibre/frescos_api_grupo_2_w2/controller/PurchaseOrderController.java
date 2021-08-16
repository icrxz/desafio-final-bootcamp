package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.services.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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
    public ResponseEntity<PurchaseOrderResponse> createPurchaseOrder(@RequestBody @Valid PurchaseOrderForm purchaseOrderForm) {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderForm);

        return new ResponseEntity<>(purchaseOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PurchaseOrderResponse> findPurchaseOrderById(@RequestParam UUID orderId) {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.findPurchaseOrderById(orderId);

        return new ResponseEntity<>(purchaseOrder, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PurchaseOrderResponse> updatePurchaseOrder(
            @RequestParam UUID orderId,
            @RequestBody @Valid PurchaseOrderForm purchaseOrderForm
    ) {
        PurchaseOrderResponse purchaseOrder = purchaseOrderService.updatePurchaseOrder(orderId, purchaseOrderForm);

        return new ResponseEntity<>(purchaseOrder, HttpStatus.OK);
    }

    @PutMapping("/return-order")
    public ResponseEntity<PurchaseOrderResponse> returnPurchaseOrder(@RequestParam UUID orderId, Authentication authentication) {
        PurchaseOrderResponse returnedPurchaseOrder = purchaseOrderService.returnPurchaseOrder(orderId, authentication.getName());

        return new ResponseEntity<>(returnedPurchaseOrder, HttpStatus.OK);
    }

    @GetMapping("/return-order")
    public ResponseEntity<List<PurchaseOrderResponse>> getReturnedPurchaseOrder() {
        List<PurchaseOrderResponse> returnedPurchaseOrders = purchaseOrderService.getReturnedPurchaseOrders();

        return new ResponseEntity<>(returnedPurchaseOrders, HttpStatus.OK);
    }

}
