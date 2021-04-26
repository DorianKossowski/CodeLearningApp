package com.server.app.util.output;

import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
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
        PrintCallStatement call1 = new PrintCallStatement(invocation1);

        CallInvocation invocation2 = mockCallInvocation("System.out.print", "SOME ");
        PrintCallStatement call2 = new PrintCallStatement(invocation2);

        CallInvocation invocation3 = mockCallInvocation("someMethod", "");
        PrintCallStatement call3 = new PrintCallStatement(invocation3);

        CallInvocation invocation4 = mockCallInvocation("System.out.println", "TEXT2");
        PrintCallStatement call4 = new PrintCallStatement(invocation4);

        List<PrintCallStatement> stmts = Arrays.asList(call1, call2, call3, call4);
        Task task = new Task(mock(ClassAst.class), Collections.emptyList(), stmts);

        // then
        assertThat(OutputPreparer.prepare(task.getPrintCalls()))
                .isEqualTo(String.format("TEXT%sSOME TEXT2%s", System.lineSeparator(), System.lineSeparator()));
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