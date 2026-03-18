package ru.ssau.management_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
public class GatewayAuthFilter extends OncePerRequestFilter {
    @Value("${gateway.secret}")
    private String gatewaySecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String login = request.getHeader("X-Login");
        String role = request.getHeader("X-Role");
        String secret = request.getHeader("X-Secret");

        if (login == null || role == null || secret == null || !verifySecret(login, role, secret)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(login, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private boolean verifySecret(String login, String role, String secret) {
        try {
            String data = login + ":" + role;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(gatewaySecret.getBytes(), "HmacSHA256"));
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String expected = Base64.getEncoder().encodeToString(rawHmac);
            return expected.equals(secret);
        } catch (Exception e) {
            return false;
        }
    }
}