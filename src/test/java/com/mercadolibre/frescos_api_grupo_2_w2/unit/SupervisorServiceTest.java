package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SupervisorRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.SupervisorService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSupervisorMock;
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
public class SupervisorServiceTest {
    @InjectMocks
    private SupervisorService SupervisorService;

    @Mock
    private SupervisorRepository SupervisorRepository;

    @BeforeEach
    public void setUp() throws Exception {
        SupervisorRepository.deleteAll();
        SupervisorService = new SupervisorService(SupervisorRepository);
    }

    @Test
    @DisplayName("should return a Supervisor if findSupervisor succeeds")
    void findSupervisor_succeeds() {
        //arrange
        given(SupervisorRepository.findById(1L)).willReturn(Optional.of(UserSupervisorMock.validSupervisor(Optional.of(1L))));

        // act
        Supervisor user = this.SupervisorService.findSupervisor(1L);

        // assert
        assertThat(user.getUserId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("any_email@email.com");
        assertThat(user.getPassword()).isEqualTo("any_password");
        assertThat(user.getRole()).isEqualTo("SUPERVISOR");
    }

    @Test
    @DisplayName("should throw if findSupervisor succeeds")
    void findSupervisor_notFoundSupervisor() {
        //arrange
        given(SupervisorRepository.findById(1L)).willReturn(Optional.empty());

        // assert
        assertThatThrownBy(() -> SupervisorService.findSupervisor(1L))
                .isInstanceOf(ApiException.class);
    }
}
