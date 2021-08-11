package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.payloads.LoginPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest extends ControllerTest{
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        this.userRepository.deleteAll();
    }

    @Test
    void shouldReturnTokenWhenSuccessfulLogin() throws JsonProcessingException {
        // Insert user
        Seller seller = UserSellerMock.validSeller(Optional.of(1L));
        seller.setPassword(encoder.encode("any_password"));
        this.userRepository.save(seller);

        LoginPayload payload = new LoginPayload("any_email@email.com", "any_password");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(payload);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/login", jsonPayload, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(!responseEntity.getBody().isEmpty());
    }

    @Test
    void shouldNotReturnTokenWhenSLoginFails() throws JsonProcessingException {
        LoginPayload payload = new LoginPayload("any_email@email.com", "any_password");
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonPayload = objectMapper.writeValueAsString(payload);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/login", jsonPayload, String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }



}
