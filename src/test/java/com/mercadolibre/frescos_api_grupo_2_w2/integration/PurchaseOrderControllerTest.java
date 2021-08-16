package com.mercadolibre.frescos_api_grupo_2_w2.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.product.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderProductsForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.*;
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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurchaseOrderControllerTest extends ControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;
    private Batch batch;
    private Buyer buyer;
    private Product product;
    private Section section;
    private Seller seller;
    private Supervisor supervisor;
    private Warehouse warehouse;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    private void cleanRepositories() {
        this.batchRepository.deleteAll();
        this.userRepository.deleteAll();
        this.warehouseRepository.deleteAll();
        this.sectionRepository.deleteAll();
        this.productRepository.deleteAll();
        this.inboundOrderRepository.deleteAll();
    }

    @BeforeEach
    void setup() throws Exception {
        this.cleanRepositories();

        this.supervisor = userRepository.save(UserSupervisorMock.validSupervisor());
        this.buyer = userRepository.save(UserBuyerMock.validBuyer());
        this.seller = userRepository.save(UserSellerMock.validSeller());

        Warehouse newWarehouse = WarehouseMock.validWarehouse();
        newWarehouse.setSupervisor(this.supervisor);
        this.warehouse = warehouseRepository.save(newWarehouse);

        Section newSection = SectionMock.validSection();
        newSection.setWarehouse(this.warehouse);
        this.section = sectionRepository.save(newSection);

        Product newProduct = ProductMock.validProduct(null);
        newProduct.setSeller(this.seller);
        this.product = productRepository.save(newProduct);

        this.batch = batchRepository.save(BatchMock.validBatch(this.product));

        InboundOrder newInboundOrder = InboundOrderMock.validInboundOrder();
        newInboundOrder.setBatchStock(Arrays.asList(this.batch));
        newInboundOrder.setSection(this.section);
        inboundOrderRepository.save(newInboundOrder);
    }

    private String loginBuyer() throws JsonProcessingException {
        // payload
        LoginPayload payload = new LoginPayload(this.buyer.getEmail(), "123");
        String jsonPayload = objectMapper.writeValueAsString(payload);

        ResponseEntity<String> responseEntity = this.testRestTemplate.postForEntity("/login", jsonPayload, String.class);
        token = "Bearer "+ responseEntity.getBody();
        return token;
    }

    private PurchaseOrder purchaseOrderEntity() {
        PurchaseOrder purchaseOrder = PurchaseOrderMock.validPurchaseOrder(null);
        PurchaseOrderProduct newPurchaseOrderProduct = new PurchaseOrderProduct(purchaseOrder, this.product, 2);
        purchaseOrder.setBuyer(this.buyer);
        purchaseOrder.setProduct(Arrays.asList(newPurchaseOrderProduct));

        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Test
    @DisplayName("should return 201 if create PurchaseOrder succeeds")
    void createPurchaseOrder_succeeds() throws Exception {
        this.token = this.loginBuyer();

        PurchaseOrderForm newPurchaseOrder = PurchaseOrderMock.validPurchaseOrderForm();
        newPurchaseOrder.setBuyerId(this.buyer.getUserId());

        PurchaseOrderProductsForm newPurchaseOrderProductsForm = new PurchaseOrderProductsForm();
        newPurchaseOrderProductsForm.setQuantity(2);
        newPurchaseOrderProductsForm.setProductId(this.product.getProductId());
        newPurchaseOrder.setProducts(Arrays.asList(newPurchaseOrderProductsForm));

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<ProductForm> request = new HttpEntity(newPurchaseOrder, header);
        ResponseEntity<PurchaseOrderResponse> result = this.testRestTemplate.postForEntity(
                "/api/v1/fresh-products/orders",
                request,
                PurchaseOrderResponse.class
        );

        //assert
        assertEquals(201, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 200 if edit PurchaseOrder succeeds")
    void editPurchaseOrder_succeeds() throws Exception {
        this.token = this.loginBuyer();
        PurchaseOrder purchaseOrder = this.purchaseOrderEntity();

        PurchaseOrderForm editedPurchaseOrder = PurchaseOrderMock.validEditPurchaseOrderForm();
        PurchaseOrderProductsForm purchaseOrderProductsForm = new PurchaseOrderProductsForm();
        purchaseOrderProductsForm.setProductId(this.product.getProductId());
        purchaseOrderProductsForm.setQuantity(4);
        editedPurchaseOrder.setBuyerId(this.buyer.getUserId());
        editedPurchaseOrder.setProducts(Arrays.asList(purchaseOrderProductsForm));

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/orders")
                .queryParam("orderId", purchaseOrder.getPurchaseOrderId());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        HttpEntity<PurchaseOrderForm> request = new HttpEntity(editedPurchaseOrder, header);
        ResponseEntity<PurchaseOrderResponse> result = this.testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                request,
                PurchaseOrderResponse.class
        );

        //assert
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("should return 200 and object if get PurchaseOrder by Id succeeds")
    void getPurchaseOrder_succeeds() throws Exception {
        this.token = this.loginBuyer();
        PurchaseOrder purchaseOrder = this.purchaseOrderEntity();
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/v1/fresh-products/orders")
                .queryParam("orderId", purchaseOrder.getPurchaseOrderId());

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);

        ResponseEntity<PurchaseOrderResponse> result = this.testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity(header),
                PurchaseOrderResponse.class
        );

        //assert
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody().getPurchaseOrderId()).usingRecursiveComparison().isEqualTo(purchaseOrder.getPurchaseOrderId());
    }
}
