package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

public class UserSellerMock {
    public static Long userId = 1L;
    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static Seller validSeller (Long id) {
        Seller sellerMock = new Seller();
        sellerMock.setEmail("seller@email.com");
        sellerMock.setPassword(encoder.encode("123"));
        sellerMock.setCnpj("any_cnpj");
        sellerMock.setRole("SELLER");
        sellerMock.setProducts(new ArrayList<>());
        sellerMock.setUserId(id);

        return sellerMock;
    }

    public static Seller validSeller () {
        Seller sellerMock = new Seller();
        sellerMock.setEmail("seller@email.com");
        sellerMock.setPassword(encoder.encode("123"));
        sellerMock.setCnpj("any_cnpj");
        sellerMock.setRole("SELLER");
        sellerMock.setProducts(new ArrayList<>());
        sellerMock.setUserId(userId);

        return sellerMock;
    }

    public static SellerForm validSellerForm () {
        Seller sellerMock = validSeller(null);
        SellerForm sellerDTO = new SellerForm();
        BeanUtils.copyProperties(sellerMock, sellerDTO, "userId");
        return sellerDTO;
    }
}
