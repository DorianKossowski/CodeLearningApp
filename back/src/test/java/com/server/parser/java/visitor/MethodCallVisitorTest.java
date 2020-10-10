package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodCall;
import com.server.parser.java.ast.ObjectRef;
import com.server.parser.java.context.MethodContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class MethodCallVisitorTest extends JavaVisitorTestBase {
    private final String METHOD_NAME = "methodName";
    private MethodContext methodContext;
    private MethodCallVisitor visitor;

    @BeforeEach
    void setUp() {
        methodContext = new MethodContext(METHOD_NAME);
        visitor = new MethodCallVisitor(methodContext);
    }

    @Test
    void shouldVisitMethodCall() {
        String input = "System.out.print(\"Hello World\")";
        JavaParser.MethodCallContext c = HELPER.shouldParseToEof(input, JavaParser::methodCall);

        MethodCall methodCall = visitor.visit(c);

        assertThat(methodCall.getText()).isEqualTo(input);
        assertThat(methodCall.getContextMethodName()).isEqualTo(METHOD_NAME);
        assertThat(methodCall.getName()).isEqualTo("System.out.print");
        assertThat(Iterables.getOnlyElement(methodCall.getArgs()).getText()).isEqualTo("Hello World");
    }

    @Test
    void shouldEnhanceObjectRefs() {
        ObjectRef objectRef = new ObjectRef("obj");
        methodContext.addVar("obj", "val");

        visitor.enhanceArguments(Collections.singletonList(objectRef));

        assertThat(objectRef.getResolved()).isEqualTo("\"val\"");
    }

    @Test
    void shouldGetCorrectMethodCallValue() {
        methodContext.addVar("var", "value");
        String input = "System.out.print(\"literal\", var)";
        JavaParser.MethodCallContext c = HELPER.shouldParseToEof(input, JavaParser::methodCall);

        MethodCall methodCall = visitor.visit(c);

        assertThat(methodCall.getResolved()).isEqualTo("System.out.print(\"literal\", \"value\")");
    }
}