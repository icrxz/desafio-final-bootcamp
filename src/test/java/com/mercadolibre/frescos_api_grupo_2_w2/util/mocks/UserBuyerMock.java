package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.BuyerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Buyer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

public class UserBuyerMock {
    public static Long userId = 1L;
    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static Buyer validBuyer (Long id) {
        Buyer buyerMock = new Buyer();
        buyerMock.setEmail("buyer@email.com");
        buyerMock.setPassword(encoder.encode("123"));
        buyerMock.setCpf("111.111.111-11");
        buyerMock.setRole("BUYER");
        buyerMock.setPurchaseOrders(new ArrayList<>());
        buyerMock.setUserId(id);

        return buyerMock;
    }

    public static Buyer validBuyer () {
        Buyer buyerMock = new Buyer();
        buyerMock.setEmail("buyer@email.com");
        buyerMock.setPassword(encoder.encode("123"));
        buyerMock.setCpf("111.111.111-11");
        buyerMock.setRole("BUYER");
        buyerMock.setPurchaseOrders(new ArrayList<>());
        buyerMock.setUserId(userId);

        return buyerMock;
    }

    public static BuyerForm validBuyerForm () {
        Buyer buyerMock = validBuyer(null);
        BuyerForm buyerForm = new BuyerForm();

        BeanUtils.copyProperties(buyerMock, buyerForm, "userId");

        return buyerForm;
    }
}
