package com.server.parser.java.visitor.resolver.util;

import com.google.common.collect.ImmutableSet;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.ast.statement.expression_statement.BreakStatement;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BreakHandler {
    private static final Set<Class<? extends Statement>> breakableStatementClasses =
            ImmutableSet.of(SwitchStatement.class, ForStatement.class, WhileStatement.class, DoWhileStatement.class);

    public static boolean shouldBreak(Statement currentStatement) {
        return getStatementsToFind(currentStatement).stream()
                .flatMap(innerStatement -> innerStatement.getExpressionStatements().stream())
                .anyMatch(expressionStatement -> expressionStatement instanceof BreakStatement);
    }

    private static List<Statement> getStatementsToFind(Statement statement) {
        List<Statement> statements = statement instanceof BlockStatement ?
                ((BlockStatement) statement).getStatements() :
                Collections.singletonList(statement);

        return statements.stream()
                .filter(innerStatement -> !breakableStatementClasses.contains(innerStatement.getClass()))
                .collect(Collectors.toList());
    }
}