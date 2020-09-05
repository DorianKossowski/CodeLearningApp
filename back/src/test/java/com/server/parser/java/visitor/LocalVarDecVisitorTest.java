package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocalVarDecVisitorTest extends JavaVisitorTestBase {
    private final LocalVarDecVisitor visitor = new LocalVarDecVisitor();

    @Test
    void shouldVisitLocalVarDec() {
        String input = "final String a = \"str\";";
        JavaParser.LocalVarDecContext c = HELPER.shouldParseToEof(input, JavaParser::localVarDec);

        Variable variable = visitor.visit(c);

        assertVariableDec(variable, "String", "a");
        assertThat(variable.getValue()).extracting(Expression::getText)
                .isEqualTo("str");
    }
}