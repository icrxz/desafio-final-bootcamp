package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderProductsForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.mapper.PurchaseOrderMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.purchaseOrder.PurchaseOrderResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Buyer;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrderProduct;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final BuyerService buyerService;
    private final ProductService productService;

    @Autowired
    public PurchaseOrderService(
            PurchaseOrderRepository purchaseOrderRepository,
            BuyerService buyerService,
            ProductService productService
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.buyerService = buyerService;
        this.productService = productService;
    }

    public PurchaseOrderResponse findPurchaseOrderById(UUID purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);

        if (purchaseOrder == null) {
            throw new ApiException("404", "Purchase order not found with this id", 404);
        }

        purchaseOrder.getProduct().forEach(tst -> System.out.println(tst.getProduct().getName() + " - " + tst.getProduct().getValue()));

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
