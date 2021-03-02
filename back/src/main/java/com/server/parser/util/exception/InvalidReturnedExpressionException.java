package com.server.parser.util.exception;

public class InvalidReturnedExpressionException extends ResolvingException {

    public InvalidReturnedExpressionException(String resolvedExpression, String resultTypeName) {
        super(String.format("Zwracany element %s nie jest typu %s", resolvedExpression, resultTypeName));
    }
}