package com.hacthon.ai_rti_assistant.controller;

import com.hacthon.ai_rti_assistant.dto.Request.RtiRequestCreateRequest;
import com.hacthon.ai_rti_assistant.entity.RtiRequest;
import com.hacthon.ai_rti_assistant.exception.BadRequestException;
import com.hacthon.ai_rti_assistant.service.RtiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rti")
public class RtiController {

    private final RtiService rtiService;

    public RtiController(RtiService rtiService) {
        this.rtiService = rtiService;
    }

    @PostMapping
    public ResponseEntity<RtiRequest> createRti(
            @Valid @RequestBody RtiRequestCreateRequest request,
            Authentication authentication
    ) {

        String userEmail = authentication.getName();
        if (!request.isInformationConfirmed()
                || !request.isSubmissionConsent()) {

            throw new BadRequestException(
                    "Please accept both declarations before submitting the RTI."
            );
        }

        return ResponseEntity.ok(
                rtiService.createRti(request, userEmail)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<RtiRequest>> getMyRtis(
            Authentication authentication
    ) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                rtiService.getMyRtis(userEmail)
        );
    }

    @PostMapping("/{rtiRequestId}/send")
    public ResponseEntity<?> sendRti(
            @PathVariable Long rtiRequestId,
            Authentication authentication
    ) {

        rtiService.sendRti(
                rtiRequestId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                "RTI sent successfully"
        );
    }
}