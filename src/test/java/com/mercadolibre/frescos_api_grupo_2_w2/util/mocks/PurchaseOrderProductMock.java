package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.purchaseOrder.PurchaseOrderProductsForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrder;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.PurchaseOrderProduct;

public class PurchaseOrderProductMock {
    public static PurchaseOrderProductsForm validPurchaseOrderProductsForm () {
        PurchaseOrderProductsForm purchaseOrderProductsForm = new PurchaseOrderProductsForm();

        purchaseOrderProductsForm.setProductId(ProductMock.validProduct(null).getProductId());
        purchaseOrderProductsForm.setQuantity(2);

        return purchaseOrderProductsForm;
    }

    public static PurchaseOrderProduct validPurchaseOrderProduct (PurchaseOrder purchaseOrder) {
        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();

        purchaseOrderProduct.setPurchaseOrder(purchaseOrder);
        purchaseOrderProduct.setProduct(ProductMock.validProduct(null));
        purchaseOrderProduct.setQuantity(2);

        return purchaseOrderProduct;
    }
}
