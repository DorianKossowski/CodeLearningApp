package com.server.parser.java.ast.statement;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.expression.Expression;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class IfStatementTest {

    @Test
    void shouldCreateIfStatement() {
        Assignment assignment = new Assignment("", "", mock(Expression.class));

        IfStatement ifStatement = IfStatement.createIf("cond", Collections.singletonList(assignment));

        ExpressionStatement statement = Iterables.getOnlyElement(ifStatement.getExpressionStatements());
        assertThat(statement.getProperty(StatementProperties.IF_CONDITION)).isEqualTo("cond");
    }
}