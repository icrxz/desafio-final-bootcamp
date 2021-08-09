package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.InboundOrderDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.InboundOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.services.InboundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/fresh-products/inboundorder")
public class InboundOrderController {
    private final InboundOrderService inboundOrderService;

    @Autowired
    public InboundOrderController(InboundOrderService inboundOrderService) {
        this.inboundOrderService = inboundOrderService;
    }

    @PostMapping
    public ResponseEntity createInboundOrder(@RequestBody @Valid InboundOrderDTO inboundOrderDTO) {
        InboundOrder newInboundOrder = inboundOrderService.createInboundOrder(inboundOrderDTO);

        return new ResponseEntity(newInboundOrder, HttpStatus.CREATED);
    }

//    @PutMapping
//    public ResponseEntity updateInboundOrder(@RequestBody @Valid InboundOrderDTO inboundOrderDTO) {
//
//    }
}