package com.hacthon.ai_rti_assistant.repository;

import com.hacthon.ai_rti_assistant.entity.PIO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PIORepository extends JpaRepository<PIO, Long> {

    List<PIO> findByDepartmentId(Long departmentId);

    Optional<PIO> findFirstByDepartmentName(String departmentName);
}