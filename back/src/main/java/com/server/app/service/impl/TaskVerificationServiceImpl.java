package com.server.app.service.impl;

import com.google.common.base.Preconditions;
import com.server.app.model.dto.VerificationResultDto;
import com.server.app.service.TaskVerificationService;
import com.server.app.util.OutputPreparer;
import com.server.parser.java.JavaParserAdapter;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.JavaTaskListener;
import com.server.parser.java.task.JavaTaskParserBuilder;
import com.server.parser.java.task.verifier.TaskVerifier;
import com.server.parser.util.exception.PrintableParseException;
import com.server.parser.util.exception.ResolvingException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
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
        JavaTaskParser.RulesEOFContext rulesEOFContext;
        Task computedTask;
        JavaTaskListener javaTaskListener;
        try {
            rulesEOFContext = createJavaTaskRulesContext(task);
        } catch (PrintableParseException e) {
            logger.error("Invalid task:\n" + e.getMessage());
            return VerificationResultDto.invalidTask();
        }
        try {
            computedTask = getTask(input);
            javaTaskListener = createJavaTaskListener(computedTask);
        } catch (PrintableParseException e) {
            logger.error("Invalid input:\n" + e.getMessage());
            return VerificationResultDto.invalidInput(e.getMessage(), e.getLineNumber());
        } catch (ResolvingException e) {
            logger.error("Resolving error during verification:\n" + e.getMessage());
            return VerificationResultDto.invalid(e);
        }
        try {
            verify(rulesEOFContext, javaTaskListener);
            logger.info("Verification completed successfully");
            return VerificationResultDto.valid(OutputPreparer.prepare(computedTask.getPrintCalls()));
        } catch (Exception e) {
            logger.error("Error during verification:\n" + e.getMessage());
            return VerificationResultDto.invalid(e);
        }
    }

    Task getTask(String input) {
        return JavaParserAdapter.getTask(input);
    }

    void verify(JavaTaskParser.RulesEOFContext rulesEOFContext, JavaTaskListener javaTaskListener) {
        new ParseTreeWalker().walk(javaTaskListener, rulesEOFContext);
    }

    JavaTaskParser.RulesEOFContext createJavaTaskRulesContext(String task) {
        JavaTaskParser parser = JavaTaskParserBuilder.build(task);
        return parser.rulesEOF();
    }

    JavaTaskListener createJavaTaskListener(Task task) {
        return new JavaTaskListener(new TaskVerifier(task));
    }
}