package com.example.demo.dto;

public record LdapUserInfo(
        String username,
        String displayName,
        String email) {
}