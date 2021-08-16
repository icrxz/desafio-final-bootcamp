package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.OrderBatch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.BatchRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.BatchService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.ProductService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SectionService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.BatchMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.InboundOrderMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.ProductMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.SectionMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BatchServiceTest {

    @InjectMocks
    private BatchService batchService;

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private ProductService productService;

    @Mock
    private SectionService sectionService;


    @BeforeEach
    public void setUp() throws Exception {
        batchService = new BatchService(batchRepository, productService, sectionService);
    }

    @Test
    @DisplayName("should Batch if createBatch succeeds")
    void createBatch_succeeds () {
        //arrange
        given(productService.findProductById(ProductMock.productID)).willReturn(ProductMock.validProduct(null));
        given(batchRepository.save(any())).willReturn(BatchMock.validBatch(null));
        given(sectionService.getSectionCurrentSize(any())).willReturn(100L);

        //act
        Batch response = batchService.createBatch(BatchMock.validBatchForm(), SectionMock.validSection(), InboundOrderMock.validInboundOrder());

        //assert
        assertThat(response.getBatchId()).isEqualTo(BatchMock.validBatch(null).getBatchId());
        assertThat(response.getInboundOrder()).isEqualTo(BatchMock.validBatch(null).getInboundOrder());
    }

    @Test
    @DisplayName("should throws if section does not have enough capacity")
    void createBatch_SectionDontHaveEnoughCapacity () {
        //arrange
        given(productService.findProductById(ProductMock.productID)).willReturn(ProductMock.validProduct(null));
        given(sectionService.getSectionCurrentSize(any())).willReturn(0L);

        //act
        assertThatThrownBy(() -> batchService.createBatch(BatchMock.validBatchForm(), SectionMock.validSection(), InboundOrderMock.validInboundOrder()))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("should throws if section and product have different types")
    void createBatch_SectionAndProductsDifferentTypes () {
        //arrange
        Product product = ProductMock.validProduct(null);
        product.setType(ProductTypeEnum.REFRIGERATED);
        given(productService.findProductById(ProductMock.productID)).willReturn(product);
        given(sectionService.getSectionCurrentSize(any())).willReturn(100L);

        //act
        assertThatThrownBy(() -> batchService.createBatch(BatchMock.validBatchForm(), SectionMock.validSection(), InboundOrderMock.validInboundOrder()))
                .isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("should throws if section and product have different types")
    void getBatchByProductId_succeeds () {
        //arrange
        UUID productId = UUID.randomUUID();
        Product product = ProductMock.validProduct(productId);
        Batch batch1 = BatchMock.validBatch(product);
        Batch batch2 = BatchMock.validBatch(product);

        given(batchRepository.findBatchesByProduct_productId(productId)).willReturn(Arrays.asList(batch1, batch2));

        //act
        List<Batch> response = batchService.findBatchesByProduct(productId);

        //assert
        assertEquals(2, response.size());
        assertEquals(batch1.getBatchId(), response.get(0).getBatchId());
    }

    @Test
    @DisplayName("should return a Batch List if succeeds")
    void dueDateBatch_Succeeds() {
        //arrange
        given(this.batchRepository.findByDueDateLessThanEqualOrderByDueDateAsc(LocalDate.now().plusDays(10))).willReturn(Arrays.asList(BatchMock.validBatch(null)));

        //act
        List<Batch> batches = this.batchService.dueDateBatch(10);

        //assert
        assertTrue(!batches.isEmpty());
    }

    @Test
    @DisplayName("should return a empty list if dueDateBatch succeeds")
    void dueDateBatch_SucceedsButReturnsEmptylist() {
        //arrange
        given(this.batchRepository.findByDueDateLessThanEqualOrderByDueDateAsc(LocalDate.now().plusDays(10))).willReturn(Arrays.asList(BatchMock.validBatch(null)));

        //act
        List<Batch> batches = this.batchService.dueDateBatch(10);

        //assert
        assertTrue(!batches.isEmpty());
    }

    @Test
    @DisplayName("should return a Batch List if succeeds")
    void dueDateAndProductTypeBatch_Succeeds() {
        //arrange
        given(this.batchRepository.findByDueDateLessThanEqualOrderByDueDateAsc(LocalDate.now().plusDays(10))).willReturn(Arrays.asList(BatchMock.validBatch(null)));

        //act
        List<Batch> batches = this.batchService.dueDateBatch(10);

        //assert
        assertTrue(!batches.isEmpty());
    }

    @Test
    @DisplayName("should return a empty list if dueDateBatch succeeds")
    void dueDateAndProductTypeBatch_SucceedsButReturnsEmptylist() {
        //arrange
        given(this.batchRepository.findDueDateLessAndProductType(LocalDate.now().plusDays(10), ProductTypeEnum.FRESH)).willReturn(Arrays.asList(BatchMock.validBatch(null)));

        //act
        List<Batch> batches = this.batchService.dueDateAndProductTypeBatch(10L, ProductTypeEnum.FRESH);

        //assert
        assertTrue(!batches.isEmpty());
    }

    @DisplayName("sort batch list by current quantity")
    void findBatchByProductOrdersCurrentQuantityAsc () {
        //arrange
        UUID productId = UUID.randomUUID();
        Product product = ProductMock.validProduct(productId);
        Batch batch1 = BatchMock.validBatch(product);
        batch1.setCurrentQuantity(10);
        Batch batch2 = BatchMock.validBatch(product);
        batch2.setCurrentQuantity(20);
        Batch batch3 = BatchMock.validBatch(product);
        batch3.setCurrentQuantity(30);

        given(batchRepository.findBatchesByProduct_productIdOrderByCurrentQuantityAsc(productId)).willReturn(Arrays.asList(batch1, batch2, batch3));

        //act
        List<Batch> response = batchService.findBatchesByProductOrder(productId,OrderBatch.C);

        //assert
        assertEquals(3, response.size());
        assertEquals(10, response.get(0).getCurrentQuantity());
        assertEquals(20, response.get(1).getCurrentQuantity());
        assertEquals(30, response.get(2).getCurrentQuantity());

        //assertEquals(responseDate.get(0).getCurrentQuantity(), response.get(0).getCurrentQuantity());
    }

    @Test
    @DisplayName("sort batch list by due date")
    void findBatchByProductOrdersDueDateAsc () {
        //arrange
        UUID productId = UUID.randomUUID();
        Product product = ProductMock.validProduct(productId);
        Batch batch1 = BatchMock.validBatch(product);
        batch1.setDueDate(LocalDate.parse("2021-07-07"));
        Batch batch2 = BatchMock.validBatch(product);
        batch2.setDueDate(LocalDate.parse("2022-07-07"));
        Batch batch3 = BatchMock.validBatch(product);
        batch3.setDueDate(LocalDate.parse("2023-07-07"));

        given(batchRepository.findBatchesByProduct_productIdOrderByDueDateAsc(productId)).willReturn(Arrays.asList(batch1, batch2, batch3));

        //act
        List<Batch> response = batchService.findBatchesByProductOrder(productId,OrderBatch.F);

        //assert
        assertEquals(3, response.size());
        assertEquals(LocalDate.parse("2021-07-07"), response.get(0).getDueDate());
        assertEquals(LocalDate.parse("2022-07-07"), response.get(1).getDueDate());
        assertEquals(LocalDate.parse("2023-07-07"), response.get(2).getDueDate());

    }
}
