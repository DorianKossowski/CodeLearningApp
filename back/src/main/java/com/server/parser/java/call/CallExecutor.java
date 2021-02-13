package com.server.parser.java.call;

import com.google.common.collect.Streams;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.ContextCopyFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementListVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CallExecutor implements Serializable {
    static final int MAX_EXECUTION_LEVEL = 10;
    private int executionLevel = 0;
    private final StatementListVisitor visitor;

    CallExecutor() {
        this(new StatementListVisitor());
    }

    CallExecutor(StatementListVisitor visitor) {
        this.visitor = Objects.requireNonNull(visitor, "visitor cannot be null");
    }

    public CallStatement execute(Method method, CallInvocation invocation) {
        prepareForNextExecution();
        JavaContext executionContext = ContextCopyFactory.createExecutionContext(method.getMethodContext());
        assignInvocationParameters(method.getHeader().getArguments(), invocation.getArgs(), executionContext);
        List<Statement> statements = visitor.visit(method.getBodyContext(), executionContext);
        --executionLevel;
        return new CallStatement(invocation, statements);
    }

    void assignInvocationParameters(List<VariableDef> arguments, List<Expression> invocationParameters, JavaContext executionContext) {
        List<String> argumentsNames = arguments.stream()
                .map(VariableDef::getName)
                .collect(Collectors.toList());
        Streams.forEachPair(argumentsNames.stream(), invocationParameters.stream(), executionContext::updateVariable);
    }

    void prepareForNextExecution() {
        if (executionLevel > MAX_EXECUTION_LEVEL) {
            throw new ResolvingException("Przekroczono ilość dopuszczalnych zagnieżdżonych wywołań równą: " + MAX_EXECUTION_LEVEL);
        }
        ++executionLevel;
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