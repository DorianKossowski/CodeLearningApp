package com.server.parser.java.task;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaTaskLexer;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.task.model.*;
import com.server.parser.java.task.verifier.TaskVerifier;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
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

    static Stream<Arguments> classSource() {
        return Stream.of(
                Arguments.of("Class with name", "class with name: \"x\"", ClassModel.builder().withName("x").build()),
                Arguments.of("Class with log info", "class log info: \"t\"", ClassModel.builder().withLogInfo("t").build())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("classSource")
    void shouldVerifyClass(@SuppressWarnings("unused") String name, String input, ClassModel model) {
        JavaTaskParser.ClassRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::classRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyClass(model);
    }

    static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of("Method with name", "method with name: \"x\"",
                        MethodModel.builder().withName("x").build()),
                Arguments.of("Method with args", "method with args: {\"int\", -}, {\"String[]\", \"x\"}",
                        MethodModel.builder().withArgs(Arrays.asList(new MethodArgs("int", null),
                                new MethodArgs("String[]", "x"))).build()),
                Arguments.of("Method with modifiers", "method with modifiers: {\"x\", \"y\"}",
                        MethodModel.builder().withModifiers(Arrays.asList("x", "y")).build()),
                Arguments.of("Method with result", "method with result: \"x\"",
                        MethodModel.builder().withResult("x").build())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("methodSource")
    void shouldVerifyMethod(@SuppressWarnings("unused") String name, String input, MethodModel model) {
        JavaTaskParser.MethodRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::methodRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyMethod(model);
    }

    static Stream<Arguments> statementSource() {
        return Stream.of(
                Arguments.of("Statement in method", "statement in method: \"m\"",
                        StatementModel.builder().withMethod("m").build()),
                Arguments.of("Statement with text", "statement with text: \"t\"",
                        StatementModel.builder().withText("t").build()),
                Arguments.of("Statement with resolved", "statement with resolved: \"t\"",
                        StatementModel.builder().withResolved("t").build()),
                Arguments.of("Statement log info", "statement log info: \"t\"",
                        StatementModel.builder().withLogInfo("t").build()),
                Arguments.of("Statement with if", "statement with if: \"t\"",
                        StatementModel.builder().withIf("t").build()),
                Arguments.of("Statement with else if", "statement with else if: \"t\"",
                        StatementModel.builder().withElseIf("t").build()),
                Arguments.of("Statement in else", "statement is in else",
                        StatementModel.builder().isInElse(true).build()),
                Arguments.of("Statement with switch expression", "statement with switch expression: \"t\"",
                        StatementModel.builder().withSwitchExpr("t").build()),
                Arguments.of("Statement with switch label", "statement with switch label: \"t\"",
                        StatementModel.builder().withSwitchLabel("t").build()),
                Arguments.of("Statement with for iteration", "statement with for iteration: \"1\"",
                        StatementModel.builder().withForIteration(1).build()),
                Arguments.of("Statement with while iteration", "statement with while iteration: \"1\"",
                        StatementModel.builder().withWhileIteration(1).build()),
                Arguments.of("Statement with do while iteration", "statement with do while iteration: \"1\"",
                        StatementModel.builder().withDoWhileIteration(1).build())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("statementSource")
    void shouldVerifyStatement(@SuppressWarnings("unused") String name, String input, StatementModel model) {
        JavaTaskParser.StatementRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::statementRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyStatement(model);
    }


    static Stream<Arguments> variableSource() {
        return Stream.of(
                Arguments.of("Variable with text", "variable with text: \"t\"",
                        VariableModel.builder().withText("t").build()),
                Arguments.of("Variable log info", "variable log info: \"t\"",
                        VariableModel.builder().withLogInfo("t").build())
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("variableSource")
    void shouldVerifyVariable(@SuppressWarnings("unused") String name, String input, VariableModel model) {
        JavaTaskParser.VariableRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::variableRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyVariable(model);
    }
}