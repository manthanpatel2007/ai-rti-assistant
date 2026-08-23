package com.hacthon.ai_rti_assistant.service.impl;

import com.hacthon.ai_rti_assistant.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final RestClient restClient;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    public EmailServiceImpl() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.brevo.com")
                .build();
    }



    @Override
    public void sendRtiEmail(
            String toEmail,
            String rtiContent,
            String userEmail,
            String pdfPath
    ) throws IOException {

        // Read generated PDF
        byte[] pdfBytes = Files.readAllBytes(
                Path.of(pdfPath)
        );

        // Convert PDF to Base64
        String base64Pdf = Base64.getEncoder()
                .encodeToString(pdfBytes);

        Map<String, Object> requestBody = Map.of(



                "sender", Map.of(
                        "name", senderName,
                        "email", senderEmail
                ),


                "to", List.of(
                        Map.of(
                                "email", toEmail
                        )
                ),



                "cc", List.of(
                        Map.of(
                                "email", userEmail
                        )
                ),



                "subject",
                "RTI Application under the Right to Information Act, 2005",



                "textContent",
                """
                To,
                Public Information Officer,

                Please find attached the RTI application
                submitted through AI RTI Assistant.

                ------------------------------
                RTI APPLICATION
                ------------------------------

                %s

                ------------------------------

                The generated RTI application is attached
                as a PDF for your reference.

                Regards,
                AI RTI Assistant
                """.formatted(rtiContent),



                "attachment", List.of(
                        Map.of(
                                "content", base64Pdf,
                                "name", "RTI-Application.pdf"
                        )
                )
        );



        restClient.post()
                .uri("/v3/smtp/email")
                .header(
                        "api-key",
                        brevoApiKey
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }



    @Override
    public void sendOtpEmail(
            String toEmail,
            String otp
    ) {

        Map<String, Object> requestBody = Map.of(

                "sender", Map.of(

                        "email", "patelmanthan23507@gmail.com"
                ),

                "to", List.of(
                        Map.of(
                                "email", toEmail
                        )
                ),

                "subject",
                "Your AI RTI Assistant OTP",

                "textContent",
                """
                Dear User,

                Your OTP for AI RTI Assistant is:

                %s

                This OTP is valid for 5 minutes.

                Please do not share this OTP with anyone.

                Regards,
                AI RTI Assistant
                """.formatted(otp)
        );

        restClient.post()
                .uri("/v3/smtp/email")
                .header(
                        "api-key",
                        "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"
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void sendPasswordResetOtpEmail(
            String toEmail,
            String otp
    ) {

        Map<String, Object> requestBody = Map.of(

                "sender", Map.of(

                        "email", "patelmanthan23507@gmail.com"
                ),

                "to", List.of(
                        Map.of(
                                "email", toEmail
                        )
                ),

                "subject",
                "Reset Your AI RTI Assistant Password",

                "textContent",
                """
                Dear User,
    
                We received a request to reset your AI RTI Assistant password.
    
                Your password reset OTP is:
    
                %s
    
                This OTP is valid for 5 minutes.
    
                If you did not request a password reset, please ignore this email.
    
                Regards,
                AI RTI Assistant
                """.formatted(otp)
        );

        restClient.post()
                .uri("/v3/smtp/email")
                .header(
                        "api-key",
                        "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"
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }
}