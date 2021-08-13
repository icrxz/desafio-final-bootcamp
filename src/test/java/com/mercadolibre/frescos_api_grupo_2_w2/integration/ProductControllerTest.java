package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.BatchCompleteResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.BatchRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.ProductRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SellerRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.BatchMock;
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
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
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

    @Autowired
    private BatchRepository batchRepository;

    @BeforeEach
    void setup() throws Exception {
        this.userRepository.deleteAll();
        this.productRepository.deleteAll();
        this.sellerRepository.deleteAll();
        this.batchRepository.deleteAll();
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


    private Product insertProduct(ProductTypeEnum type, Seller seller) {
        Product product = ProductMock.validProduct(null);
        product.setProductId(UUID.randomUUID());
        product.setType(type);
        product.setSeller(seller);
        return this.productRepository.save(product);
    }

    private Batch insertBatch(Product product, LocalDate dueDate) {
        Batch batch = BatchMock.validBatch(null);
        batch.setProduct(product);
        batch.setDueDate(dueDate);
        return this.batchRepository.save(batch);
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
    void getProductsByCategory_succeeds() throws JsonProcessingException {
        token = this.loginSupervisor();
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<ProductForm> request = new HttpEntity<>(new ProductForm());
        Product product = ProductMock.validProduct(UUID.randomUUID());
        Seller seller = this.sellerRepository.save(UserSellerMock.validSeller());
        product.setSeller(seller);
        product = this.productRepository.save(product);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/category/list")
                .queryParam("productType", ProductTypeEnum.FRESH.getCode());

        ResponseEntity<ProductResponse[]> result = this.testRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(header), ProductResponse[].class);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(product.getProductId(), result.getBody()[0].getProductId());
    }

    @Test
    @DisplayName("should return a product list if getAllProducts succeeds")
    void getAllProducts_succeeds() throws JsonProcessingException {
        token = this.loginSupervisor();

        Product product = ProductMock.validProduct(UUID.randomUUID());
        Seller seller = this.sellerRepository.save(UserSellerMock.validSeller());
        product.setSeller(seller);
        product = this.productRepository.save(product);

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<ProductResponse[]> result = this.testRestTemplate.exchange("/api/v1/fresh-products", HttpMethod.GET, new HttpEntity(header), ProductResponse[].class);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(product.getProductId(), result.getBody()[0].getProductId());
    }

    @Test
    @DisplayName("should return a batch list if getAllProducts succeeds")
    void findBatchByProduct_succeeds() throws JsonProcessingException {
        token = this.loginSupervisor();
        Seller seller = this.userRepository.save(UserSellerMock.validSeller(null));

        //Insert products and batchs
        //Case 1
        Product product = insertProduct(ProductTypeEnum.FRESH, seller);
        insertBatch(product, LocalDate.now());
        insertBatch(product, LocalDate.now().plusDays(2));
        insertBatch(product, LocalDate.now().plusDays(10));

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/batch/list")
                .queryParam("productId", product.getProductId());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<BatchCompleteResponse> result = this.testRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(header), BatchCompleteResponse.class);

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().getBatchStock().size() == 3);
    }

    @Test
    @DisplayName("should return 403 if user is not authenticate ou have no permissions")
    void findBatchByProduct_forbidden() throws JsonProcessingException {
        token = this.loginSeller();

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/batch/list")
                .queryParam("productId", UUID.randomUUID());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<String> result = this.testRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(header), String.class);
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return a ordered batch list if getAllProducts succeeds")
    void findBatchByProductOrder_succeeds() throws JsonProcessingException {
        token = this.loginSupervisor();
        Seller seller = this.userRepository.save(UserSellerMock.validSeller(null));

        //Insert products and batch
        //Case 1
        Product product = insertProduct(ProductTypeEnum.FRESH, seller);
        insertBatch(product, LocalDate.now());
        insertBatch(product, LocalDate.now().plusDays(2));
        insertBatch(product, LocalDate.now().plusDays(10));

        //OtherProduct
        Product product2 = insertProduct(ProductTypeEnum.FRESH, seller);
        insertBatch(product2, LocalDate.now());

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/batch/list/order")
                .queryParam("productId", product.getProductId())
                .queryParam("order", "F");

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<BatchCompleteResponse> result = this.testRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(header), BatchCompleteResponse.class);

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().getBatchStock().size() == 3);
        assertEquals(result.getBody().getBatchStock().get(0).getDueDate(), LocalDate.now());
        assertEquals(result.getBody().getBatchStock().get(1).getDueDate(), LocalDate.now().plusDays(2));
        assertEquals(result.getBody().getBatchStock().get(2).getDueDate(), LocalDate.now().plusDays(10));
    }

    @Test
    @DisplayName("should return 403 if user is not authenticate ou have no permissions")
    void findBatchByProductOrder_forbidden() throws JsonProcessingException {
        token = this.loginSeller();

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/batch/list/order")
                .queryParam("productId", UUID.randomUUID());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<String> result = this.testRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(header), String.class);
        assertEquals(403, result.getStatusCodeValue());
    }
}
