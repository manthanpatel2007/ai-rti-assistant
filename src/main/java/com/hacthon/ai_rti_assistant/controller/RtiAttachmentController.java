package com.hacthon.ai_rti_assistant.controller;

import com.hacthon.ai_rti_assistant.entity.RtiAttachment;
import com.hacthon.ai_rti_assistant.service.RtiAttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rti/attachments")
public class RtiAttachmentController {

    private final RtiAttachmentService attachmentService;

    public RtiAttachmentController(
            RtiAttachmentService attachmentService
    ) {
        this.attachmentService = attachmentService;
    }

    @PostMapping(
            value = "/{rtiRequestId}",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> uploadAttachment(
            @PathVariable Long rtiRequestId,
            @RequestParam("file") MultipartFile file
    ) {

        try {

            RtiAttachment attachment =
                    attachmentService.upload(
                            rtiRequestId,
                            file
                    );

            return ResponseEntity.ok(attachment);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{attachmentId}/view")
    public ResponseEntity<?> viewAttachment(
            @PathVariable Long attachmentId
    ) {

        try {

            RtiAttachment attachment =
                    attachmentService.getAttachment(attachmentId);

            byte[] fileBytes =
                    attachmentService.getFileBytes(attachmentId);

            MediaType mediaType =
                    MediaType.parseMediaType(
                            attachment.getContentType()
                    );

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" +
                                    attachment.getOriginalFileName() +
                                    "\""
                    )
                    .body(new ByteArrayResource(fileBytes));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<?> downloadAttachment(
            @PathVariable Long attachmentId
    ) {

        try {

            RtiAttachment attachment =
                    attachmentService.getAttachment(attachmentId);

            byte[] fileBytes =
                    attachmentService.getFileBytes(attachmentId);

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    attachment.getContentType()
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    attachment.getOriginalFileName() +
                                    "\""
                    )
                    .body(new ByteArrayResource(fileBytes));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}