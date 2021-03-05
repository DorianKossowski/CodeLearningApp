package com.server.app.util.output;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;

import java.util.List;
import java.util.stream.Collectors;

public class OutputPreparer {

    public static String prepare(List<PrintCallStatement> printCalls) {
        List<CallInvocation> invocations = printCalls.stream()
                .flatMap(statement -> statement.getExpressionStatements().stream())
                .map(statement -> (CallInvocation) statement)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (CallInvocation invocation : invocations) {
            sb.append(prepareText(invocation));
            if (invocation.getName().equals("System.out.println")) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    private static String prepareText(CallInvocation invocation) {
        Expression expression = Iterables.getOnlyElement(invocation.getArgs());
        return expression.getOutput();
    }
}