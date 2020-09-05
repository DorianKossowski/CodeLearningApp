package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;

// TODO extends JavaVisitor<Literal>?
public class LiteralVisitor extends JavaVisitor<String> {

    @Override
    public String visitLiteral(JavaParser.LiteralContext ctx) {
        return new StringLiteralVisitor().visit(ctx.stringLiteral());
    }
}