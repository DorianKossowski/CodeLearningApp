package com.server.parser.java.context;

import com.server.parser.java.call.CallResolver;

import java.util.Objects;

public class ContextParameters {
    private final CallResolver callResolver = new CallResolver();
    private final String className;
    private final String methodName;
    private final boolean isStaticContext;

    public static ContextParameters createClassContextParameters(String className) {
        return new ContextParameters(className, "", true);
    }

    public static ContextParameters createMethodContextParameters(ContextParameters classContextParameters,
                                                                  String methodName, boolean isStaticContext) {
        return new ContextParameters(classContextParameters.getClassName(), methodName, isStaticContext);
    }

    private ContextParameters(String className, String methodName, boolean isStaticContext) {
        this.className = Objects.requireNonNull(className, "className cannot be null");
        this.methodName = Objects.requireNonNull(methodName, "methodName cannot be null");
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

    public boolean isStaticContext() {
        return isStaticContext;
    }
}