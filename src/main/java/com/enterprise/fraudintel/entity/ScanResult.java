package com.enterprise.fraudintel.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "scan_results")
@Data
public class ScanResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private Double riskScore;
    private String riskLevel;
    private String socialMediaSentiment; // Requested: "Positive", "Neutral", "Negative"
    
    private LocalDateTime scanTimestamp;

    @PrePersist
    protected void onCreate() {
        scanTimestamp = LocalDateTime.now();
    }
}
