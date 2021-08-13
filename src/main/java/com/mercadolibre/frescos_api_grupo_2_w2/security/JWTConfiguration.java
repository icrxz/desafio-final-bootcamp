package com.mercadolibre.frescos_api_grupo_2_w2.security;

import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class JWTConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public JWTConfiguration(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/user/supervisor").hasAuthority("SUPERVISOR")
                .antMatchers(HttpMethod.GET, "/api/v1/user").hasAuthority("SUPERVISOR")
                .antMatchers(HttpMethod.POST, "/api/v1/fresh-products/inboundorder").hasAnyAuthority("SUPERVISOR", "SELLER")
                .antMatchers("/api/v1/fresh-products/orders").hasAuthority("BUYER")
                .antMatchers(HttpMethod.POST, "/api/v1/fresh-products").hasAnyAuthority("SUPERVISOR", "SELLER")
                .antMatchers(HttpMethod.GET, "/api/v1/fresh-products").hasAnyAuthority("SUPERVISOR", "BUYER")
                .antMatchers(HttpMethod.GET, "/api/v1/fresh-products/list").hasAnyAuthority("SUPERVISOR", "BUYER")
                .antMatchers(HttpMethod.POST, "/api/v1/sections").hasAuthority("SUPERVISOR")
                .antMatchers("/api/v1/fresh-products/warehouses/*").hasAuthority("SUPERVISOR");

        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/ping").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/user/seller").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/user/buyer").permitAll()
                .antMatchers(HttpMethod.GET, "/fake").permitAll()
                .antMatchers(HttpMethod.GET, "/v3/api-docs").permitAll();


        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticateFilter(authenticationManager(), userService))
                .addFilter(new JWTValidateFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
