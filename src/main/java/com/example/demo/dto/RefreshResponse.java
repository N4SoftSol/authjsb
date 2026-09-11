package com.example.demo.dto;

public record RefreshResponse(
        String accessToken,
        long expiresIn) {
}