package com.hacthon.ai_rti_assistant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "government_departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_district_id", nullable = false)
    private SubDistrict subDistrict;
}