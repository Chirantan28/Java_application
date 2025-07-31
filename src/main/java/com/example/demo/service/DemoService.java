package com.example.demo.service;

import com.example.demo.model.Demo;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import java.util.UUID;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.MDC;

@Service
public class DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);
    private final Random random = new Random();
    private final Tracer tracer;

    public DemoService(Tracer tracer) {
        this.tracer = tracer;
    }

    public Demo getMessage(Long id) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Processing request with ID: {} and requestId: {}", id, requestId);
        
        // Simulate processing time
        int processingTime = random.nextInt(500);
        try {
            Thread.sleep(processingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Processing interrupted for id: {}", id, e);
        }

        
        if (processingTime > 400) {
            logger.error("High processing time detected: {}ms for id: {}", processingTime, id);
        } else if (processingTime > 300) {
            logger.warn("Processing time above threshold: {}ms for id: {}", processingTime, id);
        } else {
            logger.debug("Normal processing time: {}ms for id: {}", processingTime, id);
        }

        Demo demo = new Demo(id, "Hello from service! ProcessingTime: " + processingTime + "ms");
        logger.info("Completed processing for id: {}, response: {}", id, demo);
        return demo;
    }

    public String processBusinessLogic(String input) {
        MDC.put("operation", "processBusinessLogic");
        Span span = tracer.spanBuilder("processBusinessLogic").startSpan();
        try (Scope scope = span.makeCurrent()) {
            logger.info("Processing input: {}", input);
            // Simulate business logic
            Thread.sleep(100);
            span.setAttribute("input.length", input.length());
            return "Processed: " + input;
        } catch (Exception e) {
            span.recordException(e);
            throw new RuntimeException("Error processing input", e);
        } finally {
            span.end();
            MDC.clear();
        }
    }
}