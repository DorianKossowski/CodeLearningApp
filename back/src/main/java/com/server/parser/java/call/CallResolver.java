package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.PrintCallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.executor.ConstructorCallExecutor;
import com.server.parser.java.call.executor.MethodCallExecutor;
import com.server.parser.java.call.executor.PrintCallExecutor;
import com.server.parser.java.call.executor.StaticCallExecutor;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.call.reference.ConstructorCallReference;
import com.server.parser.java.call.reference.PrintCallReference;

import java.util.List;
import java.util.Objects;

public class CallResolver {
    private final CallableKeeper callableKeeper;
    private final ConstructorCallExecutor constructorCallExecutor;
    private final MethodCallExecutor methodCallExecutor;
    private final StaticCallExecutor staticCallExecutor;
    private final PrintCallExecutor printCallExecutor;

    public CallResolver() {
        this(new CallableKeeper(), new ConstructorCallExecutor(), new MethodCallExecutor(), new StaticCallExecutor(),
                new PrintCallExecutor());
    }

    public CallResolver(CallableKeeper callableKeeper, ConstructorCallExecutor constructorCallExecutor,
                        MethodCallExecutor methodCallExecutor, StaticCallExecutor staticCallExecutor,
                        PrintCallExecutor printCallExecutor) {
        this.callableKeeper = Objects.requireNonNull(callableKeeper, "callableKeeper cannot be null");
        this.constructorCallExecutor = Objects.requireNonNull(constructorCallExecutor, "constructorCallExecutor cannot be null");
        this.methodCallExecutor = Objects.requireNonNull(methodCallExecutor, "callExecutor cannot be null");
        this.staticCallExecutor = Objects.requireNonNull(staticCallExecutor, "staticCallExecutor cannot be null");
        this.printCallExecutor = Objects.requireNonNull(printCallExecutor, "printCallExecutor cannot be null");
    }

    public CallableKeeper getCallableKeeper() {
        return callableKeeper;
    }

    public CallStatement resolve(boolean isStaticContext, CallInvocation invocation) {
        if (invocation.getCallReference() instanceof PrintCallReference) {
            return printCallExecutor.executePrintMethod(invocation);
        }
        if (isSpecificEqualsMethod(invocation)) {
            return methodCallExecutor.executeSpecialEqualsMethod(invocation);
        }
        Method method = callableKeeper.getCallable(isStaticContext, invocation);
        if (invocation.getCallReference() instanceof ConstructorCallReference) {
            return constructorCallExecutor.execute(method, invocation);
        }
        if (method.getHeader().isStatic()) {
            return staticCallExecutor.execute(method, invocation);
        }
        return methodCallExecutor.execute(method, invocation);
    }

    boolean isSpecificEqualsMethod(CallInvocation callInvocation) {
        CallReference callReference = callInvocation.getCallReference();
        return callReference.getValue().isPresent() &&
                callReference.getCallName().equals("equals") &&
                callInvocation.getArgs().size() == 1;
    }

    public List<PrintCallStatement> getResolvedPrintCalls() {
        return printCallExecutor.getResolvedPrintCalls();
    }
}