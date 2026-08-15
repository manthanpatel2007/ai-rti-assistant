package com.hacthon.ai_rti_assistant.entity;

import com.hacthon.ai_rti_assistant.entity.Department;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "government_pios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PIO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String officeAddress;

    @Column(columnDefinition = "TEXT")
    private String sourceUrl;

    private LocalDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}