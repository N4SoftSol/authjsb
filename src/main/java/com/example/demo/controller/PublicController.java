package com.example.demo.controller;


import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@Slf4j
public class PublicController {
    private final Environment environment;

    public PublicController(
            Environment environment) {

        this.environment = environment;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>>
    getPublicInfo() {
        log.info("GET /api/public/info reached - Public request received");
        Map<String, Object> info =
                new HashMap<>();

        info.put("status", "UP");
        info.put("application", "auth-server");
        info.put("Git Update", "1.8");
        info.put(
                "profiles",
                environment.getActiveProfiles());

        return ResponseEntity.ok(info);
    }
}