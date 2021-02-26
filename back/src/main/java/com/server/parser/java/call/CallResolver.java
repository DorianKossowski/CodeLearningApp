package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.executor.MethodCallExecutor;
import com.server.parser.java.call.executor.StaticCallExecutor;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.call.reference.PrintCallReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CallResolver implements Serializable {
    private final CallableKeeper callableKeeper;
    private final MethodCallExecutor methodCallExecutor;
    private final StaticCallExecutor staticCallExecutor;
    private final List<PrintCallStatement> resolvedPrintCalls = new ArrayList<>();

    public CallResolver() {
        this(new CallableKeeper(), new MethodCallExecutor(), new StaticCallExecutor());
    }

    public CallResolver(CallableKeeper callableKeeper, MethodCallExecutor methodCallExecutor,
                        StaticCallExecutor staticCallExecutor) {
        this.callableKeeper = Objects.requireNonNull(callableKeeper, "callableKeeper cannot be null");
        this.methodCallExecutor = Objects.requireNonNull(methodCallExecutor, "callExecutor cannot be null");
        this.staticCallExecutor = Objects.requireNonNull(staticCallExecutor, "staticCallExecutor cannot be null");
    }

    public CallableKeeper getCallableKeeper() {
        return callableKeeper;
    }

    public CallStatement resolve(CallInvocation invocation) {
        if (invocation.getCallReference() instanceof PrintCallReference) {
            PrintCallStatement callStatement = staticCallExecutor.executePrintMethod(invocation);
            resolvedPrintCalls.add(callStatement);
            return callStatement;
        }
        if (isSpecificEqualsMethod(invocation)) {
            return methodCallExecutor.executeSpecialEqualsMethod(invocation);
        }
        Method method = callableKeeper.getCallable(invocation);
        return staticCallExecutor.execute(method, invocation);
    }

    boolean isSpecificEqualsMethod(CallInvocation callInvocation) {
        CallReference callReference = callInvocation.getCallReference();
        return callReference.getVariable().isPresent() &&
                callReference.getCallName().equals("equals") &&
                callInvocation.getArgs().size() == 1;
    }

    public List<PrintCallStatement> getResolvedPrintCalls() {
        return resolvedPrintCalls;
    }
}