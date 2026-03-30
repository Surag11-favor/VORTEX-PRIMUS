package com.enterprise.fraudintel.controller;

import com.enterprise.fraudintel.repository.AuditLogRepository;
import com.enterprise.fraudintel.repository.ScanResultRepository;
import com.enterprise.fraudintel.repository.UserRepository;
import com.enterprise.fraudintel.repository.MitigationRuleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
public class PageController {

    private final ScanResultRepository scanResultRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final MitigationRuleRepository mitigationRuleRepository;

    public PageController(ScanResultRepository scanResultRepository, 
                          AuditLogRepository auditLogRepository,
                          UserRepository userRepository,
                          MitigationRuleRepository mitigationRuleRepository) {
        this.scanResultRepository = scanResultRepository;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
        this.mitigationRuleRepository = mitigationRuleRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("scans", scanResultRepository.findAllByOrderByScanTimestampDesc().stream().limit(50).collect(Collectors.toList()));
        return "index";
    }

    @PostMapping("/")
    public String publicScan(String targetIp) {
        // Simple redirect to trigger actual scan logic in ScanController
        return "redirect:/threat-scan?payload=" + targetIp;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activeThreats", scanResultRepository.countByRiskLevel("HIGH"));
        model.addAttribute("blockedThreats", auditLogRepository.countByAction("BLOCK"));
        model.addAttribute("totalScans", scanResultRepository.count());
        model.addAttribute("activeUsers", userRepository.count());
        model.addAttribute("recentLogs", auditLogRepository.findAllByOrderByTimestampDesc().stream().limit(5).collect(Collectors.toList()));
        return "dashboard";
    }

    @GetMapping("/threat-scan")
    public String threatScan(Model model) {
        model.addAttribute("scans", scanResultRepository.findAllByOrderByScanTimestampDesc().stream().limit(50).collect(Collectors.toList()));
        return "threat-scan";
    }

    @GetMapping("/mitigation-rules")
    public String mitigationRules(Model model) {
        model.addAttribute("rules", mitigationRuleRepository.findAll());
        return "mitigation-rules";
    }

    @GetMapping("/audit-logs")
    public String auditLogs(Model model) {
        model.addAttribute("logs", auditLogRepository.findAllByOrderByTimestampDesc().stream().limit(100).collect(Collectors.toList()));
        return "audit-logs";
    }

    @GetMapping("/authorized-users")
    public String authorizedUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "authorized-users";
    }
}
