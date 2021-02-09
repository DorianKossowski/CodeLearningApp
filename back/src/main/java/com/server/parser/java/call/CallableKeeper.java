package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.util.exception.ResolvingException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CallableKeeper implements Serializable {
    private final Map<MethodHeader, Method> callableWithContext = new HashMap<>();

    public void keepCallable(Method method) {
        MethodHeader header = method.getHeader();
        if (callableWithContext.containsKey(header)) {
            throw new ResolvingException(String.format("Metoda %s już istnieje", header));
        }
        callableWithContext.put(header, method);
    }

    Map<MethodHeader, Method> getCallablesWithContext() {
        return callableWithContext;
    }
}