package com.server.app.service.impl;

import com.google.common.base.Preconditions;
import com.server.app.model.dto.VerificationResultDto;
import com.server.app.service.TaskVerificationService;
import com.server.app.util.output.OutputPreparer;
import com.server.parser.java.JavaParserAdapter;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.JavaTaskParserAdapter;
import com.server.parser.util.exception.PrintableParseException;
import com.server.parser.util.exception.ResolvingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskVerificationServiceImpl implements TaskVerificationService {
    private static final Logger logger = LoggerFactory.getLogger(TaskVerificationServiceImpl.class);

    @Override
    public VerificationResultDto verify(String task, String input) {
        Preconditions.checkArgument(task != null, "task cannot be null");
        Preconditions.checkArgument(input != null, "input cannot be null");

        Task resolvedTask;
        String output;
        try {
            resolvedTask = getResolvedUserInputTask(input);
            output = getOutput(resolvedTask);
        } catch (PrintableParseException e) {
            logger.error("Invalid input:\n" + e.getMessage());
            return VerificationResultDto.invalidInput(e.getMessage(), e.getLineNumber());
        } catch (ResolvingException e) {
            logger.error("Resolving error during verification:\n" + e.getMessage());
            return VerificationResultDto.invalid(e);
        }

        try {
            verify(task, resolvedTask);
            logger.info("Verification completed successfully");
            return VerificationResultDto.valid(output);
        } catch (PrintableParseException e) {
            logger.error("Invalid task:\n" + e.getMessage());
            return VerificationResultDto.invalidTask(output);
        } catch (Exception e) {
            logger.error("Error during verification:\n" + e.getMessage());
            return VerificationResultDto.invalid(e, output);
        }
    }

    String getOutput(Task resolvedTask) {
        return OutputPreparer.prepare(resolvedTask.getPrintCalls());
    }

    Task getResolvedUserInputTask(String input) {
        return JavaParserAdapter.getResolvedTask(input);
    }

    void verify(String task, Task resolvedTask) {
        JavaTaskParserAdapter.verifyTask(task, resolvedTask);
    }
}