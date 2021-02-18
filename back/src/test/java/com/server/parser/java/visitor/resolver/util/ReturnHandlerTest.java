package com.server.parser.java.visitor.resolver.util;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.IfElseStatement;
import com.server.parser.java.ast.statement.ReturnStatement;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ReturnHandlerTest {

    @Test
    void shouldReturn() {
        IfElseStatement ifElseStatement = IfElseStatement.createIf("", new ReturnStatement("", mock(Expression.class)));

        assertThat(ReturnHandler.shouldReturn(ifElseStatement)).isTrue();
    }

    @Test
    void shouldNotReturnWhenCallStatement() {
        ReturnStatement returnStatement = new ReturnStatement("", mock(Expression.class));
        CallStatement callStatement = new CallStatement(mock(CallInvocation.class), Collections.singletonList(returnStatement));

        assertThat(ReturnHandler.shouldReturn(callStatement)).isFalse();
    }
}