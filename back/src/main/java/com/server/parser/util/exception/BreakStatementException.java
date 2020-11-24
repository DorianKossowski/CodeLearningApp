package com.server.parser.util.exception;

public class BreakStatementException extends ResolvingException {

    public BreakStatementException() {
        super("'break' poza instrukcją switch oraz pętlą");
    }
}