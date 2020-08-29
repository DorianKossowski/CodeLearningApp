package com.server.parser.java.ast;

import java.util.Objects;

public class Method extends AstElement {
    private final MethodHeader header;
    //    private final MethodBody body;

    public Method(MethodHeader header) {
        this.header = Objects.requireNonNull(header, "header cannot be null");
    }

    public MethodHeader getHeader() {
        return header;
    }
}