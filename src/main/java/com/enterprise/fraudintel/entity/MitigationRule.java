package com.enterprise.fraudintel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mitigation_rules")
@Data
public class MitigationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String action; // e.g., "BLOCK", "FLAG", "MFA"
    private boolean enabled;
    private Integer priority;
}
