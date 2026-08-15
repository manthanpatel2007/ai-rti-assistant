package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.dto.Request.RtiRequestCreateRequest;
import com.hacthon.ai_rti_assistant.entity.RtiRequest;

import java.util.List;

public interface RtiService {

    RtiRequest createRti(
            RtiRequestCreateRequest request,
            String userEmail
    );

    List<RtiRequest> getMyRtis(String userEmail);

    void sendRti(
            Long rtiRequestId,
            String userEmail
    );
}