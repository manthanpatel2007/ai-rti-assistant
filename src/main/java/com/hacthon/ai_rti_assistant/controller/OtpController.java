package com.hacthon.ai_rti_assistant.controller;

import com.hacthon.ai_rti_assistant.dto.Request.VerifyOtpRequest;
import com.hacthon.ai_rti_assistant.service.OtpService;
import com.hacthon.ai_rti_assistant.service.RateLimiterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class OtpController {

    private final OtpService otpService;
    private final RateLimiterService rateLimiterService ;

    public OtpController(OtpService otpService, RateLimiterService rateLimiterService) {
        this.otpService = otpService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {

        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok(
                Map.of("message", "Email verified successfully")
        );
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {

        String key = "resend-otp:" + email;

        // 3 requests / 10 minutes
        if (!rateLimiterService.isAllowed(key, 3, 10 * 60 * 1000)) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many attempts. Try again later.");
        }

        // Actual OTP resend logic
        otpService.resendOtp(email);

        return ResponseEntity.ok(
                Map.of("message", "OTP sent successfully")
        );
    }
}

