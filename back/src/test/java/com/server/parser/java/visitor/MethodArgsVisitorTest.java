package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MethodArgsVisitorTest extends JavaVisitorTestBase {
    private final MethodArgsVisitor visitor = new MethodArgsVisitor();

    @Test
    void shouldVisitMethodArgs() {
        String input = "(Integer[] a, double b, int c)";
        JavaParser.MethodArgsContext c = HELPER.shouldParseToEof(input, JavaParser::methodArgs);

        List<Variable> variables = visitor.visit(c);

        assertThat(variables).hasSize(3);
        assertVariableDec(variables.get(0), "Integer[]", "a");
        assertVariableDec(variables.get(1), "double", "b");
        assertVariableDec(variables.get(2), "int", "c");
    }

    @Test
    void shouldVisitMethodArgsWhenEmpty() {
        String input = "()";
        JavaParser.MethodArgsContext c = HELPER.shouldParseToEof(input, JavaParser::methodArgs);

        List<Variable> variables = visitor.visit(c);

        assertThat(variables).isEmpty();
    }
}