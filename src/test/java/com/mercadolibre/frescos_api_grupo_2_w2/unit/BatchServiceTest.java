package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses.BatchResponse;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Batch;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Product;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.enums.ProductTypeEnum;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ProductNotFoundException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.BatchRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.BatchService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.ProductService;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SectionService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.BatchMock;
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
import java.util.ArrayList;
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
        Batch response = batchService.createBatch(BatchMock.validBatchForm(), SectionMock.validSection());

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
        assertThatThrownBy(() -> batchService.createBatch(BatchMock.validBatchForm(), SectionMock.validSection()))
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
        assertThatThrownBy(() -> batchService.createBatch(BatchMock.validBatchForm(), SectionMock.validSection()))
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
}
