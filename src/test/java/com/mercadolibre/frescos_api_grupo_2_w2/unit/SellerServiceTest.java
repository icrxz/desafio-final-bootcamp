package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SellerRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SellerService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {

    @InjectMocks
    private SellerService sellerService;

    @Mock
    private SellerRepository sellerRepository;

    @BeforeEach
    public void setUp() throws Exception {
        sellerRepository.deleteAll();
        sellerService = new SellerService(sellerRepository);
    }

    @Test
    @DisplayName("should return a Seller if findSeller succeeds")
    void findSeller_succeeds() {
        //arrange
        given(sellerRepository.findById(1L)).willReturn(Optional.of(UserSellerMock.validSeller(1L)));

        // act
        Seller user = this.sellerService.findSeller(1L);

        // assert
        assertThat(user.getUserId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("seller@email.com");
        assertThat(user.getRole()).isEqualTo("SELLER");
    }

    @Test
    @DisplayName("should throw if findSeller succeeds")
    void findSeller_notFoundSeller() {
        //arrange
        given(sellerRepository.findById(1L)).willReturn(Optional.empty());

        // assert
        assertThatThrownBy(() -> sellerService.findSeller(1L))
                .isInstanceOf(ApiException.class);
    }
}
