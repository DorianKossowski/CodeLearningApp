package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.context.ContextCopyFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementListVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CallExecutor implements Serializable {
    private final StatementListVisitor visitor;

    CallExecutor() {
        this(new StatementListVisitor());
    }

    CallExecutor(StatementListVisitor visitor) {
        this.visitor = Objects.requireNonNull(visitor, "visitor cannot be null");
    }

    public CallStatement execute(Method method, CallInvocation invocation) {
        JavaContext executionContext = ContextCopyFactory.createExecutionContext(method.getMethodContext());
        List<Statement> statements = visitor.visit(method.getBodyContext(), executionContext);
        return new CallStatement(invocation, statements);
    }

    public CallStatement callPrintMethod(CallInvocation invocation) {
        int argumentsSize = invocation.getArgs().size();
        checkPrintMethodArguments(invocation.getName(), argumentsSize);
        return new CallStatement(invocation, Collections.emptyList());
    }

    private void checkPrintMethodArguments(String methodName, int argumentsSize) {
        if (argumentsSize != 1) {
            throw new ResolvingException(String.format("Metoda %s musi przyjmować tylko jeden argument (wywołano z %d)",
                    methodName, argumentsSize));
        }
    }
}