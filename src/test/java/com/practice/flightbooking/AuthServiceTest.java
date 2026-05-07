package com.practice.flightbooking;

import com.practice.flightbooking.dto.request.RegisterRequestDTO;
import com.practice.flightbooking.dto.response.AuthResponseDTO;
import com.practice.flightbooking.entity.User;
import com.practice.flightbooking.enums.Role;
import com.practice.flightbooking.repo.UserRepo;
import com.practice.flightbooking.security.JwtUtil;
import com.practice.flightbooking.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDTO registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequestDTO();
        registerRequest.setName("John Doe");
        registerRequest.setEmail("john@gmail.com");
        registerRequest.setPassword("123456");
        registerRequest.setPhone("9876543210");
    }

    // ── Test 1: Register successfully ─────────────────────────
    @Test
    void register_ShouldReturnToken_WhenEmailNotExists() {
        // ARRANGE
        when(userRepo.existsByEmail("john@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtUtil.generateToken(any(), any())).thenReturn("mockToken");

        // ACT
        AuthResponseDTO result = authService.register(registerRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals("mockToken", result.getToken());
        assertEquals("john@gmail.com", result.getEmail());
        assertEquals("CUSTOMER", result.getRole());
        verify(userRepo, times(1)).save(any(User.class));
    }

    // ── Test 2: Register fails if email exists ─────────────────
    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        // ARRANGE
        when(userRepo.existsByEmail("john@gmail.com")).thenReturn(true);

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });
        verify(userRepo, never()).save(any(User.class));
    }
}