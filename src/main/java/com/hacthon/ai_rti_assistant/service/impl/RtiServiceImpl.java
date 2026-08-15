package com.hacthon.ai_rti_assistant.service.impl;

import com.hacthon.ai_rti_assistant.dto.Request.RtiRequestCreateRequest;
import com.hacthon.ai_rti_assistant.entity.PIO;
import com.hacthon.ai_rti_assistant.entity.RtiRequest;
import com.hacthon.ai_rti_assistant.entity.RtiStatus;
import com.hacthon.ai_rti_assistant.entity.User;
import com.hacthon.ai_rti_assistant.exception.BadRequestException;
import com.hacthon.ai_rti_assistant.repository.PIORepository;
import com.hacthon.ai_rti_assistant.repository.RtiRequestRepository;
import com.hacthon.ai_rti_assistant.repository.UserRepository;
import com.hacthon.ai_rti_assistant.service.AiService;
import com.hacthon.ai_rti_assistant.service.EmailService;
import com.hacthon.ai_rti_assistant.service.RtiService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RtiServiceImpl implements RtiService {

    private final RtiRequestRepository rtiRequestRepository;
    private final UserRepository userRepository;
    private final AiService aiService;
    private final EmailService emailService;
    private final PIORepository pioRepository;

    public RtiServiceImpl(
            RtiRequestRepository rtiRequestRepository,
            UserRepository userRepository,
            AiService aiService,
            EmailService emailService,
            PIORepository pioRepository
    ) {
        this.rtiRequestRepository = rtiRequestRepository;
        this.userRepository = userRepository;
        this.aiService = aiService;
        this.emailService = emailService;
        this.pioRepository = pioRepository;
    }

    // =====================================================
    // CREATE RTI
    // =====================================================

    @Override
    public RtiRequest createRti(
            RtiRequestCreateRequest request,
            String userEmail
    ) {

        // =========================
        // 1. Validate both checkboxes
        // =========================

        if (!request.isInformationConfirmed()
                || !request.isSubmissionConsent()) {

            throw new BadRequestException(
                    "Please accept both declarations before submitting the RTI."
            );
        }

        // =========================
        // 2. Find logged-in user
        // =========================

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new BadRequestException(
                                "User not found"
                        )
                );

        // =========================
        // 3. Generate RTI using AI
        // =========================

        String generatedContent = aiService.generateRti(
                request.getIssueDescription(),
                request.getLocation(),
                request.getDepartment()
        );

        // =========================
        // 4. Create RTI request
        // =========================

        RtiRequest rtiRequest = new RtiRequest();

        rtiRequest.setUser(user);

        rtiRequest.setIssueDescription(
                request.getIssueDescription()
        );

        rtiRequest.setLocation(
                request.getLocation()
        );

        rtiRequest.setDepartment(
                request.getDepartment()
        );

        rtiRequest.setGeneratedContent(
                generatedContent
        );

        rtiRequest.setStatus(
                RtiStatus.GENERATED
        );

        rtiRequest.setCreatedAt(
                LocalDateTime.now()
        );

        // =========================
        // 5. Save consent
        // =========================

        rtiRequest.setInformationConfirmed(
                request.isInformationConfirmed()
        );

        rtiRequest.setSubmissionConsent(
                request.isSubmissionConsent()
        );

        rtiRequest.setConsentAcceptedAt(
                LocalDateTime.now()
        );

        // =========================
        // 6. Save RTI
        // =========================

        return rtiRequestRepository.save(rtiRequest);
    }

    // =====================================================
    // GET MY RTIs
    // =====================================================

    @Override
    public List<RtiRequest> getMyRtis(
            String userEmail
    ) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new BadRequestException(
                                "User not found"
                        )
                );

        return rtiRequestRepository
                .findByUserIdOrderByCreatedAtDesc(
                        user.getId()
                );
    }

    // =====================================================
    // SEND RTI
    // TO  = PIO
    // CC  = Registered User
    // PDF = Generated RTI PDF
    // =====================================================

    @Override
    public void sendRti(
            Long rtiRequestId,
            String userEmail
    ) {

        // =========================
        // 1. Find RTI
        // =========================

        RtiRequest rtiRequest =
                rtiRequestRepository.findById(rtiRequestId)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "RTI request not found"
                                )
                        );

        // =========================
        // 2. Verify RTI belongs to user
        // =========================

        if (!rtiRequest.getUser()
                .getEmail()
                .equals(userEmail)) {

            throw new BadRequestException(
                    "You are not authorized to send this RTI"
            );
        }

        // =========================
        // 3. Verify consent
        // =========================

        if (!rtiRequest.isInformationConfirmed()
                || !rtiRequest.isSubmissionConsent()) {

            throw new BadRequestException(
                    "Both declarations must be accepted before sending the RTI"
            );
        }

        // =========================
        // 4. Verify PDF exists
        // =========================

        if (rtiRequest.getPdfPath() == null
                || rtiRequest.getPdfPath().isBlank()) {

            throw new BadRequestException(
                    "Please generate the RTI PDF before sending"
            );
        }

        // =========================
        // 5. Find PIO
        // =========================

        PIO pio = pioRepository
                .findFirstByDepartmentName(
                        rtiRequest.getDepartment()
                )
                .orElseThrow(() ->
                        new BadRequestException(
                                "PIO not found for selected department"
                        )
                );

        // =========================
        // 6. Send email through Brevo
        // =========================

        try {

            emailService.sendRtiEmail(
                    pio.getEmail(),
                    rtiRequest.getGeneratedContent(),
                    rtiRequest.getUser().getEmail(),
                    rtiRequest.getPdfPath()
            );

        } catch (Exception e) {

            throw new BadRequestException(
                    "Failed to send RTI email"
            );
        }

        // =========================
        // 7. Update status
        // =========================

        rtiRequest.setStatus(
                RtiStatus.SENT
        );

        rtiRequestRepository.save(rtiRequest);
    }
}