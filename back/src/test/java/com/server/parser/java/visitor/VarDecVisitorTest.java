package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class VarDecVisitorTest extends JavaVisitorTestBase {
    private final VarDecVisitor visitor = new VarDecVisitor();

    @Test
    void shouldVisitVarDec() {
        String input = "a = \"str\"";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        Map.Entry<String, Expression> varDecEntry = visitor.visit(c);

        assertThat(varDecEntry.getKey()).isEqualTo("a");
        assertThat(varDecEntry.getValue()).extracting(Expression::getText)
                .isEqualTo("str");
    }

    @Test
    void shouldVisitVarDecWithoutValue() {
        String input = "a";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        Map.Entry<String, Expression> varDecEntry = visitor.visit(c);

        assertThat(varDecEntry.getKey()).isEqualTo("a");
        assertThat(varDecEntry.getValue()).isNull();
    }
}