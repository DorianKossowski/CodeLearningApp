package com.server.parser.java.visitor.resolver.util;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.IfElseStatement;
import com.server.parser.java.ast.statement.ReturnStatement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ReturnHandlerTest {

    @Test
    void shouldReturn() {
        IfElseStatement ifElseStatement = IfElseStatement.createIf("", new ReturnStatement("", mock(Expression.class)));

        assertThat(ReturnHandler.shouldReturn(ifElseStatement)).isTrue();
    }
}