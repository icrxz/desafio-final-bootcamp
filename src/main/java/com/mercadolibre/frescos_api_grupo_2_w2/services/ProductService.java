package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.ProductDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
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

    public Product createProduct(ProductDTO productDTO) {
        Seller foundSeller = sellerService.findSeller(productDTO.getSellerId());

        Product product = Product.builder()
                .name(productDTO.getName())
                .seller(foundSeller)
                .type(productDTO.getType())
                .build();

        return productRepository.save(product);
    }

    public Product findProductById(UUID productId) {
        Product foundProduct = productRepository.findById(productId).orElse(null);

        if (foundProduct == null) {
            throw new ApiException("404", "Product not found with this id", 404);
        }

        return foundProduct;
    }
}
