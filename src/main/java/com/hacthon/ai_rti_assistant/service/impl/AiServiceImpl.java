package com.hacthon.ai_rti_assistant.service.impl;

import com.hacthon.ai_rti_assistant.exception.ResourceNotFoundException;
import com.hacthon.ai_rti_assistant.service.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AiServiceImpl implements AiService {

    private final RestClient restClient;
    private final String apiKey;

    public AiServiceImpl(
            @Value("${gemini.api-key}") String apiKey
    ) {
        this.apiKey = apiKey;

        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    @Override
    public String generateRti(
            String issueDescription,
            String location,
            String department
    ) {

        String prompt = """
        You are an expert Indian RTI application writer.

        Create a formal and legally appropriate RTI application
        based on the user's information.

        Issue:
        %s

        Location:
        %s

        Department:
        %s

        The application should:
        - Have a clear subject.
        - Address the appropriate Public Information Officer.
        - Ask specific information-related questions.
        - Be professional and concise.
        - Not invent facts that the user did not provide.
        - Follow the general structure of an Indian RTI application.
        - Do not include applicant personal details.
        - Do not include placeholders for name, address, phone, email, date, or signature.
        - Do not write [Your Name], [Your Address], [Your Phone Number],
          [Insert Date], or similar placeholders.

        Return only the RTI application text.
        """.formatted(
                issueDescription,
                location,
                department
        );

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        Map response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-3.6-flash:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        if (response == null) {
            throw new ResourceNotFoundException(
                    "Gemini returned empty response"
            );
        }

        try {
            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.get("candidates");

            Map<String, Object> content =
                    (Map<String, Object>) candidates.get(0).get("content");

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) content.get("parts");

            return (String) parts.getFirst().get("text");

        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    "Failed to parse Gemini response"
            );
        }
    }
}