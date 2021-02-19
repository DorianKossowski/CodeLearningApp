package com.server.parser.java.context;

import com.rits.cloning.Cloner;
import com.server.parser.java.ast.FieldVarInitExpressionSupplier;

public class ContextCopyFactory {

    public static JavaContext createValidationContext(JavaContext baseContext) {
        return createCloner().deepClone(baseContext);
    }

    private static Cloner createCloner() {
        Cloner cloner = new Cloner();
        cloner.dontClone(FieldVarInitExpressionSupplier.class);
        return cloner;
    }

    public static JavaContext createExecutionContext(JavaContext baseContext) {
        JavaContext executionContext = createCloner().deepClone(baseContext);
        executionContext.setStaticFields(baseContext.getStaticFields());
        return executionContext;
    }
}