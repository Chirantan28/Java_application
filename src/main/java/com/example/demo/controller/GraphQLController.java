package com.example.demo.controller;

import com.example.demo.model.Demo;
import com.example.demo.service.DemoService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class GraphQLController {
    private static final Logger logger = LoggerFactory.getLogger(GraphQLController.class);
    private final DemoService demoService;

    public GraphQLController(DemoService demoService) {
        this.demoService = demoService;
    }

    @QueryMapping
    public Demo getMessage(@Argument Long id) {
        logger.info("Received GraphQL request for id: {}", id);
        Demo response = demoService.getMessage(id);
        logger.info("Returning GraphQL response: {}", response);
        return response;
    }
}