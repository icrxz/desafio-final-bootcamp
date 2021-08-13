package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.WarehouseForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SupervisorRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.WarehouseRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSupervisorMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.WarehouseMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.payloads.LoginPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WarehouseControllerTest extends ControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    String token;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setup() throws Exception {
        this.supervisorRepository.deleteAll();
        this.userRepository.deleteAll();
        this.warehouseRepository.deleteAll();
    }

    private String loginSeller() throws JsonProcessingException {
        // insert user
        Seller seller = UserSellerMock.validSeller(1L);
        seller.setPassword(encoder.encode("any_password"));
        this.userRepository.save(seller);

        // payload
        LoginPayload payload = new LoginPayload(seller.getEmail(), "any_password");
        String jsonPayload = objectMapper.writeValueAsString(payload);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/login", jsonPayload, String.class);
        token = "Bearer "+ responseEntity.getBody();
        return token;
    }

    private String loginSupervisor() throws JsonProcessingException {
        // Insert user supervisor
        Supervisor supervisor = UserSupervisorMock.validSupervisor();
        supervisor.setEmail("other_email@email.com");
        supervisor.setRole("SUPERVISOR");
        supervisor.setPassword(encoder.encode("any_password"));
        this.userRepository.save(supervisor);

        //Login with Supervisor
        LoginPayload payload = new LoginPayload("other_email@email.com", "any_password");
        String jsonPayload = objectMapper.writeValueAsString(payload);
        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/login", jsonPayload, String.class);
        token = "Bearer "+ responseEntity.getBody();
        return token;
    }

    @Test
    @DisplayName("should return 201 if createWarehouse succeeds")
    void createWarehouse_succeeds() throws Exception {
        token = this.loginSupervisor();

        // arrange
        Supervisor supervisor = this.supervisorRepository.save(new Supervisor());
        WarehouseForm form = new WarehouseForm();
        form.setSupervisorId(supervisor.getUserId());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<WarehouseForm> request = new HttpEntity<>(form, header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/warehouses", request, String.class);

        //Verify request succeed
        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 404 if supervisor is not found")
    void createWarehouse_SupervisorNotFound() throws Exception {
        token = this.loginSupervisor();

        // arrange
        WarehouseForm form = new WarehouseForm();
        form.setSupervisorId(100);

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<WarehouseForm> request = new HttpEntity<>(form, header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/warehouses", request, String.class);

        //Verify request succeed
        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 403 if userTokenProvided is not allowed")
    void createWarehouse_AuthenticateError() throws Exception {
        token = this.loginSeller();
        // arrange
        WarehouseForm form = new WarehouseForm();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<WarehouseForm> request = new HttpEntity<>(form, header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/warehouses", request, String.class);

        //Verify request succeed
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return All Warehouse if getAllWarehouses succeeds")
    void getAllWarehouses_succeeds() throws Exception {
        token = this.loginSupervisor();

        // arrange
        // arrange
        Supervisor supervisor = this.supervisorRepository.save(new Supervisor());
        Warehouse warehouse = WarehouseMock.validWarehouse();
        warehouse.getSupervisor().setUserId(supervisor.getUserId());
        this.warehouseRepository.save(warehouse);

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity result = this.testRestTemplate.exchange("/api/v1/warehouses", HttpMethod.GET, new HttpEntity<>(header), String.class);

        //Verify request succeed
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("should return 403 if userTokenProvided is not allowed")
    void getAllWarehouses_AuthenticateError() throws Exception {
        token = this.loginSeller();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity result = this.testRestTemplate.exchange("/api/v1/warehouses", HttpMethod.GET, new HttpEntity<>(header), String.class);

        //Verify request succeed
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

}