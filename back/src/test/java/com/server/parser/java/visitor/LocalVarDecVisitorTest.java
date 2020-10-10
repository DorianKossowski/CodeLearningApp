package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.context.MethodContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class LocalVarDecVisitorTest extends JavaVisitorTestBase {
    private LocalVarDecVisitor visitor;
    private MethodContext methodContext;

    @BeforeEach
    void setUp() {
        methodContext = new MethodContext("");
        visitor = new LocalVarDecVisitor(methodContext);
    }

    @Test
    void shouldVisitLocalVarDec() {
        String input = "String a = \"str\"";
        JavaParser.LocalVarDecContext c = HELPER.shouldParseToEof(input, JavaParser::localVarDec);

        Variable variable = visitor.visit(c);

        assertThat(variable.getText()).isEqualTo(input);
        assertVariableDec(variable, "String", "a");
        assertThat(variable.getValue()).extracting(Expression::getText)
                .isEqualTo("\"str\"");

        assertThat(methodContext.getVarToValue()).containsExactly(entry("a", "\"str\""));
    }
}