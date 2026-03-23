package com.enterprise.fraudintel.controller;

import com.enterprise.fraudintel.entity.AuditLog;
import com.enterprise.fraudintel.entity.ScanResult;
import com.enterprise.fraudintel.repository.AuditLogRepository;
import com.enterprise.fraudintel.repository.ScanResultRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/analysis")
public class ScanController {

    private final ScanResultRepository scanResultRepository;
    private final AuditLogRepository auditLogRepository;

    public ScanController(ScanResultRepository scanResultRepository, AuditLogRepository auditLogRepository) {
        this.scanResultRepository = scanResultRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @PostMapping("/scan")
    public Map<String, Object> runScan(@RequestBody Map<String, String> request, Principal principal) {
        String content = request.get("content");
        
        // Mock analysis logic
        Random random = new Random();
        double riskScore = 15 + (85 * random.nextDouble());
        String riskLevel = riskScore > 75 ? "HIGH" : (riskScore > 40 ? "MEDIUM" : "LOW");
        
        // Social Media Sentiment logic
        String[] sentiments = {"Positive", "Neutral", "Negative", "Highly Suspicious"};
        String sentiment = sentiments[random.nextInt(sentiments.length)];
        
        // Persist Result
        ScanResult result = new ScanResult();
        result.setPayload(content);
        result.setRiskScore(riskScore);
        result.setRiskLevel(riskLevel);
        result.setSocialMediaSentiment(sentiment);
        scanResultRepository.save(result);

        // Audit Log
        AuditLog log = new AuditLog();
        log.setAction("SCAN_PERFORMED");
        log.setPerformedBy(principal != null ? principal.getName() : "Anonymous");
        log.setDetails("Scanned payload: " + (content != null && content.length() > 30 ? content.substring(0, 30) + "..." : content));
        auditLogRepository.save(log);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("riskScore", riskScore);
        response.put("riskLevel", riskLevel);
        response.put("sentiment", sentiment);
        response.put("summary", "Analysis complete for payload. Risk detected: " + riskLevel);
        
        return response;
    }
}
