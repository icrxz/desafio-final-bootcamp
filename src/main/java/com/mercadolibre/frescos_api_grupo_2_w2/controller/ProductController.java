package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.ProductDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity createProduct(@RequestBody @Valid ProductDTO productDTO) {
        Product newProduct = productService.createProduct(productDTO);

        return new ResponseEntity(newProduct, HttpStatus.CREATED);
    }
}
