package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.entity.RtiAttachment;
import com.hacthon.ai_rti_assistant.entity.RtiRequest;
import com.hacthon.ai_rti_assistant.exception.BadRequestException;
import com.hacthon.ai_rti_assistant.repository.RtiAttachmentRepository;
import com.hacthon.ai_rti_assistant.repository.RtiRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class RtiAttachmentService {

    private final RtiAttachmentRepository attachmentRepository;
    private final RtiRequestRepository rtiRequestRepository;

    private final Path uploadDirectory =
            Paths.get("uploads/rti");

    public RtiAttachmentService(
            RtiAttachmentRepository attachmentRepository,
            RtiRequestRepository rtiRequestRepository
    ) {
        this.attachmentRepository = attachmentRepository;
        this.rtiRequestRepository = rtiRequestRepository;
    }
    public RtiAttachment getAttachment(Long attachmentId) {

        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() ->
                        new BadRequestException("Attachment not found")
                );
    }

    public byte[] getFileBytes(Long attachmentId) throws IOException {

        RtiAttachment attachment = getAttachment(attachmentId);

        Path path = Paths.get(attachment.getFilePath());

        if (!Files.exists(path)) {
            throw new BadRequestException("File not found");
        }

        return Files.readAllBytes(path);
    }

    public RtiAttachment upload(
            Long rtiRequestId,
            MultipartFile file
    ) throws IOException {

        RtiRequest rtiRequest = rtiRequestRepository
                .findById(rtiRequestId)
                .orElseThrow(() ->
                        new BadRequestException("RTI request not found")
                );

        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BadRequestException(
                    "File size must be less than 5 MB"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")
                        && !contentType.equals("application/pdf"))) {

            throw new org.apache.coyote.BadRequestException(
                    "Only JPG, PNG and PDF files are allowed"
            );
        }

        Files.createDirectories(uploadDirectory);

        String extension = getExtension(file.getOriginalFilename());

        String storedFileName =
                UUID.randomUUID() + extension;

        Path filePath =
                uploadDirectory.resolve(storedFileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        RtiAttachment attachment = new RtiAttachment();

        attachment.setOriginalFileName(
                file.getOriginalFilename()
        );

        attachment.setStoredFileName(storedFileName);

        attachment.setFilePath(
                filePath.toString()
        );

        attachment.setContentType(contentType);

        attachment.setFileSize(file.getSize());

        attachment.setRtiRequest(rtiRequest);

        return attachmentRepository.save(attachment);
    }

    private String getExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {
            return "";
        }

        return fileName.substring(
                fileName.lastIndexOf(".")
        );
    }
}