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
            return callExecutor.callPrintMethod(invocation);
        }
        Method method = callableKeeper.getCallable(invocation);
        return callExecutor.call();
    }

    private boolean isSpecialPrintMethod(String name) {
        return name.startsWith("System.out.print");
    }
}