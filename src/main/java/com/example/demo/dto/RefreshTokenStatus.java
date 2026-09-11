package com.example.demo.dto;

import java.time.LocalDateTime;

public record RefreshTokenStatus(
        String username,
        Long tokenId,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        Boolean revoked,
        String expirationStatus,
        String totalDuration,
        String timeLeft) {
}