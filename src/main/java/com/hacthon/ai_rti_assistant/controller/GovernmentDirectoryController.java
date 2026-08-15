package com.hacthon.ai_rti_assistant.controller;

import com.hacthon.ai_rti_assistant.entity.*;
import com.hacthon.ai_rti_assistant.service.GovernmentDirectoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/government")
public class GovernmentDirectoryController {

    private final GovernmentDirectoryService governmentDirectoryService;

    public GovernmentDirectoryController(
            GovernmentDirectoryService governmentDirectoryService
    ) {
        this.governmentDirectoryService = governmentDirectoryService;
    }

    @GetMapping("/districts")
    public ResponseEntity<List<District>> getDistricts() {
        return ResponseEntity.ok(
                governmentDirectoryService.getAllDistricts()
        );
    }

    @GetMapping("/districts/{districtId}/sub-districts")
    public ResponseEntity<List<SubDistrict>> getSubDistricts(
            @PathVariable Long districtId
    ) {
        return ResponseEntity.ok(
                governmentDirectoryService.getSubDistricts(districtId)
        );
    }

    @GetMapping("/sub-districts/{subDistrictId}/departments")
    public ResponseEntity<List<Department>> getDepartments(
            @PathVariable Long subDistrictId
    ) {
        return ResponseEntity.ok(
                governmentDirectoryService.getDepartments(subDistrictId)
        );
    }

    @GetMapping("/departments/{departmentId}/problems")
    public ResponseEntity<List<ProblemCategory>> getProblems(
            @PathVariable Long departmentId
    ) {
        return ResponseEntity.ok(
                governmentDirectoryService.getProblems(departmentId)
        );
    }

    @GetMapping("/departments/{departmentId}/pios")
    public ResponseEntity<List<PIO>> getPios(
            @PathVariable Long departmentId
    ) {
        return ResponseEntity.ok(
                governmentDirectoryService.getPios(departmentId)
        );
    }
}