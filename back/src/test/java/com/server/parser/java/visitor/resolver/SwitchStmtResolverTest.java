package com.server.parser.java.visitor.resolver;

import com.google.common.collect.Iterables;
import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SwitchStmtResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext javaContext;
    @Mock
    private JavaParser.ExpressionContext expressionContext;

    private SwitchStmtResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        resolver = new SwitchStmtResolver(javaContext);
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

    @Test
    void shouldResolveCaseLabelExpression() {
        String input = "case \"str\":";
        JavaParser.SwitchElementLabelContext c = HELPER.shouldParseToEof(input, JavaParser::switchElementLabel);
        Expression expression = mock(Expression.class);
        when(javaContext.getVisitor(Expression.class).visit(c.expression(), javaContext)).thenReturn(expression);

        List<Expression> labelExpressions = resolver.resolveLabelExpressions(Collections.singletonList(c));

        assertThat(Iterables.getOnlyElement(labelExpressions)).isSameAs(expression);
    }

    @Test
    void shouldResolveDefaultLabelExpression() {
        String input = "default:";
        JavaParser.SwitchElementLabelContext c = HELPER.shouldParseToEof(input, JavaParser::switchElementLabel);

        List<Expression> labelExpressions = resolver.resolveLabelExpressions(Collections.singletonList(c));

        assertThat(Iterables.getOnlyElement(labelExpressions)).isNull();
    }

    @Test
    void shouldResolveSwitchElement() {
        String input = "case 1:default: fun(); ";
        JavaParser.SwitchElementContext c = HELPER.shouldParseToEof(input, JavaParser::switchElement);
        SwitchStmtResolver spyResolver = createSpyResolverFromReal();
        Expression expression = mock(Expression.class);
        doReturn(Arrays.asList(expression, null)).when(spyResolver).resolveLabelExpressions(c.switchElementLabel());

        SwitchStmtResolver.SwitchElement switchElement = spyResolver.resolveSwitchElement(c);

        List<Expression> labelExpressions = switchElement.getLabelExpressions();
        assertThat(labelExpressions).hasSize(2);
        assertThat(labelExpressions.get(0)).isSameAs(expression);
        assertThat(labelExpressions.get(1)).isNull();
        assertThat(Iterables.getOnlyElement(switchElement.getStatements()).getText()).isEqualTo("fun()");
    }

    private SwitchStmtResolver createSpyResolverFromReal() {
        JavaContext javaContext = new JavaContext();
        javaContext.createCurrentMethodContext("");
        return spy(new SwitchStmtResolver(javaContext));
    }

    @Test
    void shouldThrowWhenDuplicatedDefault() {
        assertThatThrownBy(() -> resolver.validateLabels(Collections.singletonList(Arrays.asList(null, null))))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Zduplikowana etykieta default w instrukcji switch");
    }

    @Test
    void shouldThrowWhenDuplicatedLabels() {
        Expression expression = mock(Expression.class);
        when(expression.getResolvedText()).thenReturn("text");
        assertThatThrownBy(() -> resolver.validateLabels(Collections.singletonList(Arrays.asList(expression, expression))))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Zduplikowana etykieta text w instrukcji switch");
    }
}