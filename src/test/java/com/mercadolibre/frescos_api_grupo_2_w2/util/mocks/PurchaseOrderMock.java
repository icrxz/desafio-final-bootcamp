package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderStatusEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class PurchaseOrderMock {
    public static UUID purchaseOrderId = UUID.randomUUID();

    public static PurchaseOrderForm validPurchaseOrderForm () {
        PurchaseOrderForm purchaseOrderForm = new PurchaseOrderForm();

        purchaseOrderForm.setDate(LocalDate.now());
        purchaseOrderForm.setBuyerId(UserBuyerMock.validBuyer(1L).getUserId());
        purchaseOrderForm.setStatus(OrderStatusEnum.CONFIRMING);
        purchaseOrderForm.setProducts(Arrays.asList(PurchaseOrderProductMock.validPurchaseOrderProductsForm()));

        return purchaseOrderForm;
    }

    public static PurchaseOrderForm validEditPurchaseOrderForm () {
        PurchaseOrderForm purchaseOrderForm = new PurchaseOrderForm();

        purchaseOrderForm.setDate(LocalDate.now().minusDays(10));
        purchaseOrderForm.setBuyerId(UserBuyerMock.validBuyer(1L).getUserId());
        purchaseOrderForm.setStatus(OrderStatusEnum.TRANSPORT);
        purchaseOrderForm.setProducts(Arrays.asList(PurchaseOrderProductMock.validPurchaseOrderProductsForm()));

        return purchaseOrderForm;
    }

    public static PurchaseOrder validPurchaseOrder (UUID receivedPurchaseOrderId) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setDate(LocalDate.now());
        purchaseOrder.setBuyer(UserBuyerMock.validBuyer(1L));
        purchaseOrder.setStatus(OrderStatusEnum.CONFIRMING);
        purchaseOrder.setProduct(new ArrayList<>());

        if (receivedPurchaseOrderId == null)
            purchaseOrder.setPurchaseOrderId(purchaseOrderId);
        else
            purchaseOrder.setPurchaseOrderId(receivedPurchaseOrderId);

        return purchaseOrder;
    }
}
