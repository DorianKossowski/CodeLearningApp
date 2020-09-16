package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.TaskAst;

public class TaskVisitor extends JavaBaseVisitor<TaskAst> {

    @Override
    public TaskAst visitTaskEOF(JavaParser.TaskEOFContext ctx) {
        return visitTask(ctx.task());
    }

    @Override
    public TaskAst visitTask(JavaParser.TaskContext ctx) {
        ClassAst classAst = new ClassDecVisitor().visit(ctx.classDec());
        return new TaskAst(classAst);
    }
}