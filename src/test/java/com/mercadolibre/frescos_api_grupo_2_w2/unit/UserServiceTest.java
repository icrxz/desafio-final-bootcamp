package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.UserForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.InternalServerErrorException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.UserAlreadyExists;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
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
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        userRepository.deleteAll();
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("should return a User if UserSeller creation succeeds")
    void createUser_UserSellerSucceeds() {
        //Seller Mock
        Seller selerMock = UserSellerMock.validSeller(null);

        //Seller Mock Created
        Seller selerCreatedMock = UserSellerMock.validSeller(Optional.of(1L));

        //arrange
        given(userRepository.save(selerMock)).willReturn(selerCreatedMock);

        // act
        User createdUser = this.userService.createUser(UserSellerMock.validSellerDTO());

        // assert
        assertThat(createdUser.getUserId()).isEqualByComparingTo(1L);
        assertThat(createdUser.getEmail()).isEqualTo("any_email@email.com");
        assertThat(createdUser.getPassword()).isEqualTo("any_password");
        assertThat(createdUser.getRole()).isEqualTo("SELLER");
    }

    @Test
    @DisplayName("should return a User if UserSeller creation succeeds")
    void createUser_UserSupervisorSucceeds() {
        Supervisor supervisorMock = UserSupervisorMock.validSupervisor(null);
        Supervisor supervisorCreatedMock = UserSupervisorMock.validSupervisor(Optional.of(1L));

        //arrange
        given(userRepository.save(supervisorMock)).willReturn(supervisorCreatedMock);

        // act
        User createdUser = this.userService.createUser(UserSupervisorMock.validSupervisorDTO());

        // assert
        assertThat(createdUser.getUserId()).isEqualByComparingTo(1L);
        assertThat(createdUser.getEmail()).isEqualTo("any_email@email.com");
        assertThat(createdUser.getPassword()).isEqualTo("any_password");
        assertThat(createdUser.getRole()).isEqualTo("SUPERVISOR");
    }

    @Test()
    @DisplayName("should throws if email already are registered")
    void createUser_UserAlreadyRegistered() {
        //arrange
        given(userRepository.findByEmail("any_email@email.com")).willReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.createUser(UserSupervisorMock.validSupervisorDTO()))
                .isInstanceOf(UserAlreadyExists.class);
    }

    @Test()
    @DisplayName("should throws if User provided is not a instance of valid classes")
    void createUser_UserClassNotInstanceOfValidClasses() {
        //arrange
        class AnyUserClass extends UserForm {}

        //act
        assertThatThrownBy(() -> userService.createUser(new AnyUserClass()))
                .isInstanceOf(InternalServerErrorException.class);
    }
}
