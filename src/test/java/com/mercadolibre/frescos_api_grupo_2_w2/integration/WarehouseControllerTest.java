package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.WarehouseForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.warehouse.WarehouseWithQuantityResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.warehouse.WarehousesWithProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.payloads.LoginPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @BeforeEach
    void setup() throws Exception {
        this.batchRepository.deleteAll();
        this.userRepository.deleteAll();
        this.warehouseRepository.deleteAll();
        this.sectionRepository.deleteAll();
        this.productRepository.deleteAll();
        this.inboundOrderRepository.deleteAll();
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

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/fresh-products/warehouses", request, String.class);

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

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/fresh-products/warehouses", request, String.class);

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

        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/fresh-products/warehouses", request, String.class);

        //Verify request succeed
        assertEquals(403, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return All Warehouse if getAllWarehouses succeeds")
    void getAllWarehouses_succeeds() throws Exception {
        token = this.loginSupervisor();

        // arrange
        Supervisor supervisor = this.supervisorRepository.save(new Supervisor());
        Warehouse warehouse = WarehouseMock.validWarehouse();
        warehouse.getSupervisor().setUserId(supervisor.getUserId());
        this.warehouseRepository.save(warehouse);

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity result = this.testRestTemplate.exchange("/api/v1/fresh-products/warehouses/list", HttpMethod.GET, new HttpEntity<>(header), String.class);

        //Verify request succeed
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("should return 403 if userTokenProvided is not allowed")
    void getAllWarehouses_AuthenticateError() throws Exception {
        token = this.loginSeller();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity result = this.testRestTemplate.exchange("/api/v1/fresh-products/warehouses/list", HttpMethod.GET, new HttpEntity<>(header), String.class);

        //Verify request succeed
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    @DisplayName("should return 200 if get warehouses by a product succeeds")
    void getWarehousesByProduct_succeeds() throws Exception {
        token = this.loginSupervisor();

        Supervisor supervisor = userRepository.save(UserSupervisorMock.validSupervisor());
        Seller seller = userRepository.save(UserSellerMock.validSeller());

        Warehouse newWarehouse = WarehouseMock.validWarehouse();
        newWarehouse.setSupervisor(supervisor);
        newWarehouse = warehouseRepository.save(newWarehouse);

        Section newSection1 = SectionMock.validSection();
        newSection1.setWarehouse(newWarehouse);
        newSection1 = sectionRepository.save(newSection1);

        Section newSection2 = SectionMock.validSection();
        newSection2.setWarehouse(newWarehouse);
        newSection2 = sectionRepository.save(newSection2);

        Product newProduct = ProductMock.validProduct(null);
        newProduct.setSeller(seller);
        newProduct = productRepository.save(newProduct);

        InboundOrder newInboundOrder1 = InboundOrderMock.validInboundOrder();
        newInboundOrder1.setSection(newSection1);
        newInboundOrder1.setBatchStock(new ArrayList<>());
        newInboundOrder1 = inboundOrderRepository.save(newInboundOrder1);

        InboundOrder newInboundOrder2 = InboundOrderMock.validInboundOrder();
        newInboundOrder2.setSection(newSection1);
        newInboundOrder2.setBatchStock(new ArrayList<>());
        newInboundOrder2 = inboundOrderRepository.save(newInboundOrder2);

        InboundOrder newInboundOrder3 = InboundOrderMock.validInboundOrder();
        newInboundOrder3.setSection(newSection2);
        newInboundOrder3.setBatchStock(new ArrayList<>());
        newInboundOrder3 = inboundOrderRepository.save(newInboundOrder3);

        Batch batch1 = BatchMock.validBatch(newProduct);
        batch1.setInboundOrder(newInboundOrder1);
        batchRepository.save(batch1);

        Batch batch2 = BatchMock.validBatch(newProduct);
        batch2.setInboundOrder(newInboundOrder2);
        batchRepository.save(batch2);

        Batch batch3 = BatchMock.validBatch(newProduct);
        batch3.setInboundOrder(newInboundOrder3);
        batchRepository.save(batch3);

        long expectedQuantity = batch1.getCurrentQuantity() + batch2.getCurrentQuantity() + batch3.getCurrentQuantity();
        WarehousesWithProductResponse expectedResponse = WarehousesWithProductResponse.builder()
                .productId(newProduct.getProductId())
                .warehouses(Arrays.asList(new WarehouseWithQuantityResponse(newWarehouse.getWarehouseId(), expectedQuantity)))
                .build();

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/warehouses")
                .queryParam("productId", newProduct.getProductId());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<WarehousesWithProductResponse> result = this.testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(header),
                WarehousesWithProductResponse.class
        );

        //Verify request succeed
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertThat(result.getBody()).usingRecursiveComparison().isEqualTo(expectedResponse);
    }
}
