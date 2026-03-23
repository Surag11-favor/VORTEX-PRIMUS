package com.enterprise.fraudintel.config;

import com.enterprise.fraudintel.entity.AuditLog;
import com.enterprise.fraudintel.entity.MitigationRule;
import com.enterprise.fraudintel.entity.User;
import com.enterprise.fraudintel.repository.AuditLogRepository;
import com.enterprise.fraudintel.repository.MitigationRuleRepository;
import com.enterprise.fraudintel.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(MitigationRuleRepository ruleRepo, AuditLogRepository logRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepo.save(admin);
            }

            if (ruleRepo.count() == 0) {
                MitigationRule r1 = new MitigationRule();
                r1.setName("IP Intelligence Filter");
                r1.setDescription("Blocks requests from known proxy and VPN networks associated with fraud.");
                r1.setAction("BLOCK");
                r1.setEnabled(true);
                r1.setPriority(1);
                ruleRepo.save(r1);

                MitigationRule r2 = new MitigationRule();
                r2.setName("Payload Sanitization");
                r2.setDescription("Automatically strips dangerous characters from suspicious payload strings.");
                r2.setAction("SANITIZE");
                r2.setEnabled(true);
                r2.setPriority(2);
                ruleRepo.save(r2);

                MitigationRule r3 = new MitigationRule();
                r3.setName("Anomalous Volume Shield");
                r3.setDescription("Triggers MFA if a single user performs more than 50 scans per minute.");
                r3.setAction("CHALLENGE");
                r3.setEnabled(false);
                r3.setPriority(3);
                ruleRepo.save(r3);
            }

            if (logRepo.count() == 0) {
                AuditLog l1 = new AuditLog();
                l1.setAction("SYSTEM_STARTUP");
                l1.setPerformedBy("SYSTEM");
                l1.setDetails("Fraud Intelligence Engine successfully initialized.");
                l1.setTimestamp(LocalDateTime.now().minusHours(2));
                logRepo.save(l1);

                AuditLog l2 = new AuditLog();
                l2.setAction("SECURITY_POLICY_UPDATE");
                l2.setPerformedBy("admin");
                l2.setDetails("Enabled 'IP Intelligence Filter' global mitigation rule.");
                l2.setTimestamp(LocalDateTime.now().minusHours(1));
                logRepo.save(l2);
            }
        };
    }
}
