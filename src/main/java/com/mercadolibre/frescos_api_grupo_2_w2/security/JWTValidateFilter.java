package com.mercadolibre.frescos_api_grupo_2_w2.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import com.mercadolibre.frescos_api_grupo_2_w2.services.UserService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JWTValidateFilter extends BasicAuthenticationFilter {
//    @Autowired
//    private UserService userService;

    public static final String HEADER_ATRIBUTE = "Authorization";
    public static final String ATRIBUTE_PREFIX = "Bearer ";

    public JWTValidateFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String atributo = request.getHeader(HEADER_ATRIBUTE);

        if (atributo == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!atributo.startsWith(ATRIBUTE_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = atributo.replace(ATRIBUTE_PREFIX, "");
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

        String user = JWT.require(Algorithm.HMAC512(JWTAutenticateFilter.TOKEN_PASSWORD))
                .build()
                .verify(token)
                .getSubject();

        if (user == null) {
            return null;
        }

        Map<String, Claim> listClaims = JWT.require(Algorithm.HMAC512(JWTAutenticateFilter.TOKEN_PASSWORD))
                .build()
                .verify(token)
                .getClaims();

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Claim claim = listClaims.entrySet().stream().findFirst().get().getValue();
        String string = claim.toString().replaceAll("^\"|\"$", "");
        authorities.add(new SimpleGrantedAuthority(string));

        return new UsernamePasswordAuthenticationToken(user,null, authorities);
    }
}
