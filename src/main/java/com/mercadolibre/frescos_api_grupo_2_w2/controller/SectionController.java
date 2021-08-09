package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SectionDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Section;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/sections")
public class SectionController {
    private final SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity createSection(@RequestBody @Valid SectionDTO sectionDTO) {
        Section newSection = sectionService.createSection(sectionDTO);

        return new ResponseEntity(newSection, HttpStatus.CREATED);
    }
}
