package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Buyer;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.BuyerRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.BuyerService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserBuyerMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BuyerServiceTest {
    @InjectMocks
    private BuyerService buyerService;

    @Mock
    private BuyerRepository buyerRepository;

    @BeforeEach
    public void setUp() throws Exception {
        buyerRepository.deleteAll();
        this.buyerService = new BuyerService(buyerRepository);
    }

    @Test
    @DisplayName("should return a Buyer if findBuyer succeeds")
    void findBuyer_succeeds() {
        //arrange
        given(buyerRepository.findById(1L)).willReturn(Optional.of(UserBuyerMock.validBuyer(1L)));

        // act
        Buyer user = this.buyerService.findBuyer(1L);

        // assert
        assertThat(user.getUserId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("buyer@email.com");
        assertThat(user.getRole()).isEqualTo("BUYER");
    }

    @Test
    @DisplayName("should return a Buyer if findBuyer succeeds")
    void findBuyerByEmail_succeeds() {
        Buyer buyerMock = UserBuyerMock.validBuyer();

        //arrange
        given(buyerRepository.findByEmail(buyerMock.getEmail())).willReturn(Optional.of(buyerMock));

        // act
        Buyer user = this.buyerService.findBuyer(buyerMock.getEmail());

        // assert
        assertThat(user.getEmail()).isEqualTo("buyer@email.com");
        assertThat(user.getRole()).isEqualTo("BUYER");
    }

    @Test
    @DisplayName("should throw ApiException if try find buyer that not exist")
    void findBuyer_notExistsBuyer() {
        //arrange
        given(buyerRepository.findById(1L)).willReturn(Optional.empty());

        // assert
        assertThatThrownBy(() -> buyerService.findBuyer(1L)).isInstanceOf(ApiException.class);
        assertThatThrownBy(() -> buyerService.findBuyer(1L)).hasMessage("Buyer not found with this id");
    }
}
