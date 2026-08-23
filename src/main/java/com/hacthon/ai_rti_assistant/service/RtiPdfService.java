package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.entity.RtiRequest;
import com.hacthon.ai_rti_assistant.entity.RtiStatus;
import com.hacthon.ai_rti_assistant.entity.User;
import com.hacthon.ai_rti_assistant.exception.ResourceNotFoundException;
import com.hacthon.ai_rti_assistant.repository.RtiRequestRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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


    // =========================================================
    // GENERATE PDF
    // =========================================================

    public RtiRequest generatePdf(Long rtiRequestId) throws Exception {

        RtiRequest rtiRequest =
                rtiRequestRepository.findById(rtiRequestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "RTI request not found"
                                )
                        );


        User user = rtiRequest.getUser();

        if (user == null) {
            throw new ResourceNotFoundException(
                    "Applicant information not found"
            );
        }


        // =====================================================
        // Validate applicant details
        // =====================================================

        if (user.getName() == null ||
                user.getName().isBlank()) {

            throw new ResourceNotFoundException(
                    "Applicant name is missing"
            );
        }

        if (user.getEmail() == null ||
                user.getEmail().isBlank()) {

            throw new ResourceNotFoundException(
                    "Applicant email is missing"
            );
        }

        if (user.getAddress() == null ||
                user.getAddress().isBlank()) {

            throw new ResourceNotFoundException(
                    "Applicant postal address is missing. Please update your profile."
            );
        }

        if (user.getPhone() == null ||
                user.getPhone().isBlank()) {

            throw new ResourceNotFoundException(
                    "Applicant phone number is missing. Please update your profile."
            );
        }


        // =====================================================
        // Create directory
        // =====================================================

        Files.createDirectories(pdfDirectory);


        String fileName =
                "RTI-" + rtiRequest.getId() + ".pdf";

        Path pdfPath =
                pdfDirectory.resolve(fileName);


        // =====================================================
        // Create PDF
        // =====================================================

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                new FileOutputStream(pdfPath.toFile())
        );

        document.open();


        // =====================================================
        // Fonts
        // =====================================================

        Font titleFont = new Font(
                Font.HELVETICA,
                14,
                Font.BOLD
        );

        Font headingFont = new Font(
                Font.HELVETICA,
                11,
                Font.BOLD
        );

        Font normalFont = new Font(
                Font.HELVETICA,
                10,
                Font.NORMAL
        );


        // =====================================================
        // TITLE
        // =====================================================

        Paragraph title =
                new Paragraph(
                        "RIGHT TO INFORMATION ACT, 2005",
                        titleFont
                );

        title.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(title);

        document.add(
                new Paragraph(" ")
        );


        // =====================================================
        // APPLICANT DETAILS
        // =====================================================

        document.add(
                new Paragraph(
                        "1. Full Name of the Applicant: "
                                + user.getName(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "2. Postal Address: "
                                + user.getAddress(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "3. Contact Details: "
                                + user.getPhone()
                                + " / "
                                + user.getEmail(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // =====================================================
        // SUBJECT
        // =====================================================

        document.add(
                new Paragraph(
                        "Subject: "
                                + rtiRequest.getDepartment(),
                        headingFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // =====================================================
        // LOCATION
        // =====================================================

        document.add(
                new Paragraph(
                        "Location: "
                                + rtiRequest.getLocation(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // =====================================================
        // APPLICATION DETAILS
        // =====================================================

        document.add(
                new Paragraph(
                        "Application Details:",
                        headingFont
                )
        );

        document.add(
                new Paragraph(
                        rtiRequest.getIssueDescription(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // =====================================================
        // AI GENERATED RTI
        // =====================================================

        document.add(
                new Paragraph(
                        rtiRequest.getGeneratedContent(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // =====================================================
        // DECLARATION
        // =====================================================

        document.add(
                new Paragraph(
                        "I state that I am a citizen of India "
                                + "and the requested information does not "
                                + "fall within the exemptions specified "
                                + "under Sections 8 and 9 of the RTI Act, 2005.",
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // =====================================================
        // PLACE
        // =====================================================

        document.add(
                new Paragraph(
                        "Place: "
                                + rtiRequest.getLocation(),
                        normalFont
                )
        );


        // =====================================================
        // DATE
        // =====================================================

        String currentDate =
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );

        document.add(
                new Paragraph(
                        "Date: " + currentDate,
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // =====================================================
        // SIGNATURE
        // =====================================================

        document.add(
                new Paragraph(
                        "Signature of the Applicant: _____________________",
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        user.getName(),
                        normalFont
                )
        );


        // =====================================================
        // CLOSE PDF
        // =====================================================

        document.close();


        // =====================================================
        // SAVE PATH + STATUS
        // =====================================================

        rtiRequest.setPdfPath(
                pdfPath.toString()
        );

        rtiRequest.setStatus(
                RtiStatus.PDF_GENERATED
        );

        return rtiRequestRepository.save(rtiRequest);
    }


    // =========================================================
    // GET PDF BYTES
    // =========================================================

    public byte[] getPdfBytes(
            Long rtiRequestId
    ) throws Exception {

        RtiRequest rtiRequest =
                rtiRequestRepository.findById(rtiRequestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "RTI request not found"
                                )
                        );


        if (rtiRequest.getPdfPath() == null ||
                rtiRequest.getPdfPath().isBlank()) {

            throw new ResourceNotFoundException(
                    "RTI PDF has not been generated yet"
            );
        }


        Path pdfPath =
                Paths.get(
                        rtiRequest.getPdfPath()
                );


        if (!Files.exists(pdfPath)) {

            throw new ResourceNotFoundException(
                    "RTI PDF file not found"
            );
        }


        return Files.readAllBytes(pdfPath);
    }
}