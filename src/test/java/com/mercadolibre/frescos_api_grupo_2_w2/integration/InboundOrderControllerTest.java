package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.payloads.LoginPayload;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @BeforeEach
    void setup() throws Exception {
        this.userRepository.deleteAll();
        this.warehouseRepository.deleteAll();
        this.sectionRepository.deleteAll();
        this.batchRepository.deleteAll();
    }

    private String loginSeller() throws JsonProcessingException {
        // insert user
        Seller seller = UserSellerMock.validSeller(1L);
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

//    @Test
//    @DisplayName("should return 201 if createInboundOrder succeeds")
//    void createInboundOrder_succeeds() throws Exception {
//        token = this.loginSupervisor();
//
//        //insert necessary values
//        Supervisor supervisor = UserSupervisorMock.validSupervisor();
//        supervisor.setWarehouse(null);
//        this.userRepository.save(supervisor);
//
//        Seller seller = UserSellerMock.validSeller(null);
//        seller = this.userRepository.save(seller);
//
//        Warehouse warehouse = WarehouseMock.validWarehouse();
//        warehouse.getSupervisor().setUserId(3L);
//        Warehouse warehouseInserted = this.warehouseRepository.save(warehouse);
//
//        Section section = SectionMock.validSection();
//        section.setWarehouse(warehouseInserted);
//        section = this.sectionRepository.save(section);
//
//        Product product = ProductMock.validProduct();
//        product.setSeller(seller);
//        product = this.productRepository.save(product);
//
//        Batch batch = BatchMock.validBatch();
//        batch.setProduct(product);
//        this.batchRepository.save(batch);
//
//        InboundOrderForm form = new InboundOrderForm();
//
//        BatchForm batchForm = new BatchForm();
//        batchForm.setProductId(product.getProductId().toString());
//        batchForm.setCurrentQuantity(100);
//        batchForm.setCurrentTemperature(100);
//        batchForm.setInitialQuantity(100);
//        batchForm.setMinimumTemperature(100);
//        form.setBatchStock(Arrays.asList(batchForm));
//
//        form.setOrderNumber(1L);
//
//        InboundOrderSectionForm inboundOrderSectionForm = new InboundOrderSectionForm();
//        inboundOrderSectionForm.setSectionCode(section.getSectionId().toString());
//        inboundOrderSectionForm.setWarehouseCode(warehouse.getWarehouseId().toString());
//        form.setSection(inboundOrderSectionForm);
//        form.setOrderDate(LocalDate.now());
//
//        JSONObject formPayload = new JSONObject();
//        formPayload.put("order_date", "18/08/2000");
//        formPayload.put("order_number", form.getOrderNumber());
//        formPayload.put("section", form.getSection());
//        formPayload.put("batch_stock", form.getBatchStock());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", token);
//
//        HttpEntity<String> request = new HttpEntity<>(formPayload.toString().replaceAll("\\\\", ""), headers);
//
//        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/api/v1/fresh-products/inboundorder", request, String.class);
//
//        //Verify request succeed
//        assertEquals(201, result.getStatusCodeValue());
//    }
}
