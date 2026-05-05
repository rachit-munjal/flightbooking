package com.practice.flightbooking.controller;

import com.practice.flightbooking.dto.request.LoginRequestDTO;
import com.practice.flightbooking.dto.request.RegisterRequestDTO;
import com.practice.flightbooking.dto.response.AuthResponseDTO;
import com.practice.flightbooking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<AuthResponseDTO> createAdmin(
            @Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.createAdmin(request));
    }
}