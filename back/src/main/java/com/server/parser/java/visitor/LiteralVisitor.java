package com.server.parser.java.visitor;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Literal;

public class LiteralVisitor extends JavaVisitor<Literal> {

    @Override
    public Literal visitLiteral(JavaParser.LiteralContext ctx) {
        return new Literal(getValue(ctx));
    }

    private Object getValue(JavaParser.LiteralContext ctx) {
        if (ctx.STRING_LITERAL() != null) {
            return JavaGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText());
        }
        if (ctx.CHAR_LITERAL() != null) {
            return ctx.CHAR_LITERAL().getText().charAt(1);
        }
        throw new UnsupportedOperationException("Provided literal is not supported");
    }
}