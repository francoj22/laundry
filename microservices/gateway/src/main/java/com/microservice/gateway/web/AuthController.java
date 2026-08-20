package com.microservice.gateway.web;

import com.microservice.gateway.config.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/auth/token")
    public ResponseEntity<Map<String, String>> issueToken(
            @RequestParam(defaultValue = "demo-user") String userId,
            @RequestParam(defaultValue = "user") String role
    ) {
        String token = jwtService.issueToken(userId, role);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
