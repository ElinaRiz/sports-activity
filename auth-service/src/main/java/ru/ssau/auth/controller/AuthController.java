package ru.ssau.auth.controller;

import lombok.RequiredArgsConstructor;
import ru.ssau.auth.dto.LoginRequest;
import ru.ssau.auth.dto.LoginResponse;
import ru.ssau.auth.dto.UserDataResponse;
import ru.ssau.auth.dto.UserPojo;
import ru.ssau.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserPojo> register(@RequestBody UserPojo pojo) {
        UserPojo response = userService.register(pojo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role")
    public ResponseEntity<UserDataResponse> getRole(@RequestHeader("Authorization") String authHeader) {
        UserDataResponse response = userService.getRole(authHeader);
        return ResponseEntity.ok(response);
    }
}