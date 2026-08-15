package com.hacthon.ai_rti_assistant.service;

public interface OtpService {

    void sendOtp(String email);

    boolean verifyOtp(String email, String otp);

    void resendOtp(String email);
}