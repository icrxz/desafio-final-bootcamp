package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SellerDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import org.springframework.beans.BeanUtils;

import java.util.Optional;
import java.util.OptionalLong;

public class UserSellerMock {
    public static Seller validSeller (Optional<Long> id) {
        Seller sellerMock = new Seller();
        sellerMock.setEmail("any_email@email.com");
        sellerMock.setPassword("any_password");
        sellerMock.setCnpj("any_cnpj");
        sellerMock.setRole("SELLER");

        if (id != null) {
            sellerMock.setUserId(id.get());
        }
        return sellerMock;
    }

    public static SellerDTO validSellerDTO () {
        Seller sellerMock = validSeller(null);
        SellerDTO sellerDTO = new SellerDTO();
        BeanUtils.copyProperties(sellerMock, sellerDTO, "userId");
        return sellerDTO;
    }
}