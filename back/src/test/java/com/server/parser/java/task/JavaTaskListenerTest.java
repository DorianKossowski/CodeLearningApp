package com.server.parser.java.task;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaTaskLexer;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.task.model.MethodArgs;
import com.server.parser.java.task.model.MethodModel;
import com.server.parser.java.task.model.StatementModel;
import com.server.parser.java.task.verifier.TaskVerifier;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JavaTaskListenerTest {
    private static final ParserTestHelper<JavaTaskParser> HELPER = new ParserTestHelper<>(JavaTaskLexer::new,
            JavaTaskParser::new);
    private static final ParseTreeWalker WALKER = new ParseTreeWalker();

    private JavaTaskListener listener;
    private TaskVerifier verifier;

    @BeforeEach
    void setUp() {
        verifier = mock(TaskVerifier.class);
        listener = new JavaTaskListener(verifier);
    }

    @Test
    void shouldVerifyMethodName() {
        String input = "method with name: \"x\"";
        JavaTaskParser.MethodRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::methodRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyMethod(MethodModel.builder().withName("x").build());
    }

    @Test
    void shouldVerifyMethodArgs() {
        String input = "method with args: {\"int\", -}, {\"String[]\", \"x\"}";
        JavaTaskParser.MethodRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::methodRule);

        WALKER.walk(listener, c);

        List<MethodArgs> methodArgs = Arrays.asList(new MethodArgs("int", null), new MethodArgs("String[]", "x"));
        verify(verifier).verifyMethod(MethodModel.builder().withArgs(methodArgs).build());
    }

    @Test
    void shouldVerifyModifiers() {
        String input = "method with modifiers: {\"x\", \"y\"}";
        JavaTaskParser.MethodRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::methodRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyMethod(MethodModel.builder().withModifiers(Arrays.asList("x", "y")).build());
    }

    @Test
    void shouldVerifyResult() {
        String input = "method with result: \"x\"";
        JavaTaskParser.MethodRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::methodRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyMethod(MethodModel.builder().withResult("x").build());
    }

    static Stream<Arguments> statementSource() {
        return Stream.of(
                Arguments.of("Statement in method", "statement in method: \"m\"",
                        StatementModel.builder().withMethod("m").build()),
                Arguments.of("Statement with text", "statement with text: \"t\"",
                        StatementModel.builder().withText("t").build()),
                Arguments.of("Statement with resolved", "statement with resolved: \"t\"",
                        StatementModel.builder().withResolved("t").build()),
                Arguments.of("Statement error message", "statement error message: \"t\"",
                        StatementModel.builder().withErrorMessage("t").build())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("statementSource")
    void shouldVerifyStatement(@SuppressWarnings("unused") String name, String input, StatementModel model) {
        JavaTaskParser.StatementRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::statementRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyStatement(model);
    }
}