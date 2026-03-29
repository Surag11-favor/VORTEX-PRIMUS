package com.enterprise.fraudintel.controller;

import com.enterprise.fraudintel.repository.AuditLogRepository;
import com.enterprise.fraudintel.repository.MitigationRuleRepository;
import com.enterprise.fraudintel.repository.ScanResultRepository;
import com.enterprise.fraudintel.repository.UserRepository;
import com.enterprise.fraudintel.entity.MitigationRule;
import com.enterprise.fraudintel.entity.ScanResult;
import com.enterprise.fraudintel.entity.User;
import com.enterprise.fraudintel.service.ScanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageController {

    private final ScanResultRepository scanResultRepository;
    private final MitigationRuleRepository mitigationRuleRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final ScanService scanService;

    public PageController(ScanResultRepository scanResultRepository, 
                          MitigationRuleRepository mitigationRuleRepository,
                          UserRepository userRepository,
                          AuditLogRepository auditLogRepository,
                          PasswordEncoder passwordEncoder,
                          ScanService scanService) {
        this.scanResultRepository = scanResultRepository;
        this.mitigationRuleRepository = mitigationRuleRepository;
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.scanService = scanService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("scans", scanResultRepository.findAllByOrderByScanTimestampDesc());
        return "index";
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activeThreats", scanResultRepository.countByRiskLevel("HIGH"));
        model.addAttribute("blockedThreats", auditLogRepository.countByAction("BLOCK"));
        model.addAttribute("totalScans", scanResultRepository.count());
        model.addAttribute("activeUsers", userRepository.count());
        model.addAttribute("recentLogs", auditLogRepository.findAllByOrderByTimestampDesc().stream().limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/threat-scan")
    public String threatScan(Model model) {
        model.addAttribute("scans", scanResultRepository.findAllByOrderByScanTimestampDesc());
        return "threat-scan";
    }

    @GetMapping("/mitigation-rules")
    public String mitigationRules(Model model) {
        model.addAttribute("rules", mitigationRuleRepository.findAll());
        return "mitigation-rules";
    }

    @GetMapping("/authorized-users")
    public String authorizedUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "authorized-users";
    }

    @GetMapping("/audit-logs")
    public String auditLogs(Model model) {
        model.addAttribute("logs", auditLogRepository.findAllByOrderByTimestampDesc());
        return "audit-logs";
    }

    @PostMapping("/rules/add")
    public String addRule(MitigationRule rule) {
        if (rule.getPriority() == null) rule.setPriority(1);
        mitigationRuleRepository.save(rule);
        return "redirect:/mitigation-rules";
    }

    @GetMapping("/rules/delete/{id}")
    public String deleteRule(@PathVariable Long id) {
        mitigationRuleRepository.deleteById(id);
        return "redirect:/mitigation-rules";
    }

    @PostMapping("/authorized-users/add")
    public String addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            String rawPassword = (user.getPassword() != null && !user.getPassword().isEmpty()) 
                ? user.getPassword() : "password123";
            user.setPassword(passwordEncoder.encode(rawPassword));
            if (user.getRole() == null || user.getRole().isEmpty()) user.setRole("USER");
            userRepository.save(user);
        }
        return "redirect:/authorized-users";
    }

    @GetMapping("/authorized-users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/authorized-users";
    }

    @PostMapping("/threat-scan")
    public String performScan(String targetIp, java.security.Principal principal) {
        if (targetIp != null && !targetIp.isEmpty()) {
            Map<String, Object> analysis = scanService.analyzeUrl(targetIp);

            ScanResult result = new ScanResult();
            result.setPayload(targetIp);

            Object scoreObj = analysis.get("threatScore");
            double threatScore = scoreObj instanceof Number ? ((Number) scoreObj).doubleValue() : 0.0;
            result.setRiskScore(threatScore);
            result.setRiskLevel(String.valueOf(analysis.get("riskRating")));
            
            String summary = String.valueOf(analysis.get("summary"));
            result.setSocialMediaSentiment(summary.length() > 250 ? summary.substring(0, 250) : summary);
            scanResultRepository.save(result);
        }
        return "redirect:/threat-scan";
    }

    @PostMapping("/")
    public String publicScan(String targetIp) {
        if (targetIp != null && !targetIp.isEmpty()) {
            Map<String, Object> analysis = scanService.analyzeUrl(targetIp);

            ScanResult result = new ScanResult();
            result.setPayload(targetIp);

            Object scoreObj = analysis.get("threatScore");
            double threatScore = scoreObj instanceof Number ? ((Number) scoreObj).doubleValue() : 0.0;
            result.setRiskScore(threatScore);
            result.setRiskLevel(String.valueOf(analysis.get("riskRating")));

            String summary = String.valueOf(analysis.get("summary"));
            result.setSocialMediaSentiment(summary.length() > 250 ? summary.substring(0, 250) : summary);
            scanResultRepository.save(result);
        }
        return "redirect:/";
    }

    @GetMapping("/clear-archives")
    public String clearArchives() {
        scanResultRepository.deleteAll();
        return "redirect:/";
    }
}
