package com.hacthon.ai_rti_assistant.repository;

import com.hacthon.ai_rti_assistant.entity.RtiRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RtiRequestRepository extends JpaRepository<RtiRequest, Long> {

    List<RtiRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
}