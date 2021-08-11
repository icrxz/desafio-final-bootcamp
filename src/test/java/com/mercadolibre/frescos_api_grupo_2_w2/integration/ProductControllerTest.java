package com.mercadolibre.frescos_api_grupo_2_w2.integration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.ProductRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SellerRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.WarehouseRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
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

    @Test
    @DisplayName("should return 201 if createProduct succeeds")
    void createSection_suceeds() throws Exception {
        token = this.loginSeller();
        // arrange
       Seller sellerMock = this.userRepository.save(UserSellerMock.validSeller(Optional.of(1L)));
       //sellerMock = this.sellerRepository.save(sellerMock);

       //mock Section
       ProductForm productForm = new ProductForm();
       productForm.setName("any_name");
       productForm.setType(ProductTypeEnum.CARNES);
       productForm.setSellerId(sellerMock.getUserId());


        //warehouse.setSupervisor(user);
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<ProductForm> request = new HttpEntity<>(productForm, header);

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/products", request, String.class);

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

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/products", request, String.class);

        //Verify request succeed
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 403 if a invalid payload are provided")
    void createProduct_InvalidPayload() throws Exception {
        HttpEntity<ProductForm> request = new HttpEntity<>(new ProductForm());

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/products", request, String.class);
        assertEquals(403, result.getStatusCodeValue());
    }

}
