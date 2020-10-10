package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VarDecVisitorTest extends JavaVisitorTestBase {
    private final VarDecVisitor visitor = new VarDecVisitor();

    @Test
    void shouldVisitVarDec() {
        String input = "String a = \"str\"";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        Variable variable = visitor.visit(c);

        assertThat(variable.getType()).isEqualTo("String");
        assertThat(variable.getName()).isEqualTo("a");
        assertThat(variable.getValue()).extracting(Expression::getText)
                .isEqualTo("str");
    }

    @Test
    void shouldVisitVarDecWithoutValue() {
        String input = "String a";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        Variable variable = visitor.visit(c);

        assertThat(variable.getType()).isEqualTo("String");
        assertThat(variable.getName()).isEqualTo("a");
        assertThat(variable.getValue()).isNull();
    }

    @Test
    void shouldThrowWhenInvalidDeclarationFromLiteral() {
        String input = "String a = 's'";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        assertThatThrownBy(() -> visitor.visit(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie s nie jest typu String");
    }
}