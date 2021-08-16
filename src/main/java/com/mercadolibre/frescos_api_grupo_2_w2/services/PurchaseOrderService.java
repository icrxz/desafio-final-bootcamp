package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderProductsForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.PurchaseOrderMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Buyer;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrderProduct;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderStatusEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final BuyerService buyerService;
    private final ProductService productService;
    private final BatchService batchService;

    @Autowired
    public PurchaseOrderService(
            PurchaseOrderRepository purchaseOrderRepository,
            BuyerService buyerService,
            ProductService productService,
            BatchService batchService
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.buyerService = buyerService;
        this.productService = productService;
        this.batchService = batchService;
    }

    public PurchaseOrderResponse findPurchaseOrderById(UUID purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);

        if (purchaseOrder == null) {
            throw new ApiException("404", "Purchase order not found with this id", 404);
        }

        BigDecimal orderTotalValue = getPurchaseOrderTotalValue(purchaseOrder);

        return PurchaseOrderMapper.entityToResponse(purchaseOrder, orderTotalValue);
    }

    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderForm purchaseOrderForm) {
        Buyer buyer = buyerService.findBuyer(purchaseOrderForm.getBuyerId());
        List<PurchaseOrderProduct> purchaseOrderProducts = this.createOrderProducts(purchaseOrderForm.getProducts());

        PurchaseOrder newPurchaseOrder = PurchaseOrder.builder()
                .buyer(buyer)
                .date(purchaseOrderForm.getDate())
                .product(purchaseOrderProducts)
                .status(purchaseOrderForm.getStatus())
                .build();

        purchaseOrderProducts.forEach(purchaseOrderProduct -> purchaseOrderProduct.setPurchaseOrder(newPurchaseOrder));
        PurchaseOrder createdPurchase = purchaseOrderRepository.save(newPurchaseOrder);

        purchaseOrderForm.getProducts().forEach(product ->
                this.batchService.removeProductsFromBatches(product.getProductId(), product.getQuantity())
        );

        BigDecimal orderTotalValue = getPurchaseOrderTotalValue(createdPurchase);

        return PurchaseOrderMapper.entityToResponse(createdPurchase, orderTotalValue);
    }

    public PurchaseOrderResponse updatePurchaseOrder(UUID purchaserOrderId, PurchaseOrderForm purchaseOrderForm) {
        Buyer buyer = buyerService.findBuyer(purchaseOrderForm.getBuyerId());
        PurchaseOrder foundPurchaseOrder = purchaseOrderRepository.findById(purchaserOrderId).orElse(null);
        List<PurchaseOrderProduct> purchaseOrderProducts = this.createOrderProducts(purchaseOrderForm.getProducts());

        foundPurchaseOrder.setBuyer(buyer);
        foundPurchaseOrder.setDate(purchaseOrderForm.getDate());
        foundPurchaseOrder.setStatus(purchaseOrderForm.getStatus());
        foundPurchaseOrder.setProduct(purchaseOrderProducts);

        purchaseOrderProducts.forEach(purchaseOrderProduct -> purchaseOrderProduct.setPurchaseOrder(foundPurchaseOrder));
        PurchaseOrder createdPurchase = purchaseOrderRepository.save(foundPurchaseOrder);
        BigDecimal orderTotalValue = getPurchaseOrderTotalValue(createdPurchase);

        return PurchaseOrderMapper.entityToResponse(createdPurchase, orderTotalValue);
    }

    public PurchaseOrderResponse returnPurchaseOrder(UUID purchaseOrderId, String userEmail) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);
        Buyer foundBuyer = buyerService.findBuyer(userEmail);

        validatePurchaseReturn(purchaseOrder, foundBuyer);

        purchaseOrder.getProduct().forEach( product ->
            this.batchService.returnProductsFromBatches(product.getProduct().getProductId(), product.getQuantity())
        );

        purchaseOrder.setStatus(OrderStatusEnum.RETURNED);
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        return PurchaseOrderMapper.entityToResponse(purchaseOrder);
    }

    public List<PurchaseOrderResponse> getReturnedPurchaseOrders() {
        List<PurchaseOrder> returnedPurchaseOrders = purchaseOrderRepository.findPurchaseOrderByStatus(OrderStatusEnum.RETURNED);

        if (returnedPurchaseOrders.size() <= 0) {
            throw new ApiException("404", "Returned purchase orders not found", 404);
        }

        return PurchaseOrderMapper.entityListToResponseList(returnedPurchaseOrders);
    }

    private void validatePurchaseReturn(PurchaseOrder returnedPurchaseOrder, Buyer buyer) {
        if (returnedPurchaseOrder == null) {
            throw new ApiException("404", "Purchase order was not found", 404);
        } else if (returnedPurchaseOrder.getStatus() != OrderStatusEnum.DELIVERED) {
            throw new ApiException("400", "Only delivered purchases can be returned", 400);
        } else if (returnedPurchaseOrder.getDate().isBefore(LocalDate.now().minusDays(1))) {
            throw new ApiException("400", "Purchases over 1 day cannot be returned", 400);
        } else if (buyer.getUserId() != returnedPurchaseOrder.getBuyer().getUserId() ) {
            throw new ApiException("400", "Purchases can only be returned by the original buyer account", 400);
        }
    }

    private List<PurchaseOrderProduct> createOrderProducts(List<PurchaseOrderProductsForm> purchaseOrderForm) {
        return purchaseOrderForm
                .stream()
                .map(p -> {
                    Product foundProduct = productService.findProductById(p.getProductId());
                    this.checkProduct(p);

                    return PurchaseOrderProduct.builder().quantity(p.getQuantity()).product(foundProduct).build();
                })
                .collect(Collectors.toList());
    }

    private void checkProduct(PurchaseOrderProductsForm purchaseOrderProduct) {
        long totalProductQuantity = productService.getProductQuantity(purchaseOrderProduct.getProductId());

        if (totalProductQuantity - purchaseOrderProduct.getQuantity() < 0)
            throw new ApiException("400", "The quantity in storage is less than the quantity ordered", 400);
    }

    private BigDecimal getPurchaseOrderTotalValue(PurchaseOrder purchaseOrder) {
        return purchaseOrder.getProduct()
                .stream()
                .map(p -> p.getProduct().getValue().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
