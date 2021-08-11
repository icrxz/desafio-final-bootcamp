package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ProductNotFoundException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.ProductRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.ProductService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SellerService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.ProductMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SellerService sellerService;

    @BeforeEach
    public void setUp() throws Exception {
        productService = new ProductService(productRepository, sellerService);
    }

    @Test
    @DisplayName("should return a product if ID succeeds")
    void findProductByIdTest () {
        UUID productId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");
        given(productRepository.findById(productId)).willReturn(java.util.Optional.of(ProductMock.create()));
        Product productTest = productService.findProductById(productId);

        assertEquals(productId, productTest.getProductId());

    }

    @Test
    @DisplayName("should throws if id not belong to any Product")
    void findSectionById_sectionNotFound() {
        UUID productId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

        assertThatThrownBy(() -> productService.findProductById(productId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("should throws if id not belong to any Product")
    void findSectionById_sectionNotFoundMessage() {
        UUID productId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");

        Throwable exception = assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId));
        assertEquals("Product not found with this id", exception.getMessage());

    }
    /*
     @Test
    void createProductTest () {
        ProductForm productForm = new ProductForm();
        productForm.setName("any_name");
        productForm.setType(ProductTypeEnum.CARNES);
        productForm.setSellerId(1L);

        given(sellerService.findSeller(1L)).willReturn(UserSellerMock.validSeller(Optional.of(1L)));

        //Seller foundSeller = sellerService.findSeller(1L);

        ProductResponse product = productService.createProduct(productForm);

        assertEquals("any_name", product.getName());
    }
     */


}