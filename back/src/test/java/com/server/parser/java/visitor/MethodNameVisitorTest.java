package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodNameVisitorTest extends JavaVisitorTestBase {
    private final MethodNameVisitor visitor = new MethodNameVisitor();

    @Test
    void shouldVisitMethodName() {
        String input = "a.b";
        JavaParser.MethodNameContext c = HELPER.shouldParseToEof(input, JavaParser::methodName);

        String name = visitor.visit(c);

        assertThat(name).isEqualTo(input);
    }
}