package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.annotation.PostConstruct;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@Configuration
@PropertySource("classpath:application.yml")
public class SplunkConfig {

    private static final Logger logger = LoggerFactory.getLogger(SplunkConfig.class);

    @Value("${logging.splunk.url:http://localhost:8088}")
    private String splunkUrl;

    @Value("${logging.splunk.token:8357121e-7e1b-4000-aa1d-f3b482531210}")
    private String splunkToken;

    @Value("${logging.splunk.source:spring-boot-multi-api}")
    private String splunkSource;

    @Value("${logging.splunk.index:main}")
    private String splunkIndex;

    @PostConstruct
    public void validateSplunkConfiguration() {
        logger.info("=== Splunk Configuration Validation ===");
        logger.info("Splunk URL: {}", splunkUrl);
        logger.info("Splunk Token: {}...{}", 
            splunkToken.substring(0, Math.min(8, splunkToken.length())),
            splunkToken.substring(Math.max(0, splunkToken.length() - 4)));
        logger.info("Splunk Source: {}", splunkSource);
        logger.info("Splunk Index: {}", splunkIndex);
        logger.info("=====================================");
        
        // Configure SSL to trust all certificates for Splunk
        configureSSLForSplunk();
    }
    
    private void configureSSLForSplunk() {
        try {
            // Create a trust manager that trusts all certificates
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };

            // Create SSL context that trusts all certificates
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            
            // Set as default SSL context
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            
            logger.info("SSL certificate validation disabled for Splunk connections");
        } catch (Exception e) {
            logger.error("Failed to configure SSL for Splunk: {}", e.getMessage());
        }
    }

    public String getSplunkUrl() {
        return splunkUrl;
    }

    public String getSplunkToken() {
        return splunkToken;
    }

    public String getSplunkSource() {
        return splunkSource;
    }

    public String getSplunkIndex() {
        return splunkIndex;
    }
} 