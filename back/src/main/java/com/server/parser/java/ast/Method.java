package com.server.parser.java.ast;

import java.util.Objects;

public class Method extends AstElement {
    private final MethodHeader header;
    private final MethodBody body;

    public Method(MethodHeader header, MethodBody body) {
        this.header = Objects.requireNonNull(header, "header cannot be null");
        this.body = Objects.requireNonNull(body, "body cannot be null");
    }

    public MethodHeader getHeader() {
        return header;
    }

    public MethodBody getBody() {
        return body;
    }
}