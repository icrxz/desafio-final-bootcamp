package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.UserForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.InternalServerErrorException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.UserAlreadyExists;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SellerRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SupervisorRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSellerMock;
import com.mercadolibre.frescos_api_grupo_2_w2.util.mocks.UserSupervisorMock;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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

    @Test()
    @DisplayName("should return a valid user by username provided if was registered")
    void loadUserByUsername_succeeds() {
        //arrange
        given(userRepository.findByEmail("any_email@email.com")).willReturn(Optional.of(UserSellerMock.validSeller(Optional.of(1L))));

        // act
        UserDetails createdUser = this.userService.loadUserByUsername("any_email@email.com");

        // assert
        assertThat(createdUser.getUsername()).isEqualTo("any_email@email.com");
    }

    @Test()
    @DisplayName("should throws if email provided not belong to any user")
    void loadUserByUsername_notFoundUser() {
        //arrange
        given(userRepository.findByEmail("any_email@email.com")).willReturn(Optional.empty());

        //act
        assertThatThrownBy(() -> userService.loadUserByUsername("any_email@email.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test()
    @DisplayName("should return a valid user list if getUsers succeeds")
    void getUsers_succeeds() {
        List<User> users = Arrays.asList(UserSellerMock.validSeller(Optional.of(1L)), UserSupervisorMock.validSupervisor(Optional.of(2L)));
        //arrange
        given(userRepository.findAll()).willReturn(users);
        List<User> responseUsers = this.userService.getUsers();

        //act
        assertThat(responseUsers).isEqualTo(users);
    }

    @Test()
    @DisplayName("should return a valid user by email provided if was registered")
    void loadUserByEmail_succeeds () {
        //arrange
        given(userRepository.findByEmail("any_email@email.com")).willReturn(Optional.of(UserSellerMock.validSeller(Optional.of(1L))));

        // act
        User user = this.userService.loadUserByEmail("any_email@email.com");

        // assert
        assertThat(user.getEmail()).isEqualTo("any_email@email.com");
    }

    @Test()
    @DisplayName("should throw if email provided not belong to any user")
    void loadUserByEmail_notFoundUser () {
        //arrange
        given(userRepository.findByEmail("any_email@email.com")).willReturn(Optional.empty());

        //act
        assertThatThrownBy(() -> userService.loadUserByEmail("any_email@email.com"))
          .isInstanceOf(UsernameNotFoundException.class);
    }
}
