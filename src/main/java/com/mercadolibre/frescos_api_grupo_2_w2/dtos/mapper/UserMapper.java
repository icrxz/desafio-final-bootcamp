package com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.UserResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserResponse entityToResponse(User user) {
        return new UserResponse(user.getUserId(), user.getEmail(), user.getRole());
    }

    public static List<UserResponse> entityListToResponseList(List<User> users) {
        return users.stream().map(UserMapper::entityToResponse).collect(Collectors.toList());
    }
}
