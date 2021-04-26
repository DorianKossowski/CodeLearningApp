package com.server.parser.java.call.executor;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.statement.expression_statement.ReturnExprStatement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.constant.IntConstant;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.exception.InvalidReturnedExpressionException;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.server.parser.java.call.executor.CallExecutor.MAX_EXECUTION_LEVEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CallExecutorTest {
    @Mock
    private JavaContext context;

    private CallExecutor executor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = mock(CallExecutor.class, CALLS_REAL_METHODS);
    }

    @Test
    void shouldThrowWhenTooManyExecutions() {
        for (int i = 0; i <= MAX_EXECUTION_LEVEL; ++i) {
            executor.preExecution();
        }

        assertThatThrownBy(() -> executor.preExecution())
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Przekroczono ilość dopuszczalnych zagnieżdżonych wywołań równą: 10");
    }

    @Test
    void shouldAssignParameters() {
        String name = "name";
        VariableDef variableDef = mock(VariableDef.class);
        when(variableDef.getName()).thenReturn(name);
        List<VariableDef> arguments = Collections.singletonList(variableDef);
        Expression expression = mock(Expression.class);
        List<Expression> invocationParameters = Collections.singletonList(expression);

        executor.assignInvocationParameters(arguments, invocationParameters, context);

        verify(context).updateVariable(name, expression);
    }

    static Stream<Arguments> statementsForReturnProvider() {
        Expression expression = mock(Expression.class);
        CallStatement callStatement = new CallStatement(mock(CallInvocation.class),
                Collections.singletonList(new ReturnExprStatement("", expression)), expression);
        return Stream.of(
                Arguments.of(Collections.singletonList(new ReturnExprStatement("", expression)), expression),
                Arguments.of(Collections.emptyList(), VoidExpression.INSTANCE),
                Arguments.of(Collections.singletonList(callStatement), VoidExpression.INSTANCE)
        );
    }

    @ParameterizedTest
    @MethodSource("statementsForReturnProvider")
    void shouldGetReturnedExpression(List<Statement> statements, Expression returnedExpression) {
        assertThat(executor.getReturnedExpression(statements)).isSameAs(returnedExpression);
    }

    @Test
    void shouldThrowWhenInvalidReturnedVoidExpression() {
        assertThatThrownBy(() -> executor.validateReturnedExpression("String", VoidExpression.INSTANCE))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Brak odpowiedniej instrukcji zwracającej");
    }

    @Test
    void shouldThrowWhenInvalidReturnedExpression() {
        assertThatThrownBy(() -> executor.validateReturnedExpression("String", new Literal(new IntConstant(10))))
                .isExactlyInstanceOf(InvalidReturnedExpressionException.class)
                .hasMessage("Problem podczas rozwiązywania: Zwracany element 10 nie jest typu String");
    }
}