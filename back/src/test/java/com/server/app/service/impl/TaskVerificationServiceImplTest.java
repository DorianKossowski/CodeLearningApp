package com.server.app.service.impl;

import com.server.app.model.dto.VerificationResultDto;
import com.server.parser.java.JavaParserAdapter;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.JavaTaskListener;
import com.server.parser.util.exception.PrintableParseException;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.*;

class TaskVerificationServiceImplTest {
    private static final String TASK = "task";
    private static final String INPUT = "input";

    private final TaskVerificationServiceImpl verificationService = new TaskVerificationServiceImpl();

    @Test
    void shouldCreateJavaTaskListener() {
        String input = "public class C {}";

        JavaTaskListener listener = verificationService.createJavaTaskListener(getTask(input));

        assertThat(listener).isNotNull();
    }

    private Task getTask(String input) {
        return JavaParserAdapter.getTask(input);
    }

    @Test
    void shouldThrowWhenBadInput() {
        String input = "public class C {\nxxx;\n}";

        PrintableParseException exception = catchThrowableOfType(() -> verificationService.createJavaTaskListener(getTask(input)),
                PrintableParseException.class);

        assertThat(exception.getLineNumber()).isEqualTo(2);
        assertThat(exception.getMessage()).isEqualTo("Problem podczas parsowania: ; [2:3]");
    }

    @Test
    void shouldCreateJavaTaskParser() {
        String task = "-> method with name: \"main\"";

        JavaTaskParser.RulesEOFContext context = verificationService.createJavaTaskRulesContext(task);

        assertThat(context).isNotNull();
    }

    @Test
    void shouldThrowWhenBadTaskInput() {
        String task = "-> method\nwith: \"main\"";

        PrintableParseException exception = catchThrowableOfType(() -> verificationService.createJavaTaskRulesContext(task),
                PrintableParseException.class);

        assertThat(exception.getLineNumber()).isEqualTo(2);
        assertThat(exception.getMessage()).isEqualTo("Problem podczas parsowania: : [2:4]");
    }

    @Test
    void shouldInvalidWhenWrongTask() {
        // given
        TaskVerificationServiceImpl verificationService = spy(new TaskVerificationServiceImpl());
        PrintableParseException exception = mock(PrintableParseException.class);
        doThrow(exception).when(verificationService).createJavaTaskRulesContext(TASK);

        // when
        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        // then
        assertThat(resultDto).isEqualTo(VerificationResultDto.invalidTask());
    }

    @Test
    void shouldInvalidWhenWrongInput() {
        // given
        TaskVerificationServiceImpl verificationService = spy(new TaskVerificationServiceImpl());
        PrintableParseException exception = mock(PrintableParseException.class);
        when(exception.getMessage()).thenReturn("msg");
        when(exception.getLineNumber()).thenReturn(1);

        Task task = mock(Task.class);
        doReturn(task).when(verificationService).getTask(INPUT);
        doReturn(mock(JavaTaskParser.RulesEOFContext.class)).when(verificationService).createJavaTaskRulesContext(TASK);
        doThrow(exception).when(verificationService).createJavaTaskListener(task);

        // when
        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        // then
        assertThat(resultDto).isEqualTo(VerificationResultDto.invalidInput("msg", 1));
    }

    @Test
    void shouldInvalidWhenResolvingError() {
        // given
        TaskVerificationServiceImpl verificationService = spy(new TaskVerificationServiceImpl());
        ResolvingException exception = new ResolvingException("ERROR");

        Task task = mock(Task.class);
        doReturn(task).when(verificationService).getTask(INPUT);
        doReturn(mock(JavaTaskParser.RulesEOFContext.class)).when(verificationService).createJavaTaskRulesContext(TASK);
        doThrow(exception).when(verificationService).createJavaTaskListener(task);

        // when
        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        // then
        assertThat(resultDto).isEqualTo(VerificationResultDto.invalid(new Exception("Problem podczas rozwiązywania: ERROR")));
    }

    @Test
    void shouldInvalidWhenExceptionDuringVerify() {
        // given
        TaskVerificationServiceImpl verificationService = spy(new TaskVerificationServiceImpl());
        PrintableParseException exception = mock(PrintableParseException.class);
        when(exception.getMessage()).thenReturn("msg");

        JavaTaskParser.RulesEOFContext context = mock(JavaTaskParser.RulesEOFContext.class);
        doReturn(context).when(verificationService).createJavaTaskRulesContext(TASK);
        JavaTaskListener listener = mock(JavaTaskListener.class);
        Task task = mock(Task.class);
        doReturn(task).when(verificationService).getTask(INPUT);
        doReturn(listener).when(verificationService).createJavaTaskListener(task);
        doThrow(exception).when(verificationService).verify(context, listener);

        // when
        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        // then
        assertThat(resultDto).isEqualTo(VerificationResultDto.invalid(new Exception("msg")));
    }

    @Test
    void shouldValidWhenVerified() {
        // given
        TaskVerificationServiceImpl verificationService = spy(new TaskVerificationServiceImpl());

        JavaTaskParser.RulesEOFContext context = mock(JavaTaskParser.RulesEOFContext.class);
        doReturn(context).when(verificationService).createJavaTaskRulesContext(TASK);
        JavaTaskListener listener = mock(JavaTaskListener.class);
        Task task = mock(Task.class);
        doReturn(task).when(verificationService).getTask(INPUT);
        doReturn(listener).when(verificationService).createJavaTaskListener(task);
        doNothing().when(verificationService).verify(context, listener);

        // when
        VerificationResultDto resultDto = verificationService.verify(TASK, INPUT);

        // then
        assertThat(resultDto).isEqualTo(VerificationResultDto.valid(""));
    }
}