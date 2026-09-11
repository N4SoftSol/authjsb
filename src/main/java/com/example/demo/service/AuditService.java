package com.example.demo.service;

import com.example.demo.entity.AuditLog;
import com.example.demo.repository.AuditLogRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(
            AuditLogRepository auditLogRepository) {

        this.auditLogRepository =
                auditLogRepository;
    }

    public void log(
            String username,
            String displayName,
            String email,
            String action,
            String resourceName,
            String result,
            String details) {

        AuditLog auditLog =
                new AuditLog();

        auditLog.setUsername(
                username);
        auditLog.setDisplayName(
                displayName);
        auditLog.setEmail(
                email);
        auditLog.setAction(
                action);
        auditLog.setResourceName(
                resourceName);
        auditLog.setResult(
                result);
        auditLog.setEventTime(
                LocalDateTime.now());
        auditLog.setDetails(
                details);

        auditLogRepository.save(auditLog);
    }
}