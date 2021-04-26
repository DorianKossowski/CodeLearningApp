package com.server.parser.java.ast;

import java.util.Objects;

public class ClassAst implements AstElement {
    private final ClassHeader header;
    private final ClassBody body;

    public ClassAst(ClassHeader header, ClassBody body) {
        this.header = Objects.requireNonNull(header, "header cannot be null");
        this.body = Objects.requireNonNull(body, "body cannot be null");
    }

    public ClassHeader getHeader() {
        return header;
    }

    public ClassBody getBody() {
        return body;
    }
}