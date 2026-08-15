package com.hacthon.ai_rti_assistant.repository;

import com.hacthon.ai_rti_assistant.entity.ProblemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemCategoryRepository extends JpaRepository<ProblemCategory, Long> {

    List<ProblemCategory> findByDepartmentId(Long departmentId);
}