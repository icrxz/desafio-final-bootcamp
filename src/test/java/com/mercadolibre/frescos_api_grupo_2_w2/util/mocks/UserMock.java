package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;

import java.util.Optional;

public class UserMock {
    public static User validUser (User user) {
        User userMock = new User();
        userMock.setUserId(user.getUserId());
        userMock.setRole(user.getRole());
        userMock.setPassword(user.getPassword());
        userMock.setEmail(user.getEmail());
        return userMock;
    }
}
