package com.server.app.util;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;

import java.util.List;
import java.util.stream.Collectors;

public class OutputPreparer {

    public static String prepare(Task task) {
        List<CallInvocation> invocations = task.getCalledStatements().stream()
                .flatMap(statement -> statement.getExpressionStatements().stream())
                .filter(statement -> statement instanceof CallInvocation)
                .map(statement -> (CallInvocation) statement)
                .filter(invocation -> invocation.getName().startsWith("System.out.print"))
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