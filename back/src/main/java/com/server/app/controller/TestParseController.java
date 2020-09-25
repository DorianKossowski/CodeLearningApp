package com.server.app.controller;

import com.server.app.model.dto.TestInputDto;
import com.server.app.service.TaskVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class TestParseController {
    private static final Logger logger = LoggerFactory.getLogger(TestParseController.class);

    private final TaskVerificationService verificationService;

    public TestParseController(TaskVerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/parse")
    public String parseInput(@RequestBody TestInputDto testInputDto) {
        logger.debug("Parsing: " + testInputDto.getInput());
        verificationService.verify(testInputDto.getTask(), testInputDto.getInput());
        return "";
    }
}