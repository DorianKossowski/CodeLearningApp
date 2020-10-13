package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.*;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClassVisitor extends JavaVisitor<ClassAst> {

    @Override
    public ClassAst visit(ParserRuleContext ctx, JavaContext context) {
        return new ClassVisitorInternal(context).visit(ctx);
    }

    // package-private for tests purpose only
    static class ClassVisitorInternal extends JavaBaseVisitor<ClassAst> {
        private final JavaContext context;

        ClassVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public ClassAst visitClassDec(JavaParser.ClassDecContext ctx) {
            ClassHeader header = visit(ctx.classHeader());
            context.setCurrentClassName(header.getName());
            ClassBody body = visit(ctx.classBody());
            return new ClassAst(header, body);
        }

        ClassHeader visit(JavaParser.ClassHeaderContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            return new ClassHeader(id);
        }

        ClassBody visit(JavaParser.ClassBodyContext ctx) {
            JavaVisitor<Variable> variableVisitor = context.getVisitor(Variable.class);
            List<Variable> fields = ctx.fieldDec().stream()
                    .map(fieldDecContext -> variableVisitor.visit(fieldDecContext, context))
                    .collect(Collectors.toList());
            JavaVisitor<Method> methodVisitor = context.getVisitor(Method.class);
            List<Method> methods = ctx.methodDec().stream()
                    .map(methodDecContext -> methodVisitor.visit(methodDecContext, context))
                    .collect(Collectors.toList());

            return new ClassBody(fields, methods);
        }
    }
}
