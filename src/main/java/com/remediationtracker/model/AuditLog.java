package com.remediationtracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private Long targetId;

    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

    // getters and setters
}