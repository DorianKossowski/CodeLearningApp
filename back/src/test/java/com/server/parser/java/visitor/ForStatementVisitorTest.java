package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.BreakExprStatement;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ForStatementVisitorTest extends JavaVisitorTestBase {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    private ForStatementVisitor visitor;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        visitor = new ForStatementVisitor(createMethodContext());
    }

    @Test
    void shouldIterateWhenLackOfCondition() {
        JavaParser.ForStatementContext forContext = mock(JavaParser.ForStatementContext.class);

        assertThat(visitor.shouldIterate(forContext)).isTrue();
    }

    @Test
    void shouldBreakIn() {
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(;;) { break; int sth; }",
                JavaParser::forStatement);

        List<Statement> statements = visitor.resolveContent(c);
        Statement statement = Iterables.getOnlyElement(statements);
        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isSameAs(BreakExprStatement.INSTANCE);
    }

    @Test
    void shouldThrowWhenInfinityLoop() {
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(int i=0; i<1001; i=i+1);",
                JavaParser::forStatement);

        assertThatThrownBy(() -> visitor.visit(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Ogranicz liczbę iteracji! Maksymalna dostępna liczba " +
                        "iteracji w pętli to 1000");
    }
}