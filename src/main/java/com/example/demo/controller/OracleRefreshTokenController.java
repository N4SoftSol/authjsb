package com.example.demo.controller;

import com.example.demo.dto.RefreshTokenStatus;
import com.example.demo.service.OracleRefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/oracle")
public class OracleRefreshTokenController {

    private final OracleRefreshTokenService service;

    public OracleRefreshTokenController(
            OracleRefreshTokenService service) {

        this.service = service;
    }

//
}