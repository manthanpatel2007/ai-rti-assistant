package com.hacthon.ai_rti_assistant.controller;

import com.hacthon.ai_rti_assistant.dto.Request.LoginRequest;
import com.hacthon.ai_rti_assistant.dto.Request.RegisterRequest;
import com.hacthon.ai_rti_assistant.dto.Response.LoginResponse;
import com.hacthon.ai_rti_assistant.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {

        authService.register(request);

        return ResponseEntity.ok(
                "Registration successful. OTP sent to your email."
        );
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }




}