package com.server.app.util;

import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.MethodCall;
import com.server.parser.java.ast.statement.Statement;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OutputPreparerTest {

    @Test
    void shouldPrepareOutput() {
        // given
        MethodCall call1 = mock(MethodCall.class);
        doCallRealMethod().when(call1).getExpressionStatements();
        when(call1.getName()).thenReturn("System.out.println");
        Expression text1 = mockExpressionWithOutput("TEXT");
        when(call1.getArgs()).thenReturn(Collections.singletonList(text1));

        MethodCall call2 = mock(MethodCall.class);
        doCallRealMethod().when(call2).getExpressionStatements();
        when(call2.getName()).thenReturn("System.out.print");
        Expression text2 = mockExpressionWithOutput("SOME ");
        when(call2.getArgs()).thenReturn(Collections.singletonList(text2));

        MethodCall call3 = mock(MethodCall.class);
        doCallRealMethod().when(call3).getExpressionStatements();
        when(call3.getName()).thenReturn("someMethod");

        MethodCall call4 = mock(MethodCall.class);
        doCallRealMethod().when(call4).getExpressionStatements();
        when(call4.getName()).thenReturn("System.out.println");
        Expression text4 = mockExpressionWithOutput("TEXT2");
        when(call4.getArgs()).thenReturn(Collections.singletonList(text4));

        List<Statement> stmts = Arrays.asList(call1, call2, call3, call4);
        Task task = new Task(mock(ClassAst.class), stmts);

        // then
        assertThat(OutputPreparer.prepare(task)).isEqualTo(String.format("TEXT%sSOME TEXT2%s", System.lineSeparator(),
                System.lineSeparator()));
    }

    private Expression mockExpressionWithOutput(String text) {
        Expression expression = mock(Expression.class);
        when(expression.getOutput()).thenReturn(text);
        return expression;
    }
}