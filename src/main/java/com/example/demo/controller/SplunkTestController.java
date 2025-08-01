package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class SplunkTestController {

    private static final Logger logger = LoggerFactory.getLogger(SplunkTestController.class);

    @GetMapping("/splunk-test")
    public Map<String, Object> testSplunkLogging() {
        String requestId = UUID.randomUUID().toString();
        LocalDateTime timestamp = LocalDateTime.now();
        
        logger.info("SPLUNK_TEST: Application test started - RequestID: {}, Time: {}", requestId, timestamp);
        logger.warn("SPLUNK_TEST: This is a warning message for Splunk testing");
        logger.error("SPLUNK_TEST: This is an error message for Splunk testing");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Splunk test logs generated");
        response.put("requestId", requestId);
        response.put("timestamp", timestamp.toString());
        response.put("testType", "splunk-integration");
        
        logger.info("SPLUNK_TEST: Test completed successfully - RequestID: {}", requestId);
        
        return response;
    }

    @GetMapping("/splunk-structured")
    public Map<String, Object> testStructuredLogging() {
        String sessionId = UUID.randomUUID().toString();
        
        // Simulate user action
        logger.info("USER_ACTION: User login successful - SessionID: {}, UserID: 12345, IP: 192.168.1.100", sessionId);
        
        // Simulate performance metric
        logger.warn("PERFORMANCE: Database query slow - QueryTime: 2500ms, Query: SELECT * FROM users", sessionId);
        
        // Simulate error
        logger.error("ERROR: Database connection failed - ErrorCode: DB001, Message: Connection timeout", sessionId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("logsGenerated", 3);
        response.put("testType", "structured-logging");
        
        return response;
    }

    @GetMapping("/splunk-bulk")
    public Map<String, Object> generateBulkLogs(@RequestParam(defaultValue = "10") int count) {
        String batchId = UUID.randomUUID().toString();
        
        logger.info("BULK_TEST: Starting bulk log generation - BatchID: {}, Count: {}", batchId, count);
        
        for (int i = 1; i <= count; i++) {
            logger.info("BULK_LOG_{}: Generated log entry {} of {} - BatchID: {}", i, i, count, batchId);
            
            if (i % 3 == 0) {
                logger.warn("BULK_WARN_{}: Warning message {} - BatchID: {}", i, i, batchId);
            }
            
            if (i % 5 == 0) {
                logger.error("BULK_ERROR_{}: Error message {} - BatchID: {}", i, i, batchId);
            }
        }
        
        logger.info("BULK_TEST: Completed bulk log generation - BatchID: {}, TotalLogs: {}", batchId, count);
        
        Map<String, Object> response = new HashMap<>();
        response.put("batchId", batchId);
        response.put("logsGenerated", count);
        response.put("status", "completed");
        
        return response;
    }

    @GetMapping("/splunk-health")
    public Map<String, Object> checkSplunkHealth() {
        String healthId = UUID.randomUUID().toString();
        
        logger.info("HEALTH_CHECK: Splunk health check initiated - HealthID: {}", healthId);
        
        // Simulate different health scenarios
        logger.info("HEALTH_CHECK: Application status - Status: RUNNING, Uptime: 3600s, Memory: 512MB", healthId);
        logger.info("HEALTH_CHECK: Database status - Status: CONNECTED, ResponseTime: 45ms", healthId);
        logger.warn("HEALTH_CHECK: Cache status - Status: DEGRADED, HitRate: 65%", healthId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("healthId", healthId);
        response.put("status", "healthy");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return response;
    }
} 