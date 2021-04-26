package com.server.parser.java.value;

public interface EqualityCheckable {
    boolean equalsOperator(Value v2);

    boolean equalsMethod(Value v2);
}