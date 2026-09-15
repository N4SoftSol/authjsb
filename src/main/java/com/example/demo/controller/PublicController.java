package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Value;
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
        private final String appName;
        private final String appVersion;

        public PublicController(
                Environment environment,
                @Value("${info.application.name}") String appName,
                @Value("${info.app.version}") String appVersion

        ) {
            this.environment = environment;
            this.appName = appName;
            this.appVersion = appVersion;
        }

        @GetMapping("/info")
        public ResponseEntity<Map<String, Object>> getPublicInfo() {
            log.info("GET /api/public/info reached - Public request received");

            Map<String, Object> info = new HashMap<>();
            info.put("status", "UP");
            info.put("application", appName);
            info.put("version", appVersion);
            info.put("profiles", environment.getActiveProfiles());

            return ResponseEntity.ok(info);
        }
}