package com.server.parser.java.visitor.resolver.util;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.IfElseStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.statement.expression_statement.ReturnExprStatement;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ReturnHandlerTest {

    @Test
    void shouldReturn() {
        IfElseStatement ifElseStatement = IfElseStatement.createIf("", new ReturnExprStatement("", mock(Expression.class)));

        assertThat(ReturnHandler.shouldReturn(ifElseStatement)).isTrue();
    }

    @Test
    void shouldNotReturnWhenCallStatement() {
        ReturnExprStatement returnExprStatement = new ReturnExprStatement("", mock(Expression.class));
        CallStatement callStatement = new CallStatement(mock(CallInvocation.class), Collections.singletonList(returnExprStatement));

        assertThat(ReturnHandler.shouldReturn(callStatement)).isFalse();
    }
}