package com.server.parser.java.context;

//TODO merge with MethodPrintable
public interface MethodVerifiable {

    String getMethodName();

    default String getMethodResultType() {
        throw new UnsupportedOperationException();
    }
}