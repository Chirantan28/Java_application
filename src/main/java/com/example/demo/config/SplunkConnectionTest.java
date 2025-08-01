package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.LocalDateTime;

@Component
public class SplunkConnectionTest implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SplunkConnectionTest.class);

    @Value("${logging.splunk.url:http://localhost:8088}")
    private String splunkUrl;

    @Value("${logging.splunk.token:8357121e-7e1b-4000-aa1d-f3b482531210}")
    private String splunkToken;

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== SPLUNK CONNECTION TEST STARTED ===");
        logger.info("Testing connection to Splunk at: {}", splunkUrl);
        logger.info("Using token: {}...{}", 
            splunkToken.substring(0, Math.min(8, splunkToken.length())),
            splunkToken.substring(Math.max(0, splunkToken.length() - 4)));
        
        testSplunkConnection();
        
        // Generate test logs
        generateTestLogs();
        
        logger.info("=== SPLUNK CONNECTION TEST COMPLETED ===");
    }

    // private void testSplunkConnection() {
    //     try {
    //         HttpClient client = HttpClient.newHttpClient();
    //         HttpRequest request = HttpRequest.newBuilder()
    //             .uri(URI.create(splunkUrl + "/services/collector"))
    //             .timeout(java.time.Duration.ofSeconds(5))
    //             .build();
            
    //         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    //         int responseCode = response.statusCode();
    //         logger.info("Splunk connection test - Response code: {}", responseCode);
            
    //         if (responseCode == 200) {
    //             logger.info("✅ Splunk is accessible and responding");
    //         } else if (responseCode == 401) {
    //             logger.warn("⚠️ Splunk is accessible but authentication failed - check token");
    //         } else if (responseCode == 404) {
    //             logger.warn("⚠️ Splunk is accessible but HTTP Event Collector might not be enabled");
    //         } else {
    //             logger.warn("⚠️ Splunk responded with unexpected code: {}", responseCode);
    //         }
            
    //     } catch (Exception e) {
    //         logger.error("❌ Failed to connect to Splunk: {}", e.getMessage());
    //         logger.info("💡 Troubleshooting tips:");
    //         logger.info("   1. Ensure Splunk is running");
    //         logger.info("   2. Verify HTTP Event Collector is enabled");
    //         logger.info("   3. Check if the URL is correct: {}", splunkUrl);
    //         logger.info("   4. Try HTTP instead of HTTPS if SSL is not configured");
    //     }
    // }
    private void testSplunkConnection() {
    try {
        HttpClient client = HttpClient.newHttpClient();

        String url = splunkUrl + "/services/collector/event"; // corrected endpoint
        String payload = "{\"event\":\"Splunk connection test at " + LocalDateTime.now() + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(java.time.Duration.ofSeconds(5))
            .header("Authorization", "Splunk " + splunkToken)  // required auth header
            .header("Content-Type", "application/json")         // required content type
            .POST(HttpRequest.BodyPublishers.ofString(payload)) // use POST method
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        logger.info("Splunk connection test - Response code: {}", code);

        if (code == 200) {
            logger.info("✅ Splunk HEC is working!");
        } else {
            logger.warn("⚠️ Unexpected response from Splunk: {}", response.body());
        }

    } catch (Exception e) {
        logger.error("❌ Failed to connect to Splunk: {}", e.getMessage());
    }
}

    private void generateTestLogs() {
        logger.info("🔍 Generating test logs for Splunk verification...");
        
        // Generate logs with different levels
        logger.debug("DEBUG: This is a debug message for Splunk testing");
        logger.info("INFO: This is an info message for Splunk testing");
        logger.warn("WARN: This is a warning message for Splunk testing");
        logger.error("ERROR: This is an error message for Splunk testing");
        
        // Generate structured logs
        logger.info("STRUCTURED_LOG: User action completed - UserID: 12345, Action: login, Timestamp: {}", LocalDateTime.now());
        logger.warn("STRUCTURED_LOG: Performance alert - ResponseTime: 2500ms, Endpoint: /api/users, Threshold: 2000ms");
        logger.error("STRUCTURED_LOG: System error - ErrorCode: DB001, Message: Database connection timeout, RetryCount: 3");
        
        logger.info("✅ Test logs generated successfully");
    }
} 