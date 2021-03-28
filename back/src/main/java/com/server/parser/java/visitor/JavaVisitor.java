package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;

public abstract class JavaVisitor<T> extends JavaBaseVisitor<T> {
    protected static final JavaDefaultTextVisitor textVisitor = new JavaDefaultTextVisitor();
}