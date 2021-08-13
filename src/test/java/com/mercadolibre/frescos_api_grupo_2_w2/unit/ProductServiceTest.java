package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.ProductForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.ProductResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
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
        UUID productId = ProductMock.productID;
        given(productRepository.findById(productId)).willReturn(java.util.Optional.of(ProductMock.validProduct(null)));
        Product productTest = productService.findProductById(productId);

        assertEquals(productId, productTest.getProductId());
    }

    @Test
    @DisplayName("should throws if id not belong to any Product")
    void findProductById_productNotFound() {
        UUID productId = ProductMock.productID;

        assertThatThrownBy(() -> productService.findProductById(productId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("should throws if id not belong to any Product")
    void findProductById_productNotFoundMessage() {
        UUID productId = ProductMock.productID;

        Throwable exception = assertThrows(ProductNotFoundException.class, () -> productService.findProductById(productId));
        assertEquals("Product not found with this id", exception.getMessage());
    }

     @Test
     @DisplayName("should return a Product creation succeeds")
     void createProductSucceeds () {
         UUID productId = UUID.fromString("0f14d0ab-9605-4a62-a9e4-5ed26688389b");
         Seller sellerMock = UserSellerMock.validSeller(1L);

         ProductForm productForm = new ProductForm();
         productForm.setName("any_name");
         productForm.setType(ProductTypeEnum.FRESH);
         productForm.setSellerId(sellerMock.getUserId());

         Product product = Product.builder()
                 .name(productForm.getName())
                 .seller(sellerMock)
                 .type(productForm.getType())
                 .build();

         given(this.sellerService.findSeller(1L)).willReturn(UserSellerMock.validSeller(1L));
         given(this.productRepository.save(any())).willReturn(product);

         ProductResponse createProduct = this.productService.createProduct(productForm);

         // assert
         assertThat(createProduct.getName()).isEqualTo("any_name");
         assertThat(createProduct.getType()).isEqualTo(ProductTypeEnum.FRESH);
         assertThat(createProduct.getSellerId()).isEqualTo(1L);
    }


}