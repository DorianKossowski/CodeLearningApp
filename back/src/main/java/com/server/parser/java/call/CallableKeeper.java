package com.server.parser.java.call;

import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CallableKeeper implements Serializable {
    private final Map<MethodHeader, MethodContext> callableWithContext = new HashMap<>();

    public void keepCallable(MethodContext methodContext, MethodHeader methodHeader) {
        if (callableWithContext.containsKey(methodHeader)) {
            throw new ResolvingException(String.format("Metoda %s już istnieje", methodHeader));
        }
        callableWithContext.put(methodHeader, methodContext);
    }

    Map<MethodHeader, MethodContext> getCallablesWithContext() {
        return callableWithContext;
    }
}