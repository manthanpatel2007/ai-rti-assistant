package com.hacthon.ai_rti_assistant.controller;

import com.hacthon.ai_rti_assistant.entity.RtiRequest;
import com.hacthon.ai_rti_assistant.service.RtiPdfService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rti")
public class RtiPdfController {

    private final RtiPdfService rtiPdfService;

    public RtiPdfController(
            RtiPdfService rtiPdfService
    ) {
        this.rtiPdfService = rtiPdfService;
    }

    @PostMapping("/{rtiRequestId}/generate-pdf")
    public ResponseEntity<?> generatePdf(
            @PathVariable Long rtiRequestId
    ) {

        try {

            RtiRequest request =
                    rtiPdfService.generatePdf(
                            rtiRequestId
                    );

            return ResponseEntity.ok(request);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{rtiRequestId}/preview")
    public ResponseEntity<?> previewPdf(
            @PathVariable Long rtiRequestId
    ) {

        try {

            byte[] pdfBytes =
                    rtiPdfService.getPdfBytes(rtiRequestId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"RTI-" +
                                    rtiRequestId +
                                    ".pdf\""
                    )
                    .body(new ByteArrayResource(pdfBytes));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{rtiRequestId}/download")
    public ResponseEntity<?> downloadPdf(
            @PathVariable Long rtiRequestId
    ) {

        try {

            byte[] pdfBytes =
                    rtiPdfService.getPdfBytes(rtiRequestId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"RTI-" +
                                    rtiRequestId +
                                    ".pdf\""
                    )
                    .body(new ByteArrayResource(pdfBytes));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}