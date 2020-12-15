package com.server.parser.java.visitor.resolver;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.ForStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ForStmtResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext javaContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldVisitForElements() {
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(int i=0; i<1; i = i+1);",
                JavaParser::forStatement);
        JavaVisitor<Statement> statementVisitor = mock(JavaVisitor.class);
        JavaVisitor<Expression> expressionVisitor = mock(JavaVisitor.class);
        when(javaContext.getVisitor(Statement.class)).thenReturn(statementVisitor);
        when(javaContext.getVisitor(Expression.class)).thenReturn(expressionVisitor);
        when(expressionVisitor.visit(c.condExpr, javaContext))
                .thenReturn(new Literal(new BooleanConstant(true)))
                .thenReturn(new Literal(new BooleanConstant(false)));

        ForStatement forStatement = ForStmtResolver.resolve(javaContext, c);

        assertThat(forStatement.getExpressionStatements()).isEmpty();
        verify(statementVisitor).visit(c.initExpr, javaContext);
        verify(expressionVisitor, times(2)).visit(c.condExpr, javaContext);
        verify(statementVisitor).visit(c.updateExpr, javaContext);
    }

    @Test
    void shouldIterateWhenLackOfCondition() {
        JavaParser.ForStatementContext forContext = mock(JavaParser.ForStatementContext.class);

        assertThat(ForStmtResolver.shouldIterate(javaContext, forContext)).isTrue();
    }

    @Test
    void shouldThrowWhenNotLogic() {
        JavaParser.ExpressionContext expressionContext = mock(JavaParser.ExpressionContext.class);
        Expression condition = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("str")));
        when(condition.getValue()).thenReturn(value);
        when(javaContext.getVisitor(Expression.class).visit(expressionContext, javaContext)).thenReturn(condition);

        assertThatThrownBy(() -> ForStmtResolver.resolveCondition(javaContext, expressionContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: \"str\" nie jest typu logicznego");
    }

}