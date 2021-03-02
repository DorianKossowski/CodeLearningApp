package com.server.parser.java.ast.statement;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.expression_statement.Assignment;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class IfElseStatementTest {

    @Test
    void shouldCreateIfStatement() {
        Assignment assignment = new Assignment("", "", mock(Expression.class));

        IfElseStatement ifElseStatement = IfElseStatement.createIf("cond", assignment);

        ExpressionStatement statement = Iterables.getOnlyElement(ifElseStatement.getExpressionStatements());
        assertThat(statement.getProperty(StatementProperties.IF_CONDITION)).isEqualTo("cond");
    }

    @Test
    void shouldCreateElseStatement() {
        Assignment assignment = new Assignment("", "", mock(Expression.class));

        IfElseStatement ifElseStatement = IfElseStatement.createElse(assignment);

        ExpressionStatement statement = Iterables.getOnlyElement(ifElseStatement.getExpressionStatements());
        assertThat(statement.getProperty(StatementProperties.IN_ELSE)).isEqualTo("true");
    }

    @Test
    void shouldCreateElseStatementWithMoreLevels() {
        Assignment assignment = new Assignment("", "", mock(Expression.class));
        IfElseStatement insideIf = IfElseStatement.createIf("", assignment);

        IfElseStatement ifElseStatement = IfElseStatement.createElse(insideIf);

        ExpressionStatement statement = Iterables.getOnlyElement(ifElseStatement.getExpressionStatements());
        assertThat(statement.getProperty(StatementProperties.IN_ELSE)).isEqualTo("true");
    }
}