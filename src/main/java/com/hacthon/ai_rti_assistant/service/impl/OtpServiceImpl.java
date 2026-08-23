package com.hacthon.ai_rti_assistant.service.impl;

import com.hacthon.ai_rti_assistant.entity.EmailOtp;
import com.hacthon.ai_rti_assistant.entity.OtpPurpose;
import com.hacthon.ai_rti_assistant.exception.BadRequestException;
import com.hacthon.ai_rti_assistant.repository.EmailOtpRepository;
import com.hacthon.ai_rti_assistant.service.EmailService;
import com.hacthon.ai_rti_assistant.service.OtpService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private final EmailOtpRepository emailOtpRepository;
    private final EmailService emailService;

    public OtpServiceImpl(
            EmailOtpRepository emailOtpRepository,
            EmailService emailService
    ) {
        this.emailOtpRepository = emailOtpRepository;
        this.emailService = emailService;
    }

    @Override
    public void sendOtp(String email) {

        String otp = generateOtp();

        EmailOtp emailOtp = new EmailOtp();

        emailOtp.setEmail(email);
        emailOtp.setOtp(otp);
        emailOtp.setCreatedAt(LocalDateTime.now());
        emailOtp.setExpiresAt(
                LocalDateTime.now().plusMinutes(5)
        );
        emailOtp.setVerified(false);

        emailOtp.setPurpose(
                OtpPurpose.EMAIL_VERIFICATION
        );

        emailOtpRepository.save(emailOtp);

        emailService.sendOtpEmail(
                email,
                otp
        );
    }

    @Override
    public boolean verifyOtp(
            String email,
            String otp
    ) {

        return verifyOtpByPurpose(
                email,
                otp,
                OtpPurpose.EMAIL_VERIFICATION
        );
    }

    @Override
    public void resendOtp(String email) {

        sendOtp(email);
    }

    @Override
    public void sendPasswordResetOtp(
            String email
    ) {

        String otp = generateOtp();

        EmailOtp emailOtp = new EmailOtp();

        emailOtp.setEmail(email);
        emailOtp.setOtp(otp);

        emailOtp.setCreatedAt(
                LocalDateTime.now()
        );

        emailOtp.setExpiresAt(
                LocalDateTime.now().plusMinutes(5)
        );

        emailOtp.setVerified(false);

        emailOtp.setPurpose(
                OtpPurpose.PASSWORD_RESET
        );

        emailOtpRepository.save(emailOtp);

        emailService.sendPasswordResetOtpEmail(
                email,
                otp
        );
    }

    @Override
    public boolean verifyPasswordResetOtp(
            String email,
            String otp
    ) {

        return verifyOtpByPurpose(
                email,
                otp,
                OtpPurpose.PASSWORD_RESET
        );
    }

    private boolean verifyOtpByPurpose(
            String email,
            String otp,
            OtpPurpose purpose
    ) {

        EmailOtp emailOtp =
                emailOtpRepository
                        .findTopByEmailAndPurposeOrderByCreatedAtDesc(
                                email,
                                purpose
                        )
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "OTP not found"
                                )
                        );

        if (emailOtp.isVerified()) {

            throw new BadRequestException(
                    "OTP already verified"
            );
        }

        if (emailOtp.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new BadRequestException(
                    "OTP expired"
            );
        }

        if (!emailOtp.getOtp().equals(otp)) {

            throw new BadRequestException(
                    "Invalid OTP"
            );
        }

        emailOtp.setVerified(true);

        emailOtpRepository.save(emailOtp);

        return true;
    }

    private String generateOtp() {

        return String.format(
                "%06d",
                new Random().nextInt(1_000_000)
        );
    }
}