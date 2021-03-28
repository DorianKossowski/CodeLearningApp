package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.JavaProgramRunner;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.JavaContext;

import java.util.Collections;
import java.util.List;

public class TaskVisitor extends JavaBaseVisitor<Task> {
    private final JavaContext context = new ClassContext();
    private final JavaProgramRunner programRunner = new JavaProgramRunner(context);

    @Override
    public Task visitTaskEOF(JavaParser.TaskEOFContext ctx) {
        return visitTask(ctx.task());
    }

    @Override
    public Task visitTask(JavaParser.TaskContext ctx) {
        ClassAst classAst = context.getVisitor(ClassAst.class, context).visit(ctx.classDec());

        List<Statement> calledStatements = programRunner.run(classAst.getBody().getMethods()).orElse(Collections.emptyList());
        return new Task(classAst, calledStatements, context.getParameters().getCallResolver().getResolvedPrintCalls());
    }
}