package com.enterprise.fraudintel.repository;

import com.enterprise.fraudintel.entity.ScanResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScanResultRepository extends JpaRepository<ScanResult, Long> {
    List<ScanResult> findAllByOrderByScanTimestampDesc();
    long countByRiskLevel(String riskLevel);
}
