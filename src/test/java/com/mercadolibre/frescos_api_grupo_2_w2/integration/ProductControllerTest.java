package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.ProductRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SellerRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.ProductMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSupervisorMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.payloads.LoginPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductControllerTest extends ControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    String token;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() throws Exception {
        this.userRepository.deleteAll();
        this.productRepository.deleteAll();
        this.sellerRepository.deleteAll();
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
    @DisplayName("should return 201 if createProduct succeeds")
    void createProduct_succeeds() throws Exception {
        token = this.loginSeller();
        // arrange
        Seller sellerMock = this.userRepository.save(UserSellerMock.validSeller(3L));

        //mock Product
        ProductForm productForm = new ProductForm();
        productForm.setName("any_name");
        productForm.setType(ProductTypeEnum.FRESH);
        productForm.setSellerId(sellerMock.getUserId());
        productForm.setValue(BigDecimal.valueOf(15.50));

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<ProductForm> request = new HttpEntity<>(productForm, header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/fresh-products", request, String.class);

        //Verify request succeed
        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 403 if user is not authenticate ou have no permissions")
    void createProduct_userNotAuthenticate() throws Exception {
        SellerForm seller = UserSellerMock.validSellerForm();
        seller.setEmail("new_user_seller@email.com");

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<ProductForm> request = new HttpEntity<>(new ProductForm(), header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/fresh-products", request, String.class);

        //Verify request succeed
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 403 if a invalid payload are provided")
    void createProduct_InvalidPayload() throws Exception {
        HttpEntity<ProductForm> request = new HttpEntity<>(new ProductForm());

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/fresh-products", request, String.class);
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return a product list if getProductsByCategory succeeds")
    void getProductsByCategory_succeeds() {
        HttpEntity<ProductForm> request = new HttpEntity<>(new ProductForm());
        Product product = ProductMock.validProduct(UUID.randomUUID());
        Seller seller = this.sellerRepository.save(UserSellerMock.validSeller());
        product.setSeller(seller);
        product = this.productRepository.save(product);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/list")
                .queryParam("productType", ProductTypeEnum.FRESH.getCode());

        ResponseEntity<ProductResponse[]> result = this.testRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, ProductResponse[].class);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(product.getProductId(), result.getBody()[0].getProductId());
    }

    @Test
    @DisplayName("should return a product list if getAllProducts succeeds")
    void getAllProducts_succeeds() {
        HttpEntity<ProductForm> request = new HttpEntity<>(new ProductForm());
        Product product = ProductMock.validProduct(UUID.randomUUID());
        Seller seller = this.sellerRepository.save(UserSellerMock.validSeller());
        product.setSeller(seller);
        product = this.productRepository.save(product);

        ResponseEntity<ProductResponse[]> result = this.testRestTemplate.exchange("/api/v1/fresh-products", HttpMethod.GET, request, ProductResponse[].class);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(product.getProductId(), result.getBody()[0].getProductId());
    }
}
