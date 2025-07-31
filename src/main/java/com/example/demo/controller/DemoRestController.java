package com.example.demo.controller;

import com.example.demo.model.Demo;
import com.example.demo.service.DemoService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/api")
public class DemoRestController {
    private static final Logger logger = LoggerFactory.getLogger(DemoRestController.class);
    private final DemoService demoService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public DemoRestController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/message/{id}")
    public Demo getMessage(@PathVariable Long id) {
        logger.info("Received REST request for id: {}", id);
        Demo response = demoService.getMessage(id);
        logger.info("Returning response: {}", response);
        return response;
    }

    @PostMapping("/generate-load")
    public String generateLoad(@RequestParam(defaultValue = "100") int requests) {
        logger.info("Starting load generation with {} requests", requests);
        
        LongStream.range(0, requests).forEach(id -> {
            executorService.submit(() -> {
                try {
                    demoService.getMessage(id);
                } catch (Exception e) {
                    logger.error("Error processing request {}", id, e);
                }
            });
        });
        
        return "Generated " + requests + " requests";
    }

    @PostMapping("/process")
    public String processInput(@RequestBody String input) {
        return demoService.processBusinessLogic(input);
    }
}