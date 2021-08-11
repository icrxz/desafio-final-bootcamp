package com.mercadolibre.frescos_api_grupo_2_w2.services;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SellerForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.SupervisorForm;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.UserForm;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserForm userForm) {
        User newUser = null;
        if (userForm instanceof SellerForm) {
            newUser = new Seller();
            BeanUtils.copyProperties(userForm, newUser);
            newUser.setRole("SELLER");
        } else if (userForm instanceof SupervisorForm) {
            newUser = new Supervisor();
            BeanUtils.copyProperties(userForm, newUser);
            newUser.setRole("SUPERVISOR");
        } else {
            throw new InternalServerErrorException(null);
        }

        Optional<User> user = this.userRepository.findByEmail(userForm.getEmail());
        if (!user.isEmpty()) {
            throw new UserAlreadyExists("Este email já está cadastrado");
        }
        return this.userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário [" + s + "] não encontrado");
        }
        return new DetailUserData(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    public User loadUserByEmail(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário [" + s + "] não encontrado");
        }
        return user.get();
    }
}
