package com.server.parser.java.context;

import com.rits.cloning.Cloner;

public class ContextCopyFactory {

    public static JavaContext createValidationContext(JavaContext baseContext) {
        return new Cloner().deepClone(baseContext);
    }
}