package com.server.parser.java.visitor.resolver.util;

import com.server.parser.java.ast.statement.BlockStatement;
import com.server.parser.java.ast.statement.ForStatement;
import com.server.parser.java.ast.statement.IfElseStatement;
import com.server.parser.java.ast.statement.expression_statement.BreakExprStatement;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class BreakHandlerTest {

    @Test
    void shouldBreak() {
        IfElseStatement ifElseStatement = IfElseStatement.createIf("", BreakExprStatement.INSTANCE);

        assertThat(BreakHandler.shouldBreak(ifElseStatement)).isTrue();
    }

    @Test
    void shouldBreakInBlock() {
        IfElseStatement ifElseStatement = IfElseStatement.createIf("", BreakExprStatement.INSTANCE);
        BlockStatement blockStatement = new BlockStatement(Collections.singletonList(ifElseStatement));

        assertThat(BreakHandler.shouldBreak(blockStatement)).isTrue();
    }

    @Test
    void shouldNotBreak() {
        ForStatement ifElseStatement = new ForStatement(Collections.singletonList(BreakExprStatement.INSTANCE));

        assertThat(BreakHandler.shouldBreak(ifElseStatement)).isFalse();
    }
}