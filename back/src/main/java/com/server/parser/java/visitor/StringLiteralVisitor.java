package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;

public class StringLiteralVisitor extends JavaVisitor<String> {

    @Override
    public String visitStringLiteral(JavaParser.StringLiteralContext ctx) {
        if (ctx.STRING_LITERAL() != null) {
            String text = ctx.STRING_LITERAL().getText();
            return text.substring(1, text.length() - 1);
        }
        // CHAR_LITERAL
        return String.valueOf(ctx.CHAR_LITERAL().getText().charAt(1));
    }
}