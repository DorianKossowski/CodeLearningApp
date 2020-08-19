package com.server.parser.util.exception;

import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

public class PrintableParseException extends RuntimeException {
    private final int lineNumber;

    public PrintableParseException(RecognitionException re) {
        super(print(re));
        lineNumber = re.getOffendingToken().getLine();
    }

    private static String print(RecognitionException re) {
        Token offendingToken = re.getOffendingToken();
        return String.format("Problem podczas parsowania: %s [%s:%s]", offendingToken.getText(),
                offendingToken.getLine(), offendingToken.getCharPositionInLine());
    }

    public int getLineNumber() {
        return lineNumber;
    }
}