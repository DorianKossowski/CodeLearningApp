package com.server.parser.java.task;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaTaskLexer;
import com.server.parser.java.JavaTaskParser;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void shouldVerifyMethod() {
        String input = "method with name: \"x\"";
        JavaTaskParser.MethodRuleContext c = HELPER.shouldParseToEof(input, JavaTaskParser::methodRule);

        WALKER.walk(listener, c);

        verify(verifier).verifyMethod(MethodModel.builder().withName("x").build());
    }
}