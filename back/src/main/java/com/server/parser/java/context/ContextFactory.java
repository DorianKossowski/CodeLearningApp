package com.server.parser.java.context;

import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.call.CallResolver;
import com.server.parser.util.ClonerFactory;

public class ContextFactory {
    private static final Class<?>[] EXCLUDED_VALIDATION_CLONING = {};
    private static final Class<?>[] EXCLUDED_EXECUTION_CLONING = {CallResolver.class};

    public static JavaContext createValidationContext(JavaContext baseContext) {
        return ClonerFactory.createCloner(EXCLUDED_VALIDATION_CLONING).deepClone(baseContext);
    }

    public static JavaContext createStaticExecutionContext(JavaContext baseContext) {
        JavaContext executionContext = ClonerFactory.createCloner(EXCLUDED_EXECUTION_CLONING).deepClone(baseContext);
        executionContext.setFields(baseContext.getStaticFields());
        return executionContext;
    }

    public static JavaContext createExecutionContext(Instance instance, JavaContext baseContext) {
        JavaContext executionContext = ClonerFactory.createCloner(EXCLUDED_EXECUTION_CLONING).deepClone(baseContext);
        executionContext.setFields(instance.getFields());
        return executionContext;
    }
}