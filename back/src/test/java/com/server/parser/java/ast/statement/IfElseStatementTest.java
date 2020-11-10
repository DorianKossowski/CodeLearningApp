package com.server.parser.java.ast.statement;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.expression.Expression;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class IfElseStatementTest {

    @Test
    void shouldCreateIfStatement() {
        Assignment assignment = new Assignment("", "", mock(Expression.class));

        IfElseStatement ifElseStatement = IfElseStatement.createIf("cond", Collections.singletonList(assignment));

        ExpressionStatement statement = Iterables.getOnlyElement(ifElseStatement.getExpressionStatements());
        assertThat(statement.getProperty(StatementProperties.IF_CONDITION)).isEqualTo("cond");
    }

    @Test
    void shouldCreateElseStatement() {
        Assignment assignment = new Assignment("", "", mock(Expression.class));

        IfElseStatement ifElseStatement = IfElseStatement.createElse(Collections.singletonList(assignment));

        ExpressionStatement statement = Iterables.getOnlyElement(ifElseStatement.getExpressionStatements());
        assertThat(statement.getProperty(StatementProperties.IN_ELSE)).isEqualTo("true");
    }

    @Test
    void shouldCreateElseStatementWithMoreLevels() {
        Assignment assignment = new Assignment("", "", mock(Expression.class));
        IfElseStatement insideIf = IfElseStatement.createIf("", Collections.singletonList(assignment));

        IfElseStatement ifElseStatement = IfElseStatement.createElse(Collections.singletonList(insideIf));

        ExpressionStatement statement = Iterables.getOnlyElement(ifElseStatement.getExpressionStatements());
        assertThat(statement.getProperty(StatementProperties.IN_ELSE)).isEqualTo("true");
    }
}