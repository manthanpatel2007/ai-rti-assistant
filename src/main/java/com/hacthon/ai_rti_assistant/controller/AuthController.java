package com.hacthon.ai_rti_assistant.controller;

import com.hacthon.ai_rti_assistant.dto.Request.ForgotPasswordRequest;
import com.hacthon.ai_rti_assistant.dto.Request.LoginRequest;
import com.hacthon.ai_rti_assistant.dto.Request.RegisterRequest;
import com.hacthon.ai_rti_assistant.dto.Request.ResetPasswordRequest;
import com.hacthon.ai_rti_assistant.dto.Response.LoginResponse;
import com.hacthon.ai_rti_assistant.service.AuthService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody RegisterRequest request
    ) {

        authService.register(request);

        return ResponseEntity.ok(
                "Registration successful. OTP sent to your email."
        );
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        authService.forgotPassword(
                request.getEmail()
        );

        return ResponseEntity.ok(
                "Password reset OTP sent to your email."
        );
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        authService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                "Password reset successfully."
        );
    }



    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshAccessToken(
            @RequestParam String refreshToken
    ) {

        return ResponseEntity.ok(
                authService.refreshAccessToken(
                        refreshToken
                )
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestParam String refreshToken
    ) {

        authService.logout(
                refreshToken
        );

        return ResponseEntity.ok(
                "Logged out successfully."
        );
    }
}