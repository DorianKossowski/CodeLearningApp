package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Literal;

public class LiteralVisitor extends JavaVisitor<Literal> {

    @Override
    public Literal visitLiteral(JavaParser.LiteralContext ctx) {
        if (ctx.STRING_LITERAL() != null) {
            String value = JavaGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText());
            return new Literal(value, '"' + value + '"');
        }
        if (ctx.CHAR_LITERAL() != null) {
            char value = ctx.CHAR_LITERAL().getText().charAt(1);
            return new Literal(value, "'" + value + "'");
        }
        if (ctx.INTEGER_LITERAL() != null) {
            int value = Integer.parseInt(ctx.INTEGER_LITERAL().getText());
            return new Literal(value);
        }
        throw new UnsupportedOperationException("Provided literal is not supported");
    }
}