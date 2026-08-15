package com.hacthon.ai_rti_assistant.repository;

import com.hacthon.ai_rti_assistant.entity.RtiAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RtiAttachmentRepository
        extends JpaRepository<RtiAttachment, Long> {

    List<RtiAttachment> findByRtiRequestId(Long rtiRequestId);
}