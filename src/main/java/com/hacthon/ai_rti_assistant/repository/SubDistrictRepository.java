package com.hacthon.ai_rti_assistant.repository;

import com.hacthon.ai_rti_assistant.entity.SubDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubDistrictRepository extends JpaRepository<SubDistrict, Long> {

    List<SubDistrict> findByDistrictId(Long districtId);
}