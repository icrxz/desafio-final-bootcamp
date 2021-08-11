package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.ProductMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ProductNotFoundException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final SellerService sellerService;

    @Autowired
    public ProductService(ProductRepository productRepository, SellerService sellerService) {
        this.productRepository = productRepository;
        this.sellerService = sellerService;
    }

    public ProductResponse createProduct(ProductForm productForm) {
        Seller foundSeller = sellerService.findSeller(productForm.getSellerId());

        Product product = Product.builder()
                .name(productForm.getName())
                .seller(foundSeller)
                .type(productForm.getType())
                .value(productForm.getValue())
                .build();
        Product createdProduct = productRepository.save(product);

        return ProductMapper.entityToResponse(createdProduct);
    }

    public Product findProductById(UUID productId) {
        Product foundProduct = productRepository.findById(productId).orElse(null);
        if (foundProduct == null) {
            throw new ProductNotFoundException("Product not found with this id");
        }
        return foundProduct;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productsList = productRepository.findAll();

        if (productsList.size() <= 0) {
            throw new ApiException("404", "Product list not found", 404);
        }

        return ProductMapper.entityListToResponseList(productsList);
    }

    public List<ProductResponse> getProductsByType(ProductTypeEnum productType) {
        List<Product> productsList = productRepository.findProductByType(productType);

        if (productsList.size() <= 0) {
            throw new ApiException("404", "Product list not found", 404);
        }

        return ProductMapper.entityListToResponseList(productsList);
    }

    public long getProductQuantity(UUID productId) {
        Product product = findProductById(productId);

        return product.getBatches().stream().mapToLong(Batch::getCurrentQuantity).sum();
    }
}
