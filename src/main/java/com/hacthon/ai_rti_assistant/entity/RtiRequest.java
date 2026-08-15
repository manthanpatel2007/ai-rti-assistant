package com.hacthon.ai_rti_assistant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rti_requests")
public class RtiRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String issueDescription;

    private String location;

    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RtiStatus status;

    @Column(columnDefinition = "TEXT")
    private String generatedContent;

    // Generated PDF location
    private String pdfPath;

    // Government PIO email
    private String recipientEmail;

    // Time when RTI was sent
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "rtiRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RtiAttachment> attachments = new ArrayList<>();

    private boolean informationConfirmed;
    private boolean submissionConsent;
    private LocalDateTime consentAcceptedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}