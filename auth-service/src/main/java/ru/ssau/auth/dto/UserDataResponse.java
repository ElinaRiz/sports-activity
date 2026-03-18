package ru.ssau.auth.dto;

public record UserDataResponse(String login, String role, String secret) {
}