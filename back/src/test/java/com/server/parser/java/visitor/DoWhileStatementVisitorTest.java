package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.BreakExprStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DoWhileStatementVisitorTest extends JavaVisitorTestBase {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    private DoWhileStatementVisitor visitor;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        visitor = new DoWhileStatementVisitor(createMethodContext());
    }

    @Test
    void shouldMakeAtLeastOneIteration() {
        JavaParser.DoWhileStatementContext c = HELPER.shouldParseToEof("do { System.out.print(\"\"); } while(false);",
                JavaParser::doWhileStatement);

        List<Statement> statements = visitor.resolveContent(c);
        Statement statement = Iterables.getOnlyElement(statements);
        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isExactlyInstanceOf(CallInvocation.class);
    }

    @Test
    void shouldBreakIn() {
        JavaParser.DoWhileStatementContext c = HELPER.shouldParseToEof("do { break; int sth; } while(true);",
                JavaParser::doWhileStatement);

        List<Statement> statements = visitor.resolveContent(c);
        Statement statement = Iterables.getOnlyElement(statements);
        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isSameAs(BreakExprStatement.INSTANCE);
    }

    @Test
    void shouldThrowWhenInfinityLoop() {
        JavaParser.DoWhileStatementContext c = HELPER.shouldParseToEof("do {} while(true);",
                JavaParser::doWhileStatement);

        assertThatThrownBy(() -> visitor.visit(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Ogranicz liczbę iteracji! Maksymalna dostępna liczba " +
                        "iteracji w pętli to 1000");
    }
}