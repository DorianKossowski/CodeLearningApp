package com.server.parser.util.exception;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class PrintableParseExceptionErrorStrategy extends BailErrorStrategy {

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        try {
            super.recover(recognizer, e);
        } catch (ParseCancellationException pce) {
            throwPrintableParseException(pce);
        }
    }

    @Override
    public Token recoverInline(Parser recognizer) throws RecognitionException {
        try {
            return super.recoverInline(recognizer);
        } catch (ParseCancellationException pce) {
            throwPrintableParseException(pce);
        }
        return null;
    }

    private static void throwPrintableParseException(ParseCancellationException pce) {
        throw new PrintableParseException((RecognitionException) pce.getCause());
    }
}