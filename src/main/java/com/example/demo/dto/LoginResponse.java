package com.example.demo.dto;

import java.util.List;

public record LoginResponse(
        String username,
        List<String> groups,
        List<String> ldapScopes,
        String accessToken,
        String refreshToken) {
}