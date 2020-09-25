package com.server.app.service.impl;

import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.task.JavaTaskListener;
import com.server.parser.util.exception.PrintableParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class TaskVerificationServiceImplTest {

    private final TaskVerificationServiceImpl verificationService = new TaskVerificationServiceImpl();

    @Test
    void shouldCreateJavaTaskListener() {
        String input = "public class C {}";

        JavaTaskListener listener = verificationService.createJavaTaskListener(input);

        assertThat(listener).isNotNull();
    }

    @Test
    void shouldThrowWhenBadInput() {
        String input = "public class C {\nxxx;\n}";

        PrintableParseException exception = catchThrowableOfType(() -> verificationService.createJavaTaskListener(input),
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
}