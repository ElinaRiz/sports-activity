package ru.ssau.api_gateway.dto;

public record UserDataResponse(String login, String role, String secret) {
}