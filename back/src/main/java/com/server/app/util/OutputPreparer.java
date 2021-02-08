package com.server.app.util;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Call;

import java.util.List;
import java.util.stream.Collectors;

public class OutputPreparer {

    public static String prepare(Task task) {
        List<Call> calls = task.getCalledStatements().stream()
                .flatMap(statement -> statement.getExpressionStatements().stream())
                .filter(statement -> statement instanceof Call)
                .map(statement -> (Call) statement)
                .filter(methodCall -> methodCall.getName().startsWith("System.out.print"))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (Call call : calls) {
            sb.append(prepareText(call));
            if (call.getName().equals("System.out.println")) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    private static String prepareText(Call call) {
        Expression expression = Iterables.getOnlyElement(call.getArgs());
        return expression.getOutput();
    }
}