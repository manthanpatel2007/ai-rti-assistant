package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.entity.*;
import com.hacthon.ai_rti_assistant.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GovernmentDirectoryService {

    private final DistrictRepository districtRepository;
    private final SubDistrictRepository subDistrictRepository;
    private final DepartmentRepository departmentRepository;
    private final ProblemCategoryRepository problemCategoryRepository;
    private final PIORepository pioRepository;

    public GovernmentDirectoryService(
            DistrictRepository districtRepository,
            SubDistrictRepository subDistrictRepository,
            DepartmentRepository departmentRepository,
            ProblemCategoryRepository problemCategoryRepository,
            PIORepository pioRepository
    ) {
        this.districtRepository = districtRepository;
        this.subDistrictRepository = subDistrictRepository;
        this.departmentRepository = departmentRepository;
        this.problemCategoryRepository = problemCategoryRepository;
        this.pioRepository = pioRepository;
    }

    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    public List<SubDistrict> getSubDistricts(Long districtId) {
        return subDistrictRepository.findByDistrictId(districtId);
    }

    public List<Department> getDepartments(Long subDistrictId) {
        return departmentRepository.findBySubDistrictId(subDistrictId);
    }

    public List<ProblemCategory> getProblems(Long departmentId) {
        return problemCategoryRepository.findByDepartmentId(departmentId);
    }

    public List<PIO> getPios(Long departmentId) {
        return pioRepository.findByDepartmentId(departmentId);
    }

}