package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public Seller findSeller(Long sellerId) {
        Seller foundSeller = sellerRepository.findById(sellerId).orElse(null);

        if (foundSeller == null) {
            throw new ApiException("404", "Seller not found with this id", 404);
        }

        return foundSeller;
    }
}
