package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderProductsForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.*;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderStatusEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.PurchaseOrderRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.*;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceTest {
    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private BuyerService buyerService;

    @Mock
    private ProductService productService;

    @Mock
    private BatchService batchService;

    @Mock
    private InboundOrderService inboundOrderService;

    @BeforeEach
    public void setUp() throws Exception {
        purchaseOrderRepository.deleteAll();
        purchaseOrderService = new PurchaseOrderService(purchaseOrderRepository, buyerService, productService, batchService);
    }

    @Test
    @DisplayName("should return a purchaseOrder if ID succeeds")
    void findPurchaseOrderById_succeeds () {
        UUID purchaseOrderId = PurchaseOrderMock.purchaseOrderId;
        PurchaseOrder purchaseOrderMocked = PurchaseOrderMock.validPurchaseOrder(null);

        given(purchaseOrderRepository.findById(purchaseOrderId))
                .willReturn(java.util.Optional.of(purchaseOrderMocked));

        PurchaseOrderResponse purchaseOrderTest = purchaseOrderService.findPurchaseOrderById(purchaseOrderId);

        assertEquals(purchaseOrderId, purchaseOrderTest.getPurchaseOrderId());
    }

    @Test
    @DisplayName("should throws if id not belong to any PurchaseOrder")
    void findPurchaseOrderById_failsIfNotFound() {
        UUID purchaseOrderId = PurchaseOrderMock.purchaseOrderId;

        assertThatThrownBy(() -> purchaseOrderService.findPurchaseOrderById(purchaseOrderId))
                .isInstanceOf(ApiException.class)
                .hasMessage("Purchase order not found with this id");
    }

    private void prepareProductCreation(
            PurchaseOrderForm purchaseOrderForm,
            List<PurchaseOrderProductsForm> orderProducts
    ) {
        Buyer buyerMock = UserBuyerMock.validBuyer(purchaseOrderForm.getBuyerId());

        PurchaseOrder newPurchaseOrder = new PurchaseOrder();
        PurchaseOrderProduct convertedOrderProduct1 = PurchaseOrderProduct.builder()
                .purchaseOrder(newPurchaseOrder)
                .product(ProductMock.validProduct(orderProducts.get(0).getProductId()))
                .quantity(orderProducts.get(0).getQuantity())
                .build();
        PurchaseOrderProduct convertedOrderProduct2 = PurchaseOrderProduct.builder()
                .purchaseOrder(newPurchaseOrder)
                .product(ProductMock.validProduct(orderProducts.get(1).getProductId()))
                .quantity(orderProducts.get(1).getQuantity())
                .build();

        newPurchaseOrder.setBuyer(buyerMock);
        newPurchaseOrder.setDate(purchaseOrderForm.getDate());
        newPurchaseOrder.setStatus(purchaseOrderForm.getStatus());
        newPurchaseOrder.setProduct(Arrays.asList(convertedOrderProduct1, convertedOrderProduct2));

        given(this.buyerService.findBuyer(purchaseOrderForm.getBuyerId())).willReturn(buyerMock);
        given(this.productService.findProductById(convertedOrderProduct1.getProduct().getProductId()))
                .willReturn(convertedOrderProduct1.getProduct());
        given(this.productService.findProductById(convertedOrderProduct2.getProduct().getProductId()))
                .willReturn(convertedOrderProduct2.getProduct());
        given(this.productService.getProductQuantity(convertedOrderProduct1.getProduct().getProductId())).willReturn(10L);
        given(this.purchaseOrderRepository.save(any())).willReturn(newPurchaseOrder);
    }

    @Test
    @DisplayName("should return a Product creation succeeds")
    void createProduct_succeeds () {
        PurchaseOrderForm purchaseOrderForm = PurchaseOrderMock.validPurchaseOrderForm();
        PurchaseOrderProductsForm purchaseOrderProduct1 = PurchaseOrderProductMock.validPurchaseOrderProductsForm();
        PurchaseOrderProductsForm purchaseOrderProduct2 = PurchaseOrderProductMock.validPurchaseOrderProductsForm();
        List<PurchaseOrderProductsForm> purchaseOrders = Arrays.asList(purchaseOrderProduct1, purchaseOrderProduct2);

        purchaseOrderForm.setProducts(purchaseOrders);
        prepareProductCreation(purchaseOrderForm, purchaseOrders);

        PurchaseOrderResponse response = this.purchaseOrderService.createPurchaseOrder(purchaseOrderForm);

        // assert
        assertThat(response.getDate()).isEqualTo(purchaseOrderForm.getDate());
        assertThat(response.getPurchaseOrderItems().size()).isEqualTo(2);
        assertThat(response.getTotalPrice()).isEqualTo(BigDecimal.valueOf(62.00));
    }

    @Test
    @DisplayName("should throws if product quantity is larger than the stock")
    void createProduct_failsWithoutStock () {
        PurchaseOrderForm purchaseOrderForm = PurchaseOrderMock.validPurchaseOrderForm();
        PurchaseOrderProductsForm purchaseOrderProduct1 = PurchaseOrderProductMock.validPurchaseOrderProductsForm();

        purchaseOrderProduct1.setQuantity(10000);
        purchaseOrderForm.setProducts(Arrays.asList(purchaseOrderProduct1));

        PurchaseOrderProduct convertedOrderProduct1 = PurchaseOrderProduct.builder()
                .product(ProductMock.validProduct(purchaseOrderProduct1.getProductId()))
                .quantity(purchaseOrderProduct1.getQuantity())
                .build();

        given(this.productService.findProductById(convertedOrderProduct1.getProduct().getProductId())).willReturn(convertedOrderProduct1.getProduct());
        given(this.productService.getProductQuantity(convertedOrderProduct1.getProduct().getProductId())).willReturn(10L);

        // assert
        assertThatThrownBy(() -> purchaseOrderService.createPurchaseOrder(purchaseOrderForm))
                .isInstanceOf(ApiException.class)
                .hasMessage("The quantity in storage is less than the quantity ordered");
    }

    @Test
    @DisplayName("should return a Product update succeeds")
    void updateProduct_succeeds () {
        UUID purchaseOrderId = PurchaseOrderMock.purchaseOrderId;
        PurchaseOrderForm purchaseOrderForm = PurchaseOrderMock.validEditPurchaseOrderForm();
        PurchaseOrder purchaseOrderMock = PurchaseOrderMock.validPurchaseOrder(null);
        Buyer buyerMock = UserBuyerMock.validBuyer(purchaseOrderForm.getBuyerId());

        PurchaseOrderProduct convertedOrderProduct1 = PurchaseOrderProduct.builder()
                .product(ProductMock.validProduct(purchaseOrderForm.getProducts().get(0).getProductId()))
                .quantity(purchaseOrderForm.getProducts().get(0).getQuantity())
                .build();

        PurchaseOrder editedPurchaseOrder = PurchaseOrder.builder()
                .buyer(buyerMock)
                .date(purchaseOrderForm.getDate())
                .product(Arrays.asList(convertedOrderProduct1))
                .status(purchaseOrderForm.getStatus())
                .build();

        given(this.buyerService.findBuyer(purchaseOrderForm.getBuyerId())).willReturn(buyerMock);
        given(this.purchaseOrderRepository.findById(purchaseOrderId)).willReturn(Optional.of(purchaseOrderMock));
        given(this.productService.findProductById(purchaseOrderForm.getProducts().get(0).getProductId()))
                .willReturn(convertedOrderProduct1.getProduct());
        given(this.productService.getProductQuantity(purchaseOrderForm.getProducts().get(0).getProductId()))
                .willReturn(10L);
        given(this.purchaseOrderRepository.save(any())).willReturn(editedPurchaseOrder);


        PurchaseOrderResponse response = this.purchaseOrderService.updatePurchaseOrder(purchaseOrderId, purchaseOrderForm);

        // assert
        assertThat(response.getDate()).isEqualTo(purchaseOrderForm.getDate());
        assertThat(response.getStatus()).isEqualTo(OrderStatusEnum.TRANSPORT.getDescription());
        assertThat(response.getPurchaseOrderItems().size()).isEqualTo(1);
        assertThat(response.getTotalPrice()).isEqualTo(BigDecimal.valueOf(31.00));
    }

    @Test
    @DisplayName("should throws if purchase order id was not found")
    void returnProduct_failsIfOrderIdWasNotFound () {
        Buyer buyerMock = UserBuyerMock.validBuyer();
        PurchaseOrder purchaseOrderMock = PurchaseOrderMock.validPurchaseOrder(null);

        given(purchaseOrderRepository.findById(purchaseOrderMock.getPurchaseOrderId())).willReturn(Optional.empty());
        given(buyerService.findBuyer(buyerMock.getEmail())).willReturn(buyerMock);

        assertThatThrownBy(() -> purchaseOrderService.returnPurchaseOrder(purchaseOrderMock.getPurchaseOrderId(), buyerMock.getEmail()))
                .isInstanceOf(ApiException.class)
                .hasMessage("Purchase order was not found");
    }

    @Test
    @DisplayName("should throws if purchase order status was not delivered")
    void returnProduct_failsIfStatusIsNotDelivered () {
        Buyer buyerMock = UserBuyerMock.validBuyer();
        PurchaseOrder purchaseOrderMock = PurchaseOrderMock.validPurchaseOrder(null);
        purchaseOrderMock.setStatus(OrderStatusEnum.CONFIRMING);

        given(purchaseOrderRepository.findById(purchaseOrderMock.getPurchaseOrderId())).willReturn(Optional.of(purchaseOrderMock));
        given(buyerService.findBuyer(buyerMock.getEmail())).willReturn(buyerMock);

        assertThatThrownBy(() -> purchaseOrderService.returnPurchaseOrder(purchaseOrderMock.getPurchaseOrderId(), buyerMock.getEmail()))
                .isInstanceOf(ApiException.class)
                .hasMessage("Only delivered purchases can be returned");
    }

    @Test
    @DisplayName("should throws if purchase order was made more than 1 day")
    void returnProduct_failsIfOrderIsPastOneDay () {
        Buyer buyerMock = UserBuyerMock.validBuyer();
        PurchaseOrder purchaseOrderMock = PurchaseOrderMock.validPurchaseOrder(null);
        purchaseOrderMock.setStatus(OrderStatusEnum.DELIVERED);
        purchaseOrderMock.setDate(LocalDate.now().minusDays(2));

        given(purchaseOrderRepository.findById(purchaseOrderMock.getPurchaseOrderId())).willReturn(Optional.of(purchaseOrderMock));
        given(buyerService.findBuyer(buyerMock.getEmail())).willReturn(buyerMock);

        assertThatThrownBy(() -> purchaseOrderService.returnPurchaseOrder(purchaseOrderMock.getPurchaseOrderId(), buyerMock.getEmail()))
                .isInstanceOf(ApiException.class)
                .hasMessage("Purchases over 1 day cannot be returned");
    }

    @Test
    @DisplayName("should throws if user who made requisition is different of purchase order buyer")
    void returnProduct_failsIfUserIsDifferentThanOrderBuyer () {
        Buyer buyerMock = UserBuyerMock.validBuyer();
        Buyer buyerMock2 = UserBuyerMock.validBuyer();
        buyerMock2.setUserId(buyerMock.getUserId() + 1L);
        PurchaseOrder purchaseOrderMock = PurchaseOrderMock.validPurchaseOrder(null);
        purchaseOrderMock.setStatus(OrderStatusEnum.DELIVERED);
        purchaseOrderMock.setBuyer(buyerMock2);

        given(purchaseOrderRepository.findById(purchaseOrderMock.getPurchaseOrderId())).willReturn(Optional.of(purchaseOrderMock));
        given(buyerService.findBuyer(buyerMock.getEmail())).willReturn(buyerMock);

        assertThatThrownBy(() -> purchaseOrderService.returnPurchaseOrder(purchaseOrderMock.getPurchaseOrderId(), buyerMock.getEmail()))
                .isInstanceOf(ApiException.class)
                .hasMessage("Purchases can only be returned by the original buyer account");
    }

    @Test
    @DisplayName("should throws if user who made requisition is different of purchase order buyer")
    void getReturnedProduct_failsIfEmpty () {
        given(purchaseOrderRepository.findPurchaseOrderByStatus(OrderStatusEnum.RETURNED)).willReturn(Arrays.asList());

        assertThatThrownBy(() -> purchaseOrderService.getReturnedPurchaseOrders())
                .isInstanceOf(ApiException.class)
                .hasMessage("Returned purchase orders not found");
    }
}
