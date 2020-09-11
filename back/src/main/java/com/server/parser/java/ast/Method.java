package com.server.parser.java.ast;

import java.util.Objects;

public class Method extends AstElement {
    private final String className;
    private final MethodHeader header;
    private final MethodBody body;

    public Method(String className, MethodHeader header, MethodBody body) {
        this.className = Objects.requireNonNull(className, "className cannot be null");
        this.header = Objects.requireNonNull(header, "header cannot be null");
        this.body = Objects.requireNonNull(body, "body cannot be null");
    }

    public String getClassName() {
        return className;
    }

    public MethodHeader getHeader() {
        return header;
    }

    public MethodBody getBody() {
        return body;
    }
}