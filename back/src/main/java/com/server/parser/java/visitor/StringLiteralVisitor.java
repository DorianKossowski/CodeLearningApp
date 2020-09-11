package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;

public class StringLiteralVisitor extends JavaVisitor<String> {

    @Override
    public String visitStringLiteral(JavaParser.StringLiteralContext ctx) {
        if (ctx.STRING_LITERAL() != null) {
            return JavaGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText());
        }
        // CHAR_LITERAL
        return String.valueOf(ctx.CHAR_LITERAL().getText().charAt(1));
    }
}