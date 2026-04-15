package com.cts.auth_service.application.controller;

import com.cts.dto.request.LoginDto;
import com.cts.dto.response.LoginResponseDto;
import com.cts.auth_service.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        log.info("Login request for email: {}", loginDto.getEmail());
        LoginResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }
}
