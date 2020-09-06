package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.ClassBody;
import com.server.parser.java.ast.ClassHeader;

public class ClassDecVisitor extends JavaBaseVisitor<ClassAst> {

    @Override
    public ClassAst visitClassDec(JavaParser.ClassDecContext ctx) {
        ClassHeader header = new ClassHeaderVisitor().visit(ctx.classHeader());
        ClassBody body = new ClassBodyVisitor().visit(ctx.classBody());
        return new ClassAst(header, body);
    }
}