package com.server.parser.java.value;

public interface Computable extends ConstantProvider {
    Computable compute(Computable v2, String operation);
}