package com.server.parser.java.context;

import com.server.parser.java.call.CallResolver;

import java.util.Objects;

public class ContextParameters {
    private final CallResolver callResolver;
    private final String className;
    private final String methodName;
    private final String methodResultType;
    private final boolean isStaticContext;

    public static ContextParameters createClassContextParameters(String className) {
        return new ContextParameters(new CallResolver(), className, "", null, true);
    }

    public static ContextParameters createMethodContextParameters(ContextParameters classContextParameters,
                                                                  String methodName, String methodResultType, boolean isStaticContext) {
        return new ContextParameters(classContextParameters.getCallResolver(), classContextParameters.getClassName(),
                methodName, methodResultType, isStaticContext);
    }

    private ContextParameters(CallResolver callResolver, String className, String methodName, String methodResultType, boolean isStaticContext) {
        this.callResolver = Objects.requireNonNull(callResolver, "callResolver cannot be null");
        this.className = Objects.requireNonNull(className, "className cannot be null");
        this.methodName = Objects.requireNonNull(methodName, "methodName cannot be null");
        this.methodResultType = methodResultType;
        this.isStaticContext = isStaticContext;
    }

    public CallResolver getCallResolver() {
        return callResolver;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodResultType() {
        if (methodResultType == null) {
            throw new UnsupportedOperationException("Tried to get method result from class context parameters");
        }
        return methodResultType;
    }

    public boolean isStaticContext() {
        return isStaticContext;
    }
}