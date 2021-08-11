package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.ProductMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ProductNotFoundException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
