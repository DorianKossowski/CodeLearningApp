package com.server.parser.util.exception;

public class ResolvingVoidException extends ResolvingException {

    public ResolvingVoidException() {
        super("Niedozowolone wyrażenie typu void");
    }
}