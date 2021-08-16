package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.BatchForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrder.InboundOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.InboundOrder.InboundOrderSectionForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.product.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderProductsForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.InboundOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.payloads.LoginPayload;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class InboundOrderControllerTest extends ControllerTest{

    ObjectMapper objectMapper = new ObjectMapper();
    String token;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @BeforeEach
    void setup() throws Exception {
        this.userRepository.deleteAll();
        this.warehouseRepository.deleteAll();
        this.sectionRepository.deleteAll();
        this.batchRepository.deleteAll();
        this.inboundOrderRepository.deleteAll();
        this.productRepository.deleteAll();
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
    @DisplayName("should return 201 if createInboundOrder succeeds")
    void createInboundOrder_succeeds() throws Exception {
        token = this.loginSupervisor();

        //insert necessary values
        Supervisor supervisor = UserSupervisorMock.validSupervisor();
        supervisor.setWarehouse(null);
        supervisor = this.userRepository.save(supervisor);

        Seller seller = UserSellerMock.validSeller(null);
        seller = this.userRepository.save(seller);

        Warehouse warehouse = WarehouseMock.validWarehouse();
        warehouse.getSupervisor().setUserId(supervisor.getUserId());
        Warehouse warehouseInserted = this.warehouseRepository.save(warehouse);

        Section section = SectionMock.validSection();
        section.setWarehouse(warehouseInserted);
        section = this.sectionRepository.save(section);

        Product product = ProductMock.validProduct(null);
        product.setSeller(seller);
        product = this.productRepository.save(product);

        InboundOrderForm form = new InboundOrderForm();

        BatchForm batchForm = new BatchForm();
        batchForm.setProductId(product.getProductId().toString());
        batchForm.setCurrentQuantity(100);
        batchForm.setCurrentTemperature(100);
        batchForm.setInitialQuantity(100);
        batchForm.setMinimumTemperature(100);
        batchForm.setManufacturingDate(LocalDate.now());
        batchForm.setManufacturingTime(LocalDateTime.now());
        batchForm.setDueDate(LocalDate.now());
        form.setBatchStock(Arrays.asList(batchForm));

        form.setOrderNumber(1L);

        InboundOrderSectionForm inboundOrderSectionForm = new InboundOrderSectionForm();
        inboundOrderSectionForm.setSectionCode(section.getSectionId().toString());
        inboundOrderSectionForm.setWarehouseCode(warehouse.getWarehouseId().toString());
        form.setSection(inboundOrderSectionForm);
        form.setOrderDate(LocalDate.now());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);

        HttpEntity<InboundOrderForm> request = new HttpEntity<>(form, headers);
        ResponseEntity<InboundOrderResponse> result = this.testRestTemplate.postForEntity(
                "/api/v1/fresh-products/inboundorder",
                request,
                InboundOrderResponse.class
        );

        //Verify request succeed
        assertEquals(201, result.getStatusCodeValue());
        assertEquals(0, result.getBody().getErrorMessages().size());
        assertEquals(0, result.getBody().getFailedBatches().size());
        assertEquals(1, result.getBody().getSuccessBatches().size());
    }

    @Test
    @DisplayName("should return 200 if updateInboundOrder succeeds")
    void editInboundOrder_succeeds() throws Exception {
        this.token = this.loginSupervisor();

        Supervisor supervisorMock = UserSupervisorMock.validSupervisor();
        supervisorMock = userRepository.save(supervisorMock);

        Seller sellerMock = UserSellerMock.validSeller();
        sellerMock = userRepository.save(sellerMock);

        Warehouse warehouseMock = WarehouseMock.validWarehouse();
        warehouseMock.setSupervisor(supervisorMock);
        warehouseMock = warehouseRepository.save(warehouseMock);

        Section sectionMock = SectionMock.validSection();
        sectionMock.setWarehouse(warehouseMock);
        sectionMock = sectionRepository.save(sectionMock);

        Product productMock = ProductMock.validProduct(null);
        productMock.setSeller(sellerMock);
        productMock = productRepository.save(productMock);

        InboundOrder inboundOrderMock = InboundOrderMock.validInboundOrder();
        inboundOrderMock.setSection(sectionMock);
        inboundOrderMock.setBatchStock(new ArrayList<>());
        inboundOrderMock = inboundOrderRepository.save(inboundOrderMock);

        Batch batchMock = BatchMock.validBatch(productMock);
        batchMock.setInboundOrder(inboundOrderMock);
        batchMock = batchRepository.save(batchMock);

        BatchForm batchForm = new BatchForm();
        batchForm.setBatchId(batchMock.getBatchId());
        batchForm.setProductId(productMock.getProductId().toString());
        batchForm.setCurrentQuantity(5);
        batchForm.setCurrentTemperature(100);
        batchForm.setInitialQuantity(5);
        batchForm.setMinimumTemperature(100);
        batchForm.setManufacturingDate(LocalDate.now());
        batchForm.setManufacturingTime(LocalDateTime.now());
        batchForm.setDueDate(LocalDate.now());

        InboundOrderSectionForm inboundOrderSectionForm = new InboundOrderSectionForm();
        inboundOrderSectionForm.setSectionCode(sectionMock.getSectionId().toString());
        inboundOrderSectionForm.setWarehouseCode(warehouseMock.getWarehouseId().toString());

        InboundOrderForm editInboundOrderForm = new InboundOrderForm();
        editInboundOrderForm.setOrderNumber(inboundOrderMock.getNumber());
        editInboundOrderForm.setOrderDate(LocalDate.now().plusDays(10));
        editInboundOrderForm.setSection(inboundOrderSectionForm);
        editInboundOrderForm.setBatchStock(Arrays.asList(batchForm));

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/inboundorder")
                .queryParam("orderId", inboundOrderMock.getNumber());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<InboundOrderForm> request = new HttpEntity<>(editInboundOrderForm, header);
        ResponseEntity<InboundOrderResponse> result = this.testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                InboundOrderResponse.class
        );

        //assert
        Assertions.assertEquals(200, result.getStatusCodeValue());
        assertEquals(0, result.getBody().getErrorMessages().size());
        assertEquals(0, result.getBody().getFailedBatches().size());
        assertEquals(1, result.getBody().getSuccessBatches().size());
    }
}
