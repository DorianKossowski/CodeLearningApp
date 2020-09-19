package com.server.parser.java;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.util.List;

public class JavaGrammarHelper {

    public static String createMethodName(List<String> identifiers) {
        return String.join(".", identifiers);
    }

    public static String getFromStringLiteral(String literal) {
        literal = literal.replaceAll("\\\\\"", "\"");
        return literal.substring(1, literal.length() - 1);
    }

    public static String getOriginalText(ParserRuleContext ctx) {
        int start = ctx.start.getStartIndex();
        int stop = ctx.stop.getStopIndex();
        Interval interval = new Interval(start, stop);
        return ctx.start.getInputStream().getText(interval);
    }
}