package com.server.parser.java.visitor;

import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class JavaVisitor<T> {
    protected static final JavaDefaultTextVisitor textVisitor = new JavaDefaultTextVisitor();

    public abstract T visit(ParserRuleContext ctx, JavaContext context);
}