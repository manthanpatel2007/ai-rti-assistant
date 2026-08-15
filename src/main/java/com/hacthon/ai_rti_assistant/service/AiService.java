package com.hacthon.ai_rti_assistant.service;

public interface AiService {

    String generateRti(String issueDescription, String location, String department);
}