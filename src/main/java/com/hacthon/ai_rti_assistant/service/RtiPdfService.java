package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.entity.RtiRequest;
import com.hacthon.ai_rti_assistant.entity.RtiStatus;
import com.hacthon.ai_rti_assistant.exception.ResourceNotFoundException;
import com.hacthon.ai_rti_assistant.repository.RtiRequestRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class RtiPdfService {

    private final RtiRequestRepository rtiRequestRepository;

    private final Path pdfDirectory =
            Paths.get("uploads/rti/pdfs");

    public RtiPdfService(
            RtiRequestRepository rtiRequestRepository
    ) {
        this.rtiRequestRepository = rtiRequestRepository;
    }

    public RtiRequest generatePdf(Long rtiRequestId) throws Exception {

        RtiRequest rtiRequest =
                rtiRequestRepository.findById(rtiRequestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "RTI request not found"
                                )
                        );

        Files.createDirectories(pdfDirectory);

        String fileName =
                "RTI-" + rtiRequest.getId() + ".pdf";

        Path pdfPath =
                pdfDirectory.resolve(fileName);

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                new FileOutputStream(pdfPath.toFile())
        );

        document.open();

        document.add(
                new Paragraph("RIGHT TO INFORMATION ACT, 2005")
        );

        document.add(new Paragraph(" "));

        document.add(
                new Paragraph(
                        "Subject: " + rtiRequest.getDepartment()
                )
        );

        document.add(new Paragraph(" "));

        document.add(
                new Paragraph(
                        "Location: " +
                                rtiRequest.getLocation()
                )
        );

        document.add(new Paragraph(" "));

        document.add(
                new Paragraph(
                        "Application Details:"
                )
        );

        document.add(
                new Paragraph(
                        rtiRequest.getIssueDescription()
                )
        );

        document.add(new Paragraph(" "));

        document.add(
                new Paragraph(
                        rtiRequest.getGeneratedContent()
                )
        );

        document.add(new Paragraph(" "));

        document.add(
                new Paragraph(
                        "Applicant: " +
                                rtiRequest.getUser().getEmail()
                )
        );

        document.close();

        rtiRequest.setPdfPath(
                pdfPath.toString()
        );

        rtiRequest.setStatus(
                RtiStatus.PDF_GENERATED
        );

        return rtiRequestRepository.save(rtiRequest);
    }

    public byte[] getPdfBytes(Long rtiRequestId) throws Exception {

        RtiRequest rtiRequest =
                rtiRequestRepository.findById(rtiRequestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "RTI request not found"
                                )
                        );

        if (rtiRequest.getPdfPath() == null) {
            throw new ResourceNotFoundException(
                    "RTI PDF has not been generated yet"
            );
        }

        Path pdfPath =
                Paths.get(rtiRequest.getPdfPath());

        if (!Files.exists(pdfPath)) {
            throw new ResourceNotFoundException(
                    "RTI PDF file not found"
            );
        }

        return Files.readAllBytes(pdfPath);
    }
}