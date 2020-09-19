package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodCall;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodCallVisitorTest extends JavaVisitorTestBase {
    private final String METHOD_NAME = "methodName";
    private final MethodCallVisitor visitor = new MethodCallVisitor(METHOD_NAME);

    @Test
    void shouldVisitMethodCall() {
        String input = "System.out.print(\"Hello World\");";
        JavaParser.MethodCallContext c = HELPER.shouldParseToEof(input, JavaParser::methodCall);

        MethodCall methodCall = visitor.visit(c);

        assertThat(methodCall.getText()).isEqualTo(input);
        assertThat(methodCall.getContextMethodName()).isEqualTo(METHOD_NAME);
        assertThat(methodCall.getName()).isEqualTo("System.out.print");
        assertThat(Iterables.getOnlyElement(methodCall.getArgs()).getText()).isEqualTo("Hello World");
    }
}