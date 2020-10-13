package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.context.JavaContext;

public class TaskVisitor extends JavaBaseVisitor<TaskAst> {
    private final JavaContext context = new JavaContext();

    @Override
    public TaskAst visitTaskEOF(JavaParser.TaskEOFContext ctx) {
        return visitTask(ctx.task());
    }

    @Override
    public TaskAst visitTask(JavaParser.TaskContext ctx) {
        ClassAst classAst = context.getVisitor(ClassAst.class).visit(ctx.classDec(), context);
        return new TaskAst(classAst);
    }
}