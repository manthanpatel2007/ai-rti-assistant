package com.hacthon.ai_rti_assistant.repository;

import com.hacthon.ai_rti_assistant.entity.EmailOtp;
import com.hacthon.ai_rti_assistant.entity.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailOtpRepository
        extends JpaRepository<EmailOtp, Long> {

    Optional<EmailOtp> findTopByEmailAndPurposeOrderByCreatedAtDesc(
            String email,
            OtpPurpose purpose
    );
}