package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.entity.PIO;

import java.util.List;

public interface PIOService {

    List<PIO> getPiosByDepartment(Long departmentId);
}