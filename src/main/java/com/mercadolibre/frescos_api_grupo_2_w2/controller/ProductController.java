package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity createProduct(@RequestBody @Valid ProductForm productForm) {
        ProductResponse newProduct = productService.createProduct(productForm);

        return new ResponseEntity(newProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getAllProducts() {
        List<ProductResponse> productsList = productService.getAllProducts();

        return new ResponseEntity(productsList, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity getProductsByCategory(@RequestParam String productType) {
        List<ProductResponse> productsListByType = productService.getProductsByType(ProductTypeEnum.toEnum(productType));

        return new ResponseEntity(productsListByType, HttpStatus.OK);
    }
}
