package com.hacthon.ai_rti_assistant.service.impl;

import com.hacthon.ai_rti_assistant.entity.PIO;
import com.hacthon.ai_rti_assistant.repository.PIORepository;
import com.hacthon.ai_rti_assistant.service.PIOService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PIOServiceImpl implements PIOService {

    private final PIORepository pioRepository;

    public PIOServiceImpl(PIORepository pioRepository) {
        this.pioRepository = pioRepository;
    }

    @Override
    public List<PIO> getPiosByDepartment(Long departmentId) {
        return pioRepository.findByDepartmentId(departmentId);
    }
}