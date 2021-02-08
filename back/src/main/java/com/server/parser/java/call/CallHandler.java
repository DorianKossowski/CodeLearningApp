package com.server.parser.java.call;

import java.io.Serializable;

public class CallHandler implements Serializable {
    private final CallableKeeper callableKeeper = new CallableKeeper();

    public CallableKeeper getCallableKeeper() {
        return callableKeeper;
    }
}