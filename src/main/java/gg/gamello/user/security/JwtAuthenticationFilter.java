package gg.gamello.user.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.gamello.user.dao.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            String[] tokenParsed = token.split("\\.");

            byte[] decodedToken = Base64.getDecoder().decode(tokenParsed[1]);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode actualObj = objectMapper.readTree(new String(decodedToken));

            User user = objectMapper.convertValue(actualObj.get("user"), User.class);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.debug("Error occurred while setting authentication: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer"))
            throw new Exception("Missing header or bad prefix");
        return token.substring(7);
    }
}