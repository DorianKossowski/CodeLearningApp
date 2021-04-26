package com.server.parser.java.call.executor;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.exception.ResolvingException;

import java.util.List;

public class StaticCallExecutor extends CallExecutor {

    @Override
    public CallStatement execute(Method method, CallInvocation invocation) {
        JavaContext executionContext = createStaticExecutionContext(method);
        List<Statement> statements = executeInContext(method, invocation, executionContext);
        Expression returnedExpression = getReturnedExpression(statements);
        validateReturnedExpression(method.getHeader().getResult(), returnedExpression);
        return new CallStatement(invocation, statements, returnedExpression);
    }

    JavaContext createStaticExecutionContext(Method method) {
        return ContextFactory.createStaticExecutionContext(method.getMethodContext());
    }

    public PrintCallStatement executePrintMethod(CallInvocation invocation) {
        int argumentsSize = invocation.getArgs().size();
        checkPrintMethodArguments(invocation.getName(), argumentsSize);
        return new PrintCallStatement(invocation);
    }

    private void checkPrintMethodArguments(String methodName, int argumentsSize) {
        if (argumentsSize != 1) {
            throw new ResolvingException(String.format("Metoda %s musi przyjmować tylko jeden argument (wywołano z %d)",
                    methodName, argumentsSize));
        }
    }
}