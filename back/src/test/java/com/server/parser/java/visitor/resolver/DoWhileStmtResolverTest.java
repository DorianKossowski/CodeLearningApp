package com.server.parser.java.visitor.resolver;

import com.google.common.collect.Iterables;
import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.statement.MethodCall;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DoWhileStmtResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Test
    void shouldMakeAtLeastOneIteration() {
        MethodContext methodContext = createMethodContext();
        JavaParser.DoWhileStatementContext c = HELPER.shouldParseToEof("do { fun(); } while(false);",
                JavaParser::doWhileStatement);

        List<Statement> statements = DoWhileStmtResolver.resolveContent(methodContext, c, methodContext.getVisitor(Statement.class));
        Statement statement = Iterables.getOnlyElement(statements);
        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isExactlyInstanceOf(MethodCall.class);
    }

    private MethodContext createMethodContext() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        methodContext.save(new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList()));
        return methodContext;
    }

    @Test
    void shouldBreakIn() {
        MethodContext methodContext = createMethodContext();
        JavaParser.DoWhileStatementContext c = HELPER.shouldParseToEof("do { break; fun(); } while(true);",
                JavaParser::doWhileStatement);

        List<Statement> statements = DoWhileStmtResolver.resolveContent(methodContext, c, methodContext.getVisitor(Statement.class));
        Statement statement = Iterables.getOnlyElement(statements);
        assertThat(statement.getExpressionStatements()).isEmpty();
    }

    @Test
    void shouldThrowWhenInfinityLoop() {
        MethodContext methodContext = createMethodContext();
        JavaParser.DoWhileStatementContext c = HELPER.shouldParseToEof("do {} while(true);",
                JavaParser::doWhileStatement);

        assertThatThrownBy(() -> DoWhileStmtResolver.resolve(methodContext, c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Ogranicz liczbę iteracji! Maksymalna dostępna liczba " +
                        "iteracji w pętli to 1000");
    }
}