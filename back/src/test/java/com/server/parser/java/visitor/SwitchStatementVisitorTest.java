package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.SwitchStatement;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.constant.BooleanConstant;
import com.server.parser.java.constant.StringConstant;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.ContextParameters;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.value.ObjectWrapperValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SwitchStatementVisitorTest extends JavaVisitorTestBase {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext javaContext;
    @Mock
    private JavaParser.ExpressionContext expressionContext;

    private SwitchStatementVisitor visitor;

    @Override
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        visitor = new SwitchStatementVisitor(javaContext);
    }

    @Test
    void shouldResolveExpression() {
        Expression condition = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("str")));
        when(condition.getValue()).thenReturn(value);
        when(javaContext.resolveExpression(expressionContext)).thenReturn(condition);

        Value resolvedValue = visitor.resolveExpression(expressionContext);

        assertThat(resolvedValue).isSameAs(value);
    }

    @Test
    void shouldThrowDuringResolvingExpression() {
        Expression expression = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new BooleanConstant(true)));
        when(expression.getValue()).thenReturn(value);
        when(javaContext.resolveExpression(expressionContext)).thenReturn(expression);

        assertThatThrownBy(() -> visitor.resolveExpression(expressionContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: W instrukcji switch: true nie jest jednego z typów: char," +
                        " byte, short, int, Character, Byte, Short, Integer, String");
    }

    @Test
    void shouldResolveCaseLabelExpression() {
        String input = "case \"str\":";
        JavaParser.SwitchElementLabelContext c = HELPER.shouldParseToEof(input, JavaParser::switchElementLabel);
        Expression expression = mock(Expression.class);
        when(javaContext.resolveExpression(c.expression())).thenReturn(expression);

        List<Expression> labelExpressions = visitor.resolveLabelExpressions(Collections.singletonList(c));

        assertThat(Iterables.getOnlyElement(labelExpressions)).isSameAs(expression);
    }

    @Test
    void shouldResolveDefaultLabelExpression() {
        String input = "default:";
        JavaParser.SwitchElementLabelContext c = HELPER.shouldParseToEof(input, JavaParser::switchElementLabel);

        List<Expression> labelExpressions = visitor.resolveLabelExpressions(Collections.singletonList(c));

        assertThat(Iterables.getOnlyElement(labelExpressions)).isNull();
    }

    @Test
    void shouldResolveSwitchElement() {
        String input = "case 1:default: fun(); ";
        JavaParser.SwitchElementContext c = HELPER.shouldParseToEof(input, JavaParser::switchElement);

        SwitchStatementVisitor.SwitchElement switchElement = new SwitchStatementVisitor(createRealMethodContext()).resolveSwitchElement(c);

        List<Expression> labelExpressions = switchElement.getLabelExpressions();
        assertThat(labelExpressions).hasSize(2);
        assertThat(labelExpressions.get(0)).isExactlyInstanceOf(Literal.class);
        assertThat(labelExpressions.get(1)).isNull();
        assertThat(switchElement.getStatementsContext().getText()).isEqualTo("fun();");
    }

    @Test
    void shouldThrowWhenDuplicatedDefault() {
        assertThatThrownBy(() -> visitor.validateLabels(Collections.singletonList(Arrays.asList(null, null))))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Zduplikowana etykieta default w instrukcji switch");
    }

    @Test
    void shouldThrowWhenDuplicatedLabels() {
        Expression expression = mock(Expression.class);
        when(expression.getResolvedText()).thenReturn("text");
        assertThatThrownBy(() -> visitor.validateLabels(Collections.singletonList(Arrays.asList(expression, expression))))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Zduplikowana etykieta text w instrukcji switch");
    }


    static Stream<Arguments> isLabelFulfilledProvider() {
        return Stream.of(
                Arguments.of(new ObjectWrapperValue(new Literal(new StringConstant("str"))), true),
                Arguments.of(new ObjectWrapperValue(new Literal(new StringConstant("str2"))), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isLabelFulfilledProvider")
    void shouldCheckIfLabelIsFulfilled(Value value, boolean expectedFulfilled) {
        List<Expression> labelExpressions = Arrays.asList(new Literal(new StringConstant("str")), null);
        SwitchStatementVisitor.SwitchElement switchElement = new SwitchStatementVisitor.SwitchElement(labelExpressions,
                mock(JavaParser.StatementsContext.class));

        boolean labelFulfilled = visitor.isLabelFulfilled(value, switchElement);

        assertThat(labelFulfilled).isEqualTo(expectedFulfilled);
    }

    @Test
    void shouldResolveStatements() {
        String input = "switch(1) { " +
                "case 1: case 2: System.out.print(\"1\"); break; System.out.print(\"2\");" +
                "default: System.out.print(\"D\"); " +
                "}";
        JavaParser.SwitchStatementContext switchCtx = HELPER.shouldParseToEof(input, JavaParser::switchStatement);

        SwitchStatement statement = new SwitchStatementVisitor(createRealMethodContext()).visit(switchCtx);

        List<ExpressionStatement> expressionStatements = statement.getExpressionStatements();
        assertThat(expressionStatements).extracting(ExpressionStatement::getText,
                stmt -> stmt.getProperty(StatementProperties.SWITCH_EXPRESSION),
                stmt -> stmt.getProperty(StatementProperties.SWITCH_LABELS)
        ).containsExactly(tuple("System.out.print(\"1\")", "1", "1,2"), tuple("break", "1", "1,2"));
    }

    private MethodContext createRealMethodContext() {
        ClassContext context = new ClassContext();
        context.setParameters(ContextParameters.createClassContextParameters(""));
        MethodContext methodContext = context.createEmptyMethodContext();
        MethodHeader methodHeader = new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList());
        methodContext.save(methodHeader, mock(JavaParser.MethodBodyContext.class));
        return methodContext;
    }

    @Test
    void shouldValidateInSeparateContext() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("init")));
        methodContext.addVariable(new MethodVar("String", "str", value));

        JavaParser.StatementsContext c = HELPER.shouldParseToEof("str=null;", JavaParser::statements);

        new SwitchStatementVisitor(methodContext).validateStatementLists(Collections.singletonList(c));

        assertThat(methodContext.getVariable("str").getValue()).isSameAs(value);
    }
}