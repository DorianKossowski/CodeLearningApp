package com.server.parser.java.context;

import com.rits.cloning.Cloner;
import com.server.parser.java.ast.FieldVarInitExpressionSupplier;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.call.CallResolver;

public class ContextFactory {
    private static final Class<?>[] EXCLUDED_VALIDATION_CLONING = {FieldVarInitExpressionSupplier.class};
    private static final Class<?>[] EXCLUDED_EXECUTION_CLONING = {FieldVarInitExpressionSupplier.class, CallResolver.class};

    public static JavaContext createValidationContext(JavaContext baseContext) {
        return createCloner(EXCLUDED_VALIDATION_CLONING).deepClone(baseContext);
    }

    private static Cloner createCloner(Class<?>... cloningExcludedClasses) {
        Cloner cloner = new Cloner();
        cloner.dontClone(cloningExcludedClasses);
        return cloner;
    }

    public static JavaContext createStaticExecutionContext(JavaContext baseContext) {
        JavaContext executionContext = createCloner(EXCLUDED_EXECUTION_CLONING).deepClone(baseContext);
        executionContext.setFields(baseContext.getStaticFields());
        return executionContext;
    }

    public static JavaContext createExecutionContext(Instance instance, JavaContext baseContext) {
        JavaContext executionContext = createCloner(EXCLUDED_EXECUTION_CLONING).deepClone(baseContext);
        executionContext.setFields(instance.getFields());
        return executionContext;
    }
}