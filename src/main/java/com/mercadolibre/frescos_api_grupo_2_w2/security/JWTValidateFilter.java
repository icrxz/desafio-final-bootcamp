package com.mercadolibre.frescos_api_grupo_2_w2.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public static final String HEADER_ATTRIBUTE = "Authorization";
    public static final String ATTRIBUTE_PREFIX = "Bearer ";

    public JWTValidateFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String attribute = request.getHeader(HEADER_ATTRIBUTE);

        if (attribute == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!attribute.startsWith(ATTRIBUTE_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = attribute.replace(ATTRIBUTE_PREFIX, "");
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

        String user = JWT.require(Algorithm.HMAC512(JWTAuthenticateFilter.TOKEN_PASSWORD))
                .build()
                .verify(token)
                .getSubject();

        if (user == null) {
            return null;
        }

        Map<String, Claim> listClaims = JWT.require(Algorithm.HMAC512(JWTAuthenticateFilter.TOKEN_PASSWORD))
                .build()
                .verify(token)
                .getClaims();

        listClaims.entrySet().stream().forEach(m -> System.out.println(m));
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        String string = listClaims.get("role").toString();
        authorities.add(new SimpleGrantedAuthority(string.replaceAll("^\"|\"$", "")));

        return new UsernamePasswordAuthenticationToken(user,null, authorities);
    }
}
