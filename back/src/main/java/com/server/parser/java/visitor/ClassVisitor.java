package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.ClassBody;
import com.server.parser.java.ast.ClassHeader;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.expression_statement.FieldVarDef;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClassVisitor extends JavaVisitor<ClassAst> {

    @Override
    public ClassAst visit(ParserRuleContext ctx, JavaContext context) {
        return new ClassVisitorInternal((ClassContext) context).visit(ctx);
    }

    // package-private for tests purpose only
    static class ClassVisitorInternal extends JavaBaseVisitor<ClassAst> {
        private final ClassContext context;

        ClassVisitorInternal(ClassContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public ClassAst visitClassDec(JavaParser.ClassDecContext ctx) {
            ClassHeader header = visit(ctx.classHeader());
            context.setName(header.getName());
            ClassBody body = visit(ctx.classBody());
            return new ClassAst(header, body);
        }

        ClassHeader visit(JavaParser.ClassHeaderContext ctx) {
            List<String> modifiers = ctx.classModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            String id = textVisitor.visit(ctx.identifier());
            return new ClassHeader(modifiers, id);
        }

        ClassBody visit(JavaParser.ClassBodyContext ctx) {
            JavaVisitor<VariableDef> variableVisitor = context.getVisitor(VariableDef.class);
            List<FieldVarDef> fields = ctx.fieldDec().stream()
                    .map(fieldDecContext -> (FieldVarDef) variableVisitor.visit(fieldDecContext, context))
                    .collect(Collectors.toList());
            JavaVisitor<Method> methodVisitor = context.getVisitor(Method.class);
            List<Method> constructors = ctx.constructorDec().stream()
                    .map(constructorDecContext -> methodVisitor.visit(constructorDecContext, context.createEmptyMethodContext()))
                    .collect(Collectors.toList());
            List<Method> methods = ctx.methodDec().stream()
                    .map(methodDecContext -> methodVisitor.visit(methodDecContext, context.createEmptyMethodContext()))
                    .collect(Collectors.toList());

            return new ClassBody(fields, constructors, methods);
        }
    }
}
