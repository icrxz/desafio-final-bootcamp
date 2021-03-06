package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.BuyerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SupervisorForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.UserForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Buyer;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.InternalServerErrorException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.UserAlreadyExists;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.UserRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.security.DetailUserData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User createUser(UserForm userForm) {
        User newUser = null;

        if (userForm instanceof SellerForm) {
            newUser = new Seller();
            newUser.setRole("SELLER");
        } else if (userForm instanceof SupervisorForm) {
            newUser = new Supervisor();
            newUser.setRole("SUPERVISOR");
        } else if (userForm instanceof BuyerForm) {
            newUser = new Buyer();
            newUser.setRole("BUYER");
        } else {
            throw new InternalServerErrorException(null);
        }

        BeanUtils.copyProperties(userForm, newUser);
        Optional<User> user = this.userRepository.findByEmail(userForm.getEmail());

        if (user.isPresent()) {
            throw new UserAlreadyExists("This email is already registered");
        }

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        return this.userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User [" + s + "] not found");
        }
        return new DetailUserData(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    public User loadUserByEmail(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User [" + s + "] not found");
        }
        return user.get();
    }
}
