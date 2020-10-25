package com.server.parser.java.ast;

public interface Computable extends ConstantProvider {
    Computable compute(Computable v2, String operation);
}