package com.hacthon.ai_rti_assistant.repository;

import com.hacthon.ai_rti_assistant.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Long> {

    Optional<District> findByNameIgnoreCase(String name);
}