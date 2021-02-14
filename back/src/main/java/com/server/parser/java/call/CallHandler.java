package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.CallStatement;

import java.io.Serializable;

public class CallHandler implements Serializable {
    private final CallableKeeper callableKeeper = new CallableKeeper();
    private final CallExecutor callExecutor = new CallExecutor();

    public CallableKeeper getCallableKeeper() {
        return callableKeeper;
    }

    public CallStatement execute(CallInvocation invocation) {
        if (isSpecialPrintMethod(invocation.getName())) {
            return callExecutor.executePrintMethod(invocation);
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
}