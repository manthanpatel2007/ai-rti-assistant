package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.dto.Request.LoginRequest;
import com.hacthon.ai_rti_assistant.dto.Request.RegisterRequest;
import com.hacthon.ai_rti_assistant.dto.Response.LoginResponse;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    void verifyOtp(String email, String otp);

    void resendOtp(String email);

    void forgotPassword(String email);

    void resetPassword(
            String email,
            String otp,
            String newPassword
    );

    LoginResponse refreshAccessToken(String refreshToken);

    void logout(String refreshToken);
}