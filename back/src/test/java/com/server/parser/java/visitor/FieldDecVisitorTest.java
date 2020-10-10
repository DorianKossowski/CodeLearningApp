package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FieldDecVisitorTest extends JavaVisitorTestBase {
    private final FieldDecVisitor visitor = new FieldDecVisitor();

    @Test
    void shouldCreateFromFieldDec() {
        String input = "String a = \"str\"";
        JavaParser.FieldDecContext c = HELPER.shouldParseToEof(input, JavaParser::fieldDec);

        Variable variable = visitor.visit(c);

        assertThat(variable.getText()).isEqualTo(input);
        assertVariableDec(variable, "String", "a");
        assertThat(variable.getValue()).extracting(Expression::getText)
                .isEqualTo("str");
    }
}