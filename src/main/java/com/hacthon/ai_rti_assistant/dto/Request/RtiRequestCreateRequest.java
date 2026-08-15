package com.hacthon.ai_rti_assistant.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RtiRequestCreateRequest {

    @NotBlank
    private String issueDescription;

    private String location;

    private String department;

    private boolean informationConfirmed;

    private boolean submissionConsent;
}