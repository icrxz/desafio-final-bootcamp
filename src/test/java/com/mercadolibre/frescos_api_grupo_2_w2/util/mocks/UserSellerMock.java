package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Optional;

public class UserSellerMock {
    public static Seller validSeller (Optional<Long> id) {
        Seller sellerMock = new Seller();
        sellerMock.setEmail("any_email@email.com");
        sellerMock.setPassword("any_password");
        sellerMock.setCnpj("any_cnpj");
        sellerMock.setRole("SELLER");
        sellerMock.setProducts(new ArrayList<>());

        if (id != null) {
            sellerMock.setUserId(id.get());
        }
        return sellerMock;
    }

    public static SellerForm validSellerForm () {
        Seller sellerMock = validSeller(null);
        SellerForm sellerDTO = new SellerForm();
        BeanUtils.copyProperties(sellerMock, sellerDTO, "userId");
        return sellerDTO;
    }
}
