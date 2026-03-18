package ru.ssau.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ssau.auth.dto.LoginRequest;
import ru.ssau.auth.dto.LoginResponse;
import ru.ssau.auth.dto.UserDataResponse;
import ru.ssau.auth.dto.UserPojo;
import ru.ssau.auth.entity.ERole;
import ru.ssau.auth.entity.User;
import ru.ssau.auth.exception.AuthenticationException;
import ru.ssau.auth.exception.DuplicateNameException;
import ru.ssau.auth.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ssau.auth.security.JwtService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${gateway.secret}")
    private String gatewaySecret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLogin(request.getLogin());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Неверный логин или пароль");
        }
        String token = jwtService.generateToken(user);
        return new LoginResponse(token, user.getRole().name(), user.getId());
    }

    public UserPojo register(UserPojo request) {
        request.setRole(ERole.USER);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = UserPojo.toEntity(request);
        try {
            User saved = userRepository.save(user);
            return UserPojo.fromEntity(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateNameException("Пользователь с таким логином, почтой и телефоном уже существует");
        }
    }

    private String generateSecret(String login, String role) {
        try {
            String data = login + ":" + role;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(gatewaySecret.getBytes(), "HmacSHA256"));
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации секрета", e);
        }
    }

    public UserDataResponse getRole(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Токен не предоставлен или недействителен");
        }
        try {
            String token = authHeader.substring(7);
            Claims claims = jwtService.validateToken(token);
            String login = claims.getSubject();
            String role = claims.get("role", String.class);
            String secret = generateSecret(login, role);

            return new UserDataResponse(login, role, secret);

        } catch (Exception e) {
            throw new AuthenticationException("Неверный токен");
        }
    }
}