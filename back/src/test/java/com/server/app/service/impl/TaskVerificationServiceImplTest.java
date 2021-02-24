package com.server.app.service.impl;

import com.server.app.model.dto.VerificationResultDto;
import com.server.parser.java.ast.Task;
import com.server.parser.util.exception.PrintableParseException;
import com.server.parser.util.exception.ResolvingException;
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

        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        assertThat(resultDto).isEqualTo(VerificationResultDto.invalidInput("msg", 1));
    }

    @Test
    void shouldInvalidWhenResolvingError() {
        ResolvingException exception = new ResolvingException("ERROR");
        doThrow(exception).when(verificationService).getResolvedUserInputTask(INPUT);

        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        assertThat(resultDto).isEqualTo(VerificationResultDto.invalid(new Exception("Problem podczas rozwiązywania: ERROR")));
    }

    @Test
    void shouldInvalidWhenWrongTask() {
        PrintableParseException exception = mock(PrintableParseException.class);
        doThrow(exception).when(verificationService).verify(TASK, resolvedTask);

        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        assertThat(resultDto).isEqualTo(VerificationResultDto.invalidTask(OUTPUT));
    }

    @Test
    void shouldInvalidWhenExceptionDuringVerify() {
        RuntimeException exception = new RuntimeException("msg");
        doThrow(exception).when(verificationService).verify(TASK, resolvedTask);

        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        assertThat(resultDto).isEqualTo(VerificationResultDto.invalid(exception, OUTPUT));
    }

    @Test
    void shouldValidWhenVerified() {
        doNothing().when(verificationService).verify(TASK, resolvedTask);

        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        assertThat(resultDto).isEqualTo(VerificationResultDto.valid(OUTPUT));
    }
}