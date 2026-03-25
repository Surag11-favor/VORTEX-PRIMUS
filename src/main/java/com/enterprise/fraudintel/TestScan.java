package com.enterprise.fraudintel;

import com.enterprise.fraudintel.service.ScanService;
import java.util.Map;

public class TestScan {
    public static void main(String[] args) {
        // Instantiate ScanService with null repositories (we are just testing the heuristics, not DB saving)
        ScanService scanService = new ScanService(null, null);
        
        System.out.println("Beginning direct heuristic scan on YouTube channel...");
        Map<String, Object> result = scanService.analyzeUrl("https://www.youtube.com/@theformivelyris");
        
        System.out.println("\n--- SCAN RESULT ---");
        System.out.println("Risk Level: " + result.get("riskLevel"));
        System.out.println("Risk Score: " + result.get("riskScore"));
        System.out.println("Sentiment: " + result.get("sentiment"));
        System.out.println("Summary: " + result.get("summary"));
    }
}
