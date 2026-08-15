package com.hacthon.ai_rti_assistant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sub_districts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;
}