package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.entity.OtpPurpose;

public interface OtpService {

    void sendOtp(String email);

    boolean verifyOtp(
            String email,
            String otp
    );

    void resendOtp(String email);

    void sendPasswordResetOtp(String email);

    boolean verifyPasswordResetOtp(
            String email,
            String otp
    );
}