package com.server.parser.java.ast;

public class Method extends AstElement {
    private final MethodHeader header;
    //    private final MethodBody body;

    public Method(MethodHeader header) {
        this.header = header;
    }

    public MethodHeader getHeader() {
        return header;
    }
}