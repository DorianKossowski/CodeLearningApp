package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.util.exception.ResolvingException;

import java.util.*;

public class CallableKeeper {
    private final Map<MethodHeader, Method> callableWithContext = new HashMap<>();
    private final MatchingCallableFinder matchingCallableFinder;

    CallableKeeper() {
        this.matchingCallableFinder = new MatchingCallableFinder(callableWithContext);
    }

    CallableKeeper(MatchingCallableFinder matchingCallableFinder) {
        this.matchingCallableFinder = Objects.requireNonNull(matchingCallableFinder, "matchingCallableFinder cannot be null");
    }

    public void keepCallable(Method method) {
        MethodHeader header = method.getHeader();
        if (callableWithContext.containsKey(header)) {
            throw new ResolvingException(String.format("Metoda %s już istnieje", header));
        }
        callableWithContext.put(header, method);
    }

    Collection<Method> getCallables() {
        return callableWithContext.values();
    }

    Method getCallable(boolean staticContext, CallInvocation invocation) {
        List<Expression> invocationArgs = invocation.getArgs();
        Method method = matchingCallableFinder.find(invocation.getCallReference(), invocationArgs)
                .orElseThrow(() -> new ResolvingException("Brak pasującej metody dla wywołania: " + invocation.getText()));
        if (staticContext) {
            validateCallingFromStatic(invocation.getCallReference(), method.getHeader());
        }
        return method;
    }

    private void validateCallingFromStatic(CallReference callReference, MethodHeader methodHeader) {
        if (!callReference.getValue().isPresent() && !methodHeader.isConstructor() && !methodHeader.isStatic()) {
            throw new ResolvingException("Nie można odwołać się do niestatycznej metody ze statycznego kontekstu");
        }
    }
}