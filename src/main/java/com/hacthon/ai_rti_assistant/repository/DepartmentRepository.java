package com.hacthon.ai_rti_assistant.repository;

import com.hacthon.ai_rti_assistant.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findBySubDistrictId(Long subDistrictId);
}