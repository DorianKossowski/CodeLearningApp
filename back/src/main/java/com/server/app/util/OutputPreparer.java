package com.server.app.util;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.MethodCall;

import java.util.List;
import java.util.stream.Collectors;

public class OutputPreparer {

    public static String prepare(Task task) {
        List<MethodCall> methodCalls = task.getCalledStatements().stream()
                .flatMap(statement -> statement.getExpressionStatements().stream())
                .filter(statement -> statement instanceof MethodCall)
                .map(statement -> (MethodCall) statement)
                .filter(methodCall -> methodCall.getName().startsWith("System.out.print"))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (MethodCall call : methodCalls) {
            sb.append(prepareText(call));
            if (call.getName().equals("System.out.println")) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static String prepareText(MethodCall call) {
        Expression expression = Iterables.getOnlyElement(call.getArgs());
        return expression.getOutput();
    }
}