package com.server.parser.java.context;

public interface MethodVerifiable {

    String getMethodName();

    default String getMethodResultType() {
        throw new UnsupportedOperationException();
    }
}