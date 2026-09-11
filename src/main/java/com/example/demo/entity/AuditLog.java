package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "AUTHJSB_AUDIT_LOG")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    private String username;
    private String displayName;
    private String email;

    private String action;

    @Column(name = "RESOURCE_NAME")
    private String resourceName;

    private String result;

    private LocalDateTime eventTime;

    @Column(length = 4000)
    private String details;


}