package ru.ssau.auth.dto;

public record LoginResponse(String token, String role, long userId) {
}