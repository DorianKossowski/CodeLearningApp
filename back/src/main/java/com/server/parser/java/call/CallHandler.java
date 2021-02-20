package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CallHandler implements Serializable {
    private final CallableKeeper callableKeeper;
    private final CallExecutor callExecutor;
    private final List<CallStatement> printCalls = new ArrayList<>();

    public CallHandler() {
        this(new CallableKeeper(), new CallExecutor());
    }

    public CallHandler(CallableKeeper callableKeeper, CallExecutor callExecutor) {
        this.callableKeeper = Objects.requireNonNull(callableKeeper, "callableKeeper cannot be null");
        this.callExecutor = Objects.requireNonNull(callExecutor, "callExecutor cannot be null");
    }


    public CallableKeeper getCallableKeeper() {
        return callableKeeper;
    }

    public CallStatement execute(CallInvocation invocation) {
        if (isSpecialPrintMethod(invocation.getName())) {
            CallStatement callStatement = callExecutor.executePrintMethod(invocation);
            printCalls.add(callStatement);
            return callStatement;
        }
        if (isSpecificEqualsMethod(invocation)) {
            return callExecutor.executeSpecialEqualsMethod(invocation);
        }
        Method method = callableKeeper.getCallable(invocation);
        return callExecutor.execute(method, invocation);
    }

    private boolean isSpecialPrintMethod(String name) {
        return name.startsWith("System.out.print");
    }

    boolean isSpecificEqualsMethod(CallInvocation callInvocation) {
        CallReference callReference = callInvocation.getCallReference();
        return callReference.getVariable().isPresent() &&
                callReference.getCallName().equals("equals") &&
                callInvocation.getArgs().size() == 1;
    }

    public List<CallStatement> getPrintCalls() {
        return printCalls;
    }
}