package com.example.demo.service;

import com.example.demo.dto.RefreshTokenStatus;
import com.example.demo.repository.OracleRefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OracleRefreshTokenService {

    private final OracleRefreshTokenRepository repository;

    public OracleRefreshTokenService(
            OracleRefreshTokenRepository repository) {

        this.repository = repository;
    }

    public List<RefreshTokenStatus> getRefreshTokenStatus() {

        return repository.getRefreshTokenStatus();
    }
}