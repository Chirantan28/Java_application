package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import jakarta.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:application.yml")
public class SplunkConfig {

    private static final Logger logger = LoggerFactory.getLogger(SplunkConfig.class);

    @Value("${logging.splunk.url:https://localhost:8088}")
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