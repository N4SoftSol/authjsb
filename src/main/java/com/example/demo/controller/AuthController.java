package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.RefreshToken;
import com.example.demo.exception.RefreshTokenExpiredException;
import com.example.demo.exception.RefreshTokenNotFoundException;
import com.example.demo.exception.RefreshTokenRevokedException;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LdapGroupService ldapGroupService;
    private final LdapGroupScopeMapper ldapGroupScopeMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuditService auditService;
    private final OracleRefreshTokenService refreshTokenservice;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, LdapGroupService ldapGroupService, LdapGroupScopeMapper ldapGroupScopeMapper, RefreshTokenRepository refreshTokenRepository, AuditService auditService, OracleRefreshTokenService refreshTokenservice) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.ldapGroupService = ldapGroupService;
        this.ldapGroupScopeMapper = ldapGroupScopeMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.auditService = auditService;
        this.refreshTokenservice = refreshTokenservice;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        Authentication auth =
                authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken
                                .unauthenticated(
                                        request.username(),
                                        request.password()));

        LdapUserInfo userInfo =
                ldapGroupService.getUserInfo(
                        auth.getName());

        List<RefreshToken> existingTokens =
                refreshTokenRepository
                        .findByUsername(
                                auth.getName());

        for (RefreshToken existing :
                existingTokens) {

            existing.setRevoked(true);
        }

        refreshTokenRepository
                .saveAll(existingTokens);


        String accessToken =
                jwtService.generateToken(auth);

        String refreshTokenValue =
                jwtService.generateRefreshToken();

        RefreshToken refreshToken =
                new RefreshToken();

        refreshToken.setUsername(
                auth.getName());

        refreshToken.setTokenValue(
                refreshTokenValue);

        refreshToken.setCreatedAt(
                LocalDateTime.now());

        refreshToken.setExpiresAt(
                LocalDateTime.now().plusDays(1));
                // User for  expiry checking
//                  LocalDateTime.now().plusSeconds(30));


        refreshToken.setRevoked(false);

        refreshTokenRepository.save(
                refreshToken);


        auditService.log(

                userInfo.username(),
                userInfo.displayName(),
                userInfo.email(),
                "LOGIN",
                "AUTH",
                "SUCCESS",
                "User authenticated successfully");

        System.out.println(
                "Display Name = " +
                        userInfo.displayName());

        System.out.println(
                "Email = " +
                        userInfo.email());

        return ResponseEntity.ok(
                Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshTokenValue));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody RefreshRequest request) {

        RefreshToken token =
                refreshTokenRepository
                        .findByTokenValue(
                                request.refreshToken())
                        .orElseThrow(() ->
                                new RefreshTokenNotFoundException(
                                        "Refresh token not found"));

        if (token.isRevoked()) {

            throw new RefreshTokenRevokedException(
                    "Refresh token has been revoked");
        }

        if (token.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new RefreshTokenExpiredException(
                    "Refresh token has expired");
        }

        Authentication auth =
                UsernamePasswordAuthenticationToken
                        .authenticated(
                                token.getUsername(),
                                null,
                                List.of());

        String accessToken =
                jwtService.generateToken(auth);

        LdapUserInfo userInfo =
                ldapGroupService.getUserInfo(
                        auth.getName());

        auditService.log(
                token.getUsername(),
                userInfo.displayName(),
                userInfo.email(),
                "REFRESH",
                "AUTH",
                "SUCCESS",
                "Access token refreshed");

        return ResponseEntity.ok(
                new RefreshResponse(
                        accessToken,
                        3600));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody LogoutRequest request) {

        RefreshToken token =
                refreshTokenRepository
                        .findByTokenValue(
                                request.refreshToken())
                        .orElseThrow(() ->
                                new RefreshTokenNotFoundException(
                                        "Refresh token not found"));

        token.setRevoked(true);

        refreshTokenRepository.save(
                token);

        LdapUserInfo userInfo =
                ldapGroupService.getUserInfo(
                        token.getUsername());

        auditService.log(
                userInfo.username(),
                userInfo.displayName(),
                userInfo.email(),
                "LOGOUT",
                "AUTH",
                "SUCCESS",
                "Refresh token revoked");
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/whoami")
    public Map<String, Object> whoami(
            Authentication authentication) {

        return Map.of(
                "name", authentication.getName(),
                "authorities",
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());
    }

    @GetMapping("/checktokens")
    public ResponseEntity<Map<String, Object>> getRefreshTokens() {

                    System.out.println(
                    "spring.datasource.url = " +
                                    "spring.datasource.url");

        List<RefreshTokenStatus> users =
                refreshTokenservice.getRefreshTokenStatus();

        Map<String, Object> response = new HashMap<>();

        response.put("status", "SUCCESS");
        response.put("count", users.size());
        response.put("users", users);

        return ResponseEntity.ok(response);
    }

}