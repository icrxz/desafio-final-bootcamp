package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SellerDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SupervisorDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.UserDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO) {
        User newUser = null;
        if (userDTO instanceof SellerDTO) {
            newUser = new Seller();
            BeanUtils.copyProperties(userDTO, newUser);
        } else if (userDTO instanceof SupervisorDTO) {
            newUser = new Supervisor();
            BeanUtils.copyProperties(userDTO, newUser);
        } else {
            // TODO exception
        }
        return this.userRepository.save(newUser);
    }
}
