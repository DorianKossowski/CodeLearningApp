package com.server.parser.java.ast;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.MethodCall;
import com.server.parser.java.ast.statement.Statement;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskTest {

    @Test
    void shouldGetOutput() {
        // given
        MethodCall call1 = mock(MethodCall.class);
        when(call1.getName()).thenReturn("System.out.println");
        Expression text1 = mockExpressionWithResolvedText("TEXT");
        when(call1.getArgs()).thenReturn(Collections.singletonList(text1));

        MethodCall call2 = mock(MethodCall.class);
        when(call2.getName()).thenReturn("System.out.print");
        Expression text2 = mockExpressionWithResolvedText("SOME ");
        when(call2.getArgs()).thenReturn(Collections.singletonList(text2));

        MethodCall call3 = mock(MethodCall.class);
        when(call3.getName()).thenReturn("someMethod");

        MethodCall call4 = mock(MethodCall.class);
        when(call4.getName()).thenReturn("System.out.println");
        Expression text4 = mockExpressionWithResolvedText("TEXT2");
        when(call4.getArgs()).thenReturn(Collections.singletonList(text4));

        List<Statement> stmts = Arrays.asList(call1, call2, call3, call4);
        Task task = new Task(mock(ClassAst.class), stmts);

        // then
        assertThat(task.getOutput()).isEqualTo("TEXT\nSOME TEXT2\n");
    }

    private Expression mockExpressionWithResolvedText(String text) {
        Expression expression = mock(Expression.class);
        when(expression.getResolvedText()).thenReturn(text);
        return expression;
    }
}