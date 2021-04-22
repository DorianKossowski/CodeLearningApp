package com.server.app.controller;

import com.server.app.model.dto.VerificationInputDto;
import com.server.app.model.dto.VerificationResultDto;
import com.server.app.service.TaskVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class TaskVerificationController {
    private static final Logger logger = LoggerFactory.getLogger(TaskVerificationController.class);

    private final TaskVerificationService verificationService;

    public TaskVerificationController(TaskVerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/validate")
    public VerificationResultDto parseInput(@RequestBody VerificationInputDto verificationInputDto) {
        logger.debug("Validating: " + verificationInputDto.getInput());
        return verificationService.verify(verificationInputDto.getTask(), verificationInputDto.getInput());
    }
}