package com.server.parser.java.ast.value;

public interface LogicOperational {
    boolean and(Value v2);

    boolean or(Value v2);
}