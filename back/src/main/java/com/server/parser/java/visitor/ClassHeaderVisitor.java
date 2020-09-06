package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassHeader;

public class ClassHeaderVisitor extends JavaVisitor<ClassHeader> {

    @Override
    public ClassHeader visitClassHeader(JavaParser.ClassHeaderContext ctx) {
        String id = textVisitor.visit(ctx.identifier());
        return new ClassHeader(id);
    }
}