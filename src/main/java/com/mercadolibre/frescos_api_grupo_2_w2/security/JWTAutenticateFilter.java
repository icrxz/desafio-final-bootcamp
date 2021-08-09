package com.mercadolibre.frescos_api_grupo_2_w2.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JWTAutenticateFilter extends UsernamePasswordAuthenticationFilter {

    public static final int TOKEN_EXPIRATION = 86_400_000;
    public static final String TOKEN_PASSWORD = System.getenv("TOKEN_PASSWORD");

    private UserService userService;

    private final AuthenticationManager authenticationManager;

    public JWTAutenticateFilter(AuthenticationManager authenticationManager, UserService userService) {
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
                                            Authentication authResult) throws IOException, ServletException {

        DetailUserData usuarioData = (DetailUserData) authResult.getPrincipal();
        usuarioData.getAuthorities().toString();
        GrantedAuthority claim = usuarioData.getAuthorities().stream().findFirst().orElse(null);

        String token;
        if (claim != null) {
            String role = claim.getAuthority().toString();
            token = JWT.create().
                    withSubject(usuarioData.getUsername())
                    .withClaim(role, role)
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC512(TOKEN_PASSWORD));
        } else {
            token = JWT.create().
                    withSubject(usuarioData.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                    .sign(Algorithm.HMAC512(TOKEN_PASSWORD));
        }
        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
