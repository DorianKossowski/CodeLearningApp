package com.server.parser.java.task;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaTaskLexer;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.task.model.MethodArgs;
import com.server.parser.java.task.model.MethodModel;
import com.server.parser.java.task.verifier.TaskVerifier;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
}