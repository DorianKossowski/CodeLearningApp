package com.server.parser.java.visitor.resolver;

import com.google.common.collect.Iterables;
import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.BreakExprStatement;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.ContextParameters;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ForStmtResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext javaContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldIterateWhenLackOfCondition() {
        JavaParser.ForStatementContext forContext = mock(JavaParser.ForStatementContext.class);

        assertThat(ForStmtResolver.shouldIterate(javaContext, forContext)).isTrue();
    }

    @Test
    void shouldBreakIn() {
        ClassContext context = createClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        methodContext.save(new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList()),
                mock(JavaParser.MethodBodyContext.class));
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(;;) { break; int sth; }",
                JavaParser::forStatement);

        List<Statement> statements = ForStmtResolver.resolveContent(methodContext, c, methodContext.getVisitor(Statement.class));
        Statement statement = Iterables.getOnlyElement(statements);
        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isSameAs(BreakExprStatement.INSTANCE);
    }

    private ClassContext createClassContext() {
        ClassContext context = new ClassContext();
        context.setParameters(ContextParameters.createClassContextParameters(""));
        return context;
    }

    @Test
    void shouldThrowWhenInfinityLoop() {
        ClassContext context = createClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        methodContext.save(new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList()),
                mock(JavaParser.MethodBodyContext.class));
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(int i=0; i<1001; i=i+1);",
                JavaParser::forStatement);

        assertThatThrownBy(() -> ForStmtResolver.resolve(methodContext, c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Ogranicz liczbę iteracji! Maksymalna dostępna liczba " +
                        "iteracji w pętli to 1000");
    }
}