package com.mercadolibre.frescos_api_grupo_2_w2.controller;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SellerDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SupervisorDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<User> createSeller(@Valid @RequestBody SellerDTO user) {
        user.setPassword(encoder.encode(user.getPassword()));

       return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PostMapping("/supervisor")
    public ResponseEntity<User> createSupervisor(@Valid @RequestBody SupervisorDTO user) {
        user.setPassword(encoder.encode(user.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }
}
