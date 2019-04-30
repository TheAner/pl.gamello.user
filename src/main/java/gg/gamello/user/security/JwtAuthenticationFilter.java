package gg.gamello.user.security;

import gg.gamello.user.dao.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final PublicKey publicKey;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, PublicKey publicKey) {
        super(authenticationManager);
        this.publicKey = publicKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, HttpClientErrorException {
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(request));
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        try {
            String token = resolveToken(request);
            Claims payload = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();

            User user = new User();
            user.setId(UUID.fromString(payload.getSubject()));
            user.setUsername(payload.get("username", String.class));
            user.setEmail(payload.get("email", String.class));

            @SuppressWarnings("unchecked")
            List<GrantedAuthority> authorities = getGrantedAuthorities(payload.get("roles", List.class));
            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Authentication exception [{}:{}] - {}", request.getRemoteAddr(), ex.getClass().getSimpleName(), ex.getMessage());
            return null;
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private String resolveToken(HttpServletRequest request) throws IllegalArgumentException {
        String headerValue = request.getHeader("Authorization");
        if(StringUtils.isEmpty(headerValue) || !headerValue.startsWith("Bearer "))
            return null;
        return headerValue.substring("Bearer".length() + 1);
    }
}
