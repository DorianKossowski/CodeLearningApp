package com.server.parser.java.visitor.resolver.util;

import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.ReturnStatement;
import com.server.parser.java.ast.statement.Statement;

public class ReturnHandler {

    public static boolean shouldReturn(Statement currentStatement) {
        if (currentStatement instanceof CallStatement) {
            return false;
        }
        return currentStatement.getExpressionStatements().stream()
                .anyMatch(expressionStatement -> expressionStatement instanceof ReturnStatement);
    }
}