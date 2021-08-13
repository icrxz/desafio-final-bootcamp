package com.mercadolibre.frescos_api_grupo_2_w2.integration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.product.ProductByDueDateAndTypeForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.product.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.BatchResponse;
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
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private Product insertProduct(ProductTypeEnum type, Seller seller) {
        Product product = ProductMock.validProduct();
        product.setProductId(UUID.randomUUID());
        product.setType(type);
        product.setSeller(seller);
       return this.productRepository.save(product);
    }

    private Batch insertBatch(Product product, LocalDate dueDate) {
        Batch batch = BatchMock.validBatch();
        batch.setProduct(product);
        batch.setDueDate(dueDate);
        return this.batchRepository.save(batch);
    }

    @Test
    @DisplayName("should return 201 if createProduct succeeds")
    void createProduct_suceeds() throws Exception {
        token = this.loginSeller();
        // arrange
       Seller sellerMock = this.userRepository.save(UserSellerMock.validSeller(Optional.of(1L)));

       //mock Product
       ProductForm productForm = new ProductForm();
       productForm.setName("any_name");
       productForm.setType(ProductTypeEnum.FRESH);
       productForm.setSellerId(sellerMock.getUserId());
       productForm.setValue(new BigDecimal(100));

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
    void createProduct_InvalidPayload() {
        HttpEntity<ProductForm> request = new HttpEntity<>(new ProductForm());

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/fresh-products", request, String.class);
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return a list of Batches with DueDate less than the provided number of days + current date")
    void getDueDateByDays_succeeds() throws JsonProcessingException {
        token = this.loginSupervisor();
        Seller seller = this.userRepository.save(UserSellerMock.validSeller(null));

        //Insert products and batchs
        //Case 1
        Product product = insertProduct(ProductTypeEnum.FRESH, seller);
        insertBatch(product, LocalDate.now());

        //Case 2
        Product product2 = insertProduct(ProductTypeEnum.REFRIGERATED, seller);
        insertBatch(product2, LocalDate.now().plusDays(2));

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<BatchResponse[]> result = this.testRestTemplate.exchange("/api/v1/fresh-products/due-date?days=10", HttpMethod.GET, new HttpEntity<>(header), BatchResponse[].class);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(result.getBody()[0].getProductId(), product.getProductId());
        assertEquals(result.getBody()[1].getProductId(), product2.getProductId());
    }

    @Test
    @DisplayName("should return a empty list of Batches if not found matched products")
    void getDueDateByDays_notFoundProduct() throws JsonProcessingException {
        token = this.loginSupervisor();
        Seller seller = this.userRepository.save(UserSellerMock.validSeller(null));

        //Insert products and batchs
        //Case 1
        Product product = insertProduct(ProductTypeEnum.FRESH, seller);
        insertBatch(product, LocalDate.now().plusDays(11));

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<BatchResponse[]> result = this.testRestTemplate.exchange("/api/v1/fresh-products/due-date?days=10", HttpMethod.GET, new HttpEntity<>( header), BatchResponse[].class);
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().length == 0);
    }

    @Test
    @DisplayName("should return 403 if a unauthorized token are provided")
    void getDueDateByDays_forbidden() throws JsonProcessingException {
        token = this.loginSeller();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<String> result = this.testRestTemplate.exchange("/api/v1/fresh-products/due-date?days=10", HttpMethod.GET, new HttpEntity<>(header), String.class);
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return a list of Batches with DueDate less than the number of days + current date and Product Type according provided")
    void getDueDateByDaysAndProductType_succeeds() throws JsonProcessingException {
        token = this.loginSupervisor();
        Seller seller = this.userRepository.save(UserSellerMock.validSeller(null));

        //Insert products and batchs
        //Case 1
        Product product = insertProduct(ProductTypeEnum.FRESH, seller);
        insertBatch(product, LocalDate.now());

        //Case 2
        Product product2 = insertProduct(ProductTypeEnum.REFRIGERATED, seller);
        insertBatch(product2, LocalDate.now());

        ProductByDueDateAndTypeForm form = new ProductByDueDateAndTypeForm();
        form.setType(ProductTypeEnum.FRESH);
        form.setDays(10L);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/due-date/list")
                .queryParam("days", 10)
                .queryParam("type", ProductTypeEnum.FRESH.toString());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<BatchResponse[]> result = this.testRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(header), BatchResponse[].class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(result.getBody()[0].getProductId(), product.getProductId());
        assertTrue(result.getBody().length == 1);
    }

    @Test
    @DisplayName("should return 403 if a unauthorized token are provided")
    void getDueDateByDaysAndProductType_forbidden() throws JsonProcessingException {
        token = this.loginSeller();
        Seller seller = this.userRepository.save(UserSellerMock.validSeller(null));

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/due-date/list")
                .queryParam("days", 10)
                .queryParam("type", ProductTypeEnum.FRESH.toString());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<String> result = this.testRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(header), String.class);
        assertEquals(403, result.getStatusCodeValue());
    }
}
