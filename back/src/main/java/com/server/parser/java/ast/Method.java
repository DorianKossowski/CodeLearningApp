package com.server.parser.java.ast;

import com.server.parser.java.JavaParser;
import com.server.parser.java.context.MethodContext;

import java.util.Objects;

public class Method implements AstElement {
    private final MethodContext methodContext;
    private final MethodHeader header;
    private final JavaParser.MethodBodyContext bodyContext;

    public Method(MethodContext methodContext, MethodHeader header, JavaParser.MethodBodyContext bodyContext) {
        this.methodContext = Objects.requireNonNull(methodContext, "methodContext cannot be null");
        this.header = Objects.requireNonNull(header, "header cannot be null");
        this.bodyContext = bodyContext;
    }

    public MethodContext getMethodContext() {
        return methodContext;
    }

    public String getClassName() {
        return methodContext.getClassName();
    }

    public MethodHeader getHeader() {
        return header;
    }

    public JavaParser.MethodBodyContext getBodyContext() {
        return bodyContext;
    }
}