package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.util.exception.ResolvingException;

import java.io.Serializable;
import java.util.*;

public class CallableKeeper implements Serializable {
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

    Method getCallable(CallInvocation invocation) {
        // TODO handle static calls
        String name = invocation.getName();
        List<Expression> invocationArgs = invocation.getArgs();
        return matchingCallableFinder.find(name, invocationArgs)
                .orElseThrow(() -> new ResolvingException("Brak pasującej metody dla wywołania: " + invocation.getText()));
    }
}