package com.server.parser.java.context;

import com.rits.cloning.Cloner;

public class ContextCopyFactory {

    public static JavaContext createValidationContext(JavaContext baseContext) {
        return new Cloner().deepClone(baseContext);
    }

    public static JavaContext createExecutionContext(JavaContext baseContext) {
        JavaContext executionContext = new Cloner().deepClone(baseContext);
        executionContext.setStaticFields(baseContext.getStaticFields());
        return executionContext;
    }
}