package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.util.exception.ResolvingException;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CallableKeeper implements Serializable {
    private final Map<MethodHeader, Method> callableWithContext = new HashMap<>();

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
        String name = invocation.getName();
        // TODO handle methods overloading
        // TODO handle static calls
        Optional<Method> callable = callableWithContext.entrySet().stream()
                .filter(entry -> isMatchingMethod(name, entry))
                .map(Map.Entry::getValue)
                .findFirst();
        return callable.orElseThrow(() -> new ResolvingException("Brak pasującej metody dla wywołania: " + invocation.getText()));
    }

    private boolean isMatchingMethod(String name, Map.Entry<MethodHeader, Method> entry) {
        MethodHeader header = entry.getKey();
        return header.getName().equals(name);
    }
}