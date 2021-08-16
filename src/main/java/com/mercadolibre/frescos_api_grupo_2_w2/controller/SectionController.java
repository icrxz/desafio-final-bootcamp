package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.SectionMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.SectionResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sections")
public class SectionController {
    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(@RequestBody @Valid SectionForm sectionForm) {
        SectionResponse newSection = sectionService.createSection(sectionForm);

        return new ResponseEntity<>(newSection, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<SectionResponse> findBatchBySection(@RequestParam UUID sectionId) {

        SectionResponse sectionResponse = SectionMapper.entityToResponse(sectionService.findSectionById(sectionId));

        return new ResponseEntity<>(sectionResponse, HttpStatus.OK);
    }

}
