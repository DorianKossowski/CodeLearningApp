package com.server.parser.util.exception;

public class ResolvingUninitializedException extends ResolvingException {

    public ResolvingUninitializedException(String text) {
        super("Niezainicjalizowana zmienna " + text);
    }
}