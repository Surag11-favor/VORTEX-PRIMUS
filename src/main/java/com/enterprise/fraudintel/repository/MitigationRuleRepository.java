package com.enterprise.fraudintel.repository;

import com.enterprise.fraudintel.entity.MitigationRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MitigationRuleRepository extends JpaRepository<MitigationRule, Long> {
    long countByEnabledTrue();
}
