package com.server.app.service;

import com.server.app.model.dto.VerificationResultDto;

public interface TaskVerificationService {
    VerificationResultDto verify(String task, String input);
}