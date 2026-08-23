package com.hacthon.ai_rti_assistant.service;

import java.io.IOException;

public interface EmailService {

    void sendRtiEmail(
            String toEmail,
            String rtiContent,
            String userEmail,
            String pdfPath
    ) throws IOException;

    void sendOtpEmail(
            String toEmail,
            String otp
    );

    void sendPasswordResetOtpEmail(
            String toEmail,
            String otp
    );
}