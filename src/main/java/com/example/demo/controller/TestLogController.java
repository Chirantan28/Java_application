package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestLogController {

    private static final Logger logger = LoggerFactory.getLogger(TestLogController.class);

    @GetMapping("/test-log")
    public String testLogging() {
        logger.info("This is a test log message for Splunk.");
        logger.warn("This is a warning message for Splunk.");
        logger.error("This is an error message for Splunk.");
        return "Log messages sent to Splunk!";
    }

    @GetMapping("/test-splunk")
    public String testSplunkLogging(@RequestParam(defaultValue = "info") String level) {
        switch (level.toLowerCase()) {
            case "debug":
                logger.debug("DEBUG: Testing Splunk logging with debug level");
                break;
            case "info":
                logger.info("INFO: Testing Splunk logging with info level");
                break;
            case "warn":
                logger.warn("WARN: Testing Splunk logging with warn level");
                break;
            case "error":
                logger.error("ERROR: Testing Splunk logging with error level");
                break;
            default:
                logger.info("INFO: Testing Splunk logging with default level");
        }
        
        return String.format("Splunk test log sent with %s level", level);
    }

    @GetMapping("/test-splunk-structured")
    public String testStructuredLogging() {
        logger.info("Structured log test - User action: {}", "login");
        logger.warn("Structured log test - Performance issue: {} ms", 1500);
        logger.error("Structured log test - Database connection failed: {}", "timeout");
        
        return "Structured log messages sent to Splunk!";
    }
}