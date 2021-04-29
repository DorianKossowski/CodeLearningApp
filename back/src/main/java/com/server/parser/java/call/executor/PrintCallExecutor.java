package com.server.parser.java.call.executor;

import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.util.exception.ResolvingException;

import java.util.ArrayList;
import java.util.List;

public class PrintCallExecutor extends StaticCallExecutor {
    private final List<PrintCallStatement> resolvedPrintCalls = new ArrayList<>();

    public PrintCallStatement executePrintMethod(CallInvocation invocation) {
        int argumentsSize = invocation.getArgs().size();
        checkPrintMethodArguments(invocation.getName(), argumentsSize);
        PrintCallStatement statement = new PrintCallStatement(invocation);
        resolvedPrintCalls.add(statement);
        return statement;
    }

    private void checkPrintMethodArguments(String methodName, int argumentsSize) {
        if (argumentsSize != 1) {
            throw new ResolvingException(String.format("Metoda %s musi przyjmować tylko jeden argument (wywołano z %d)",
                    methodName, argumentsSize));
        }
    }

    public List<PrintCallStatement> getResolvedPrintCalls() {
        return resolvedPrintCalls;
    }
}