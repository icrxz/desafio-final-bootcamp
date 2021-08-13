package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.BatchMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.SectionMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.BatchCompleteResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderBatch;
import com.mercadolibre.frescos_api_grupo_2_w2.services.BatchService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fresh-products/batch")
public class BatchController {

    private final BatchService batchService;
    private final SectionService sectionService;

    @Autowired
    public BatchController(BatchService batchService, SectionService sectionService) {
        this.batchService = batchService;
        this.sectionService = sectionService;
    }
}
