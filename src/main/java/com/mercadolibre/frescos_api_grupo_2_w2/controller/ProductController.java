package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.product.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.BatchMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.BatchCompleteResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderBatch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.services.BatchService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class ProductController {
    private final ProductService productService;
    private final BatchService batchService;

    @Autowired
    public ProductController(
            ProductService productService,
            BatchService batchService
    ) {
        this.productService = productService;
        this.batchService = batchService;
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

    @GetMapping("category/list")
    public ResponseEntity getProductsByCategory(@RequestParam String productType) {
        List<ProductResponse> productsListByType = productService.getProductsByType(ProductTypeEnum.toEnum(productType));

        return new ResponseEntity(productsListByType, HttpStatus.OK);
    }

    @GetMapping("/due-date")
    public ResponseEntity getDueDateByDays(@RequestParam long days) {
        List<Batch> batches = this.batchService.dueDateBatch(days);
        return new ResponseEntity(BatchMapper.batchListToListResponse(batches), HttpStatus.OK);
    }

    @GetMapping("/due-date/list")
    public ResponseEntity getDueDateByDaysAndProductType(@RequestParam Long days, @RequestParam ProductTypeEnum type) {
        List<Batch> batches = this.batchService.dueDateAndProductTypeBatch(days, type);
        return new ResponseEntity(BatchMapper.batchListToListResponse(batches), HttpStatus.OK);
    }
  
    @GetMapping("batch/list")
    public ResponseEntity findBatchByProduct(@RequestParam UUID productId) {

        BatchCompleteResponse batchCompleteResponse = new BatchCompleteResponse();
        batchCompleteResponse.setBatchStock(BatchMapper.batchRolToListResponse(batchService.findBatchesByProduct(productId)));
        batchCompleteResponse.setProductId(productId);

        return new ResponseEntity(batchCompleteResponse, HttpStatus.OK);
    }

    @GetMapping("batch/list/order")
    public ResponseEntity findBatchByProductOrder(@RequestParam(value = "productId") UUID productId,
                                                  @RequestParam(value = "order", defaultValue = "C") OrderBatch orderBatch) {
        BatchCompleteResponse batchCompleteResponse = new BatchCompleteResponse();
        batchCompleteResponse.setBatchStock(BatchMapper.batchRolToListResponse(batchService.findBatchesByProductOrder(productId, orderBatch)));
        batchCompleteResponse.setProductId(productId);

        return new ResponseEntity(batchCompleteResponse, HttpStatus.OK);
    }
}
