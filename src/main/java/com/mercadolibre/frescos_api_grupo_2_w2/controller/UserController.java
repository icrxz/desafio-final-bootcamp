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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/seller")
    public ResponseEntity<UserResponse> createSeller(@Valid @RequestBody SellerForm user) {
        User createdSeller = userService.createUser(user);
        
       return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.entityToResponse(createdSeller));
    }

    @PostMapping("/supervisor")
    public ResponseEntity<UserResponse> createSupervisor(@Valid @RequestBody SupervisorForm user) {
        User createdSupervisor = userService.createUser(user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.entityToResponse(createdSupervisor));
    }

    @PostMapping("/buyer")
    public ResponseEntity<UserResponse> createBuyer(@Valid @RequestBody BuyerForm user) {
        User createdBuyer = userService.createUser(user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.entityToResponse(createdBuyer));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = UserMapper.entityListToResponseList(userService.getUsers());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
