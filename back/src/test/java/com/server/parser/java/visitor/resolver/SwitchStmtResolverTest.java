package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SwitchStmtResolverTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext javaContext;
    @Mock
    private JavaParser.ExpressionContext expressionContext;

    private SwitchStmtResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        resolver = new SwitchStmtResolver(javaContext, mock(StatementVisitor.StatementVisitorInternal.class));
    }

    @Test
    void shouldResolveExpression() {
        Expression condition = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("str")));
        when(condition.getValue()).thenReturn(value);
        when(javaContext.getVisitor(Expression.class).visit(expressionContext, javaContext)).thenReturn(condition);

        Value resolvedValue = resolver.resolveExpression(expressionContext);

        assertThat(resolvedValue).isSameAs(value);
    }

    @Test
    void shouldThrowDuringResolvingExpression() {
        Expression expression = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new BooleanConstant(true)));
        when(expression.getValue()).thenReturn(value);
        when(javaContext.getVisitor(Expression.class).visit(expressionContext, javaContext)).thenReturn(expression);

        assertThatThrownBy(() -> resolver.resolveExpression(expressionContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: true nie jest jednego z typów: char, byte, short, int, " +
                        "Character, Byte, Short, Integer, String");
    }
}