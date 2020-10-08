package com.server.parser.util.exception;

public class ResolvingException extends RuntimeException {

    public ResolvingException(String message) {
        super("Problem podczas rozwiązywania: " + message);
    }
}