package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSupervisorMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.payloads.LoginPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest extends ControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    String token;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() throws Exception {
        this.userRepository.deleteAll();
        // Insert user
        Seller seller = UserSellerMock.validSeller(Optional.of(1L));
        seller.setPassword(encoder.encode("any_password"));
        this.userRepository.save(seller);

        // payload
        LoginPayload payload = new LoginPayload("any_email@email.com", "any_password");
        String jsonPayload = objectMapper.writeValueAsString(payload);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/login", jsonPayload, String.class);
        token = "Bearer "+ responseEntity.getBody();
    }

    @Test
    @DisplayName("should create a userSeller")
    void createUser_userSellerSucceeds() throws Exception {
        SellerForm seller = UserSellerMock.validSellerForm();
        seller.setEmail("new_user_seller@email.com");

        HttpEntity<SellerForm> request = new HttpEntity<>(seller);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/user/seller", request, String.class);

        //Verify request succeed
        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 400 if a invalid payload are provided")
    void createUser_userSellerInvalidPayload() throws Exception {
        HttpEntity<SellerForm> request = new HttpEntity<>(new SellerForm());

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/user/seller", request, String.class);
        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 403 if a unauthorized token are provied")
    void createUser_userSupervisorNotAuthorized() throws Exception {
        SellerForm seller = UserSellerMock.validSellerForm();
        seller.setEmail("new_user_supervisor@email.com");

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<SellerForm> request = new HttpEntity<>(seller, header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/user/supervisor", request, String.class);

        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should create a userSupervisor")
    void createUser_userSupervisor() throws Exception {
        this.userRepository.deleteAll();

        // Insert user supervisor
        Supervisor supervisor = UserSupervisorMock.validSupervisor(Optional.of(1L));
        supervisor.setEmail("other_email@email.com");
        supervisor.setRole("SUPERVISOR");
        supervisor.setPassword(encoder.encode("any_password"));
        this.userRepository.save(supervisor);

        //Login with Supervisor
        LoginPayload payload = new LoginPayload("other_email@email.com", "any_password");
        String jsonPayload = objectMapper.writeValueAsString(payload);
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/login", jsonPayload, String.class);
        token = "Bearer "+ responseEntity.getBody();

        SellerForm seller = UserSellerMock.validSellerForm();
        seller.setEmail("new_user_supervisor@email.com");

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<SellerForm> request = new HttpEntity<>(seller, header);
        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/user/supervisor", request, String.class);
        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return all users")
    void getAllUsers() throws JsonProcessingException {
        this.userRepository.deleteAll();

        // Insert user supervisor
        Supervisor supervisor = UserSupervisorMock.validSupervisor(Optional.of(1L));
        supervisor.setEmail("other_email@email.com");
        supervisor.setRole("SUPERVISOR");
        supervisor.setPassword(encoder.encode("any_password"));
        this.userRepository.save(supervisor);

        //Login with Supervisor
        LoginPayload payload = new LoginPayload("other_email@email.com", "any_password");
        String jsonPayload = objectMapper.writeValueAsString(payload);
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/login", jsonPayload, String.class);
        token = "Bearer "+ responseEntity.getBody();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<String> result = this.testRestTemplate.exchange("/api/v1/user/", HttpMethod.GET, new HttpEntity<>(header), String.class);
        assertEquals(200, result.getStatusCodeValue());
    }

}
