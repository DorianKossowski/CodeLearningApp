package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodBody;
import com.server.parser.java.ast.Statement;
import com.server.parser.java.context.MethodContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MethodBodyVisitor extends JavaVisitor<MethodBody> {
    private final MethodContext methodContext;

    public MethodBodyVisitor(MethodContext methodContext) {
        this.methodContext = Objects.requireNonNull(methodContext, "methodContext cannot be null");
    }

    @Override
    public MethodBody visitMethodBody(JavaParser.MethodBodyContext ctx) {
        MethodBodyStatementVisitor methodBodyStatementVisitor = new MethodBodyStatementVisitor(methodContext);
        MethodVarDecVisitor methodVarDecVisitor = new MethodVarDecVisitor(methodContext);

        List<Statement> statements = new ArrayList<>();
        for (int i = 0; i < ctx.getChildCount(); ++i) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof JavaParser.MethodVarDecContext) {
                statements.add(methodVarDecVisitor.visit(child));
            } else if (child instanceof JavaParser.MethodBodyStatementContext) {
                statements.add(methodBodyStatementVisitor.visit(child));
            }
        }
        return new MethodBody(statements);
    }
}