package com.mercadolibre.frescos_api_grupo_2_w2.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JWTAuthenticateFilter extends UsernamePasswordAuthenticationFilter {

    public static final int TOKEN_EXPIRATION = 86_400_000;
    //public static final String TOKEN_PASSWORD = System.getenv("TOKEN_PASSWORD");
    public static final String TOKEN_PASSWORD = "463408a1-54c9-4307-bb1c-6cced559f5a7";

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticateFilter(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);

            User userSearched = this.userService.loadUserByEmail(user.getEmail());

            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            if (userSearched != null) {
                authorities.add(new SimpleGrantedAuthority(userSearched.getRole()));
            }

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            ));

        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar usuario", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        DetailUserData userData = (DetailUserData) authResult.getPrincipal();
        userData.getAuthorities().toString();
        GrantedAuthority claim = userData.getAuthorities().stream().findFirst().orElse(null);

        String token;
        if (claim != null) {
            String role = claim.getAuthority();
            token = JWT.create().
                    withSubject(userData.getUsername())
                    .withClaim(role, role)
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC512(TOKEN_PASSWORD));
        } else {
            token = JWT.create().
                    withSubject(userData.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC512(TOKEN_PASSWORD));
        }
        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
