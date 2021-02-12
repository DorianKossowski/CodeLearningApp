package com.server.app.util;

import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OutputPreparerTest {

    @Test
    void shouldPrepareOutput() {
        // given
        CallInvocation invocation1 = mockCallInvocation("System.out.println", "TEXT");
        CallStatement call1 = new CallStatement(invocation1, Collections.emptyList());

        CallInvocation invocation2 = mockCallInvocation("System.out.print", "SOME ");
        CallStatement call2 = new CallStatement(invocation2, Collections.emptyList());

        CallInvocation invocation3 = mockCallInvocation("someMethod", "");
        CallStatement call3 = new CallStatement(invocation3, Collections.emptyList());

        CallInvocation invocation4 = mockCallInvocation("System.out.println", "TEXT2");
        CallStatement call4 = new CallStatement(invocation4, Collections.emptyList());

        List<Statement> stmts = Arrays.asList(call1, call2, call3, call4);
        Task task = new Task(mock(ClassAst.class), stmts);

        // then
        assertThat(OutputPreparer.prepare(task)).isEqualTo(String.format("TEXT%sSOME TEXT2%s", System.lineSeparator(),
                System.lineSeparator()));
    }

    private CallInvocation mockCallInvocation(String name, String exprOutput) {
        CallInvocation invocation = mock(CallInvocation.class);
        when(invocation.getName()).thenReturn(name);
        Expression text1 = mockExpressionWithOutput(exprOutput);
        when(invocation.getArgs()).thenReturn(Collections.singletonList(text1));
        return invocation;
    }

    private Expression mockExpressionWithOutput(String text) {
        Expression expression = mock(Expression.class);
        when(expression.getOutput()).thenReturn(text);
        return expression;
    }
}