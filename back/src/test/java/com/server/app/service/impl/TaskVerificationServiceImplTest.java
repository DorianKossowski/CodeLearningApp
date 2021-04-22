package com.server.app.service.impl;

import com.server.app.model.dto.VerificationResultDto;
import com.server.parser.java.ast.Task;
import com.server.parser.util.exception.PrintableParseException;
import com.server.parser.util.exception.ResolvingException;
import com.server.util.LogTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskVerificationServiceImplTest {
    private static final String TASK = "task";
    private static final String INPUT = "input";
    private static final String OUTPUT = "output";

    @Mock
    private Task resolvedTask;

    private final TaskVerificationServiceImpl verificationService = spy(new TaskVerificationServiceImpl());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(resolvedTask).when(verificationService).getResolvedUserInputTask(INPUT);
        doReturn(OUTPUT).when(verificationService).getOutput(resolvedTask);
    }

    @Test
    void shouldInvalidWhenWrongInput() {
        PrintableParseException exception = mock(PrintableParseException.class);
        when(exception.getMessage()).thenReturn("msg");
        when(exception.getLineNumber()).thenReturn(1);
        doThrow(exception).when(verificationService).getResolvedUserInputTask(INPUT);

        try (LogTester logTester = new LogTester(TaskVerificationServiceImpl.class)) {
            VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

            assertThat(resultDto).isEqualTo(VerificationResultDto.invalidInput("msg", 1));
            logTester.assertError("Invalid input:\nmsg");
        }
    }

    @Test
    void shouldInvalidWhenResolvingError() {
        ResolvingException exception = new ResolvingException("ERROR");
        doThrow(exception).when(verificationService).getResolvedUserInputTask(INPUT);

        try (LogTester logTester = new LogTester(TaskVerificationServiceImpl.class)) {
            VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

            assertThat(resultDto).isEqualTo(VerificationResultDto.invalid(new Exception("Problem podczas rozwiązywania: ERROR")));
            logTester.assertError("Resolving error during verification:\nProblem podczas rozwiązywania: ERROR");
        }
    }

    @Test
    void shouldInvalidWhenWrongTask() {
        PrintableParseException exception = mock(PrintableParseException.class);
        doReturn("ERROR").when(exception).getMessage();
        doThrow(exception).when(verificationService).verify(TASK, resolvedTask);

        try (LogTester logTester = new LogTester(TaskVerificationServiceImpl.class)) {
            VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

            assertThat(resultDto).isEqualTo(VerificationResultDto.invalidTask(OUTPUT));
            logTester.assertError("Invalid task:\nERROR");
        }
    }

    @Test
    void shouldInvalidWhenExceptionDuringVerify() {
        RuntimeException exception = new RuntimeException("msg");
        doThrow(exception).when(verificationService).verify(TASK, resolvedTask);

        try (LogTester logTester = new LogTester(TaskVerificationServiceImpl.class)) {
            VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

            assertThat(resultDto).isEqualTo(VerificationResultDto.invalid(exception, OUTPUT));
            logTester.assertError("Error during verification:\nmsg");
        }
    }

    @Test
    void shouldValidWhenVerified() {
        doNothing().when(verificationService).verify(TASK, resolvedTask);

        try (LogTester logTester = new LogTester(TaskVerificationServiceImpl.class)) {
            VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

            assertThat(resultDto).isEqualTo(VerificationResultDto.valid(OUTPUT));
            logTester.assertInfo("Verification completed successfully");
        }
    }

    @Test
    void shouldCatchWhenResolvingExceptionDuringOutput() {
        ResolvingException exception = new ResolvingException("ERROR");
        doThrow(exception).when(verificationService).getOutput(resolvedTask);

        try (LogTester logTester = new LogTester(TaskVerificationServiceImpl.class)) {
            VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

            assertThat(resultDto).isEqualTo(VerificationResultDto.invalid(new Exception("Problem podczas rozwiązywania: ERROR")));
            logTester.assertError("Resolving error during verification:\nProblem podczas rozwiązywania: ERROR");
        }
    }
}