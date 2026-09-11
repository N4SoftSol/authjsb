package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "AUTHJSB_REFRESH_TOKEN")
public class RefreshToken {

    @Column(name = "TOKEN_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(length = 4000)
    private String tokenValue;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean revoked;


}