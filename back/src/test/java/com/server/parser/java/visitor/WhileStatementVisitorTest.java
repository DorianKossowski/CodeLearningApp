package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.BreakExprStatement;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WhileStatementVisitorTest extends JavaVisitorTestBase {
    private WhileStatementVisitor visitor;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        visitor = new WhileStatementVisitor(createMethodContext());
    }

    @Test
    void shouldBreakIn() {
        JavaParser.WhileStatementContext c = HELPER.shouldParseToEof("while(true) { break; int sth; }",
                JavaParser::whileStatement);

        List<Statement> statements = visitor.resolveContent(c);
        Statement statement = Iterables.getOnlyElement(statements);
        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isSameAs(BreakExprStatement.INSTANCE);
    }

    @Test
    void shouldThrowWhenInfinityLoop() {
        JavaParser.WhileStatementContext c = HELPER.shouldParseToEof("while(true);", JavaParser::whileStatement);

        assertThatThrownBy(() -> visitor.visit(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Ogranicz liczbę iteracji! Maksymalna dostępna liczba " +
                        "iteracji w pętli to 1000");
    }
}