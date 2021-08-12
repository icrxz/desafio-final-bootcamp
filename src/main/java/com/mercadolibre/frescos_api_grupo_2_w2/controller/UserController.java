package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.BuyerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SupervisorForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.UserMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.UserResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    private final PasswordEncoder encoder;

    public UserController(PasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @PostMapping("/seller")
    public ResponseEntity<User> createSeller(@Valid @RequestBody SellerForm user) {
        user.setPassword(encoder.encode(user.getPassword()));

       return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PostMapping("/supervisor")
    public ResponseEntity<User> createSupervisor(@Valid @RequestBody SupervisorForm user) {
        user.setPassword(encoder.encode(user.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PostMapping("/buyer")
    public ResponseEntity<User> createBuyer(@Valid @RequestBody BuyerForm user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        List<UserResponse> users = UserMapper.entityListToResponseList(userService.getUsers());

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
