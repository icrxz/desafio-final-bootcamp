package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SectionControllerTest extends ControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    String token;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setup() throws Exception {
        this.userRepository.deleteAll();
        this.warehouseRepository.deleteAll();
    }

     private String loginSeller() throws JsonProcessingException {
        // insert user
        Seller seller = UserSellerMock.validSeller(Optional.of(1L));
        seller.setPassword(encoder.encode("any_password"));
        this.userRepository.save(seller);

        // payload
        LoginPayload payload = new LoginPayload("any_email@email.com", "any_password");
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
    @DisplayName("should return 403 if user is not authenticate ou have no permissions")
    void createSection_userNotAuthenticate() throws Exception {
        token = this.loginSeller();
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<SectionForm> request = new HttpEntity<>(new SectionForm(), header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/sections", request, String.class);

        //Verify request succeed
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 201 if createSection succeeds")
    void createSection_suceeds() throws Exception {
        token = this.loginSupervisor();
        // arrange
        Supervisor user = this.userRepository.save(UserSupervisorMock.validSupervisor());
        Warehouse warehouse = WarehouseMock.validWarehouse();
        warehouse.setSupervisor(user);
        warehouse = this.warehouseRepository.save(warehouse);

        //mock Section
        SectionForm sectionForm = new SectionForm();
        sectionForm.setMaxCapacity(100);
        sectionForm.setWarehouseId(warehouse.getWarehouseId().toString());
        sectionForm.setProductType(ProductTypeEnum.FRESH);

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<SectionForm> request = new HttpEntity<>(sectionForm, header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/sections", request, String.class);

        //Verify request succeed
        assertEquals(201, result.getStatusCodeValue());
    }
}
