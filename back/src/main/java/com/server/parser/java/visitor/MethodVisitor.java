package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.*;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.context.MethodContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodVisitor extends JavaVisitor<Method> {

    @Override
    public Method visit(ParserRuleContext ctx, JavaContext context) {
        return new MethodVisitorInternal(context).visit(ctx);
    }

    // package-private for tests purpose only
    static class MethodVisitorInternal extends JavaBaseVisitor<Method> {
        private final JavaContext context;

        MethodVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public Method visitMethodDec(JavaParser.MethodDecContext ctx) {
            MethodHeader methodHeader = visit(ctx.methodHeader());
            MethodContext methodContext = new MethodContext(methodHeader.getName());
            context.putMethodWithContext(methodContext);
            MethodBody methodBody = visit(ctx.methodBody());
            return new Method("className", methodHeader, methodBody);
        }

        MethodHeader visit(JavaParser.MethodHeaderContext ctx) {
            List<String> modifiers = ctx.methodModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            String methodResult = ctx.methodResult().getText();
            String identifier = ctx.identifier().getText();
            List<Variable> args = visit(ctx.methodArgs());
            return new MethodHeader(modifiers, methodResult, identifier, args);
        }

        List<Variable> visit(JavaParser.MethodArgsContext ctx) {
            JavaVisitor<Variable> visitor = context.getVisitor(Variable.class);
            return ctx.singleMethodArg().stream()
                    .map(singleMethodArgContext -> visitor.visit(singleMethodArgContext, context))
                    .collect(Collectors.toList());
        }

        MethodBody visit(JavaParser.MethodBodyContext ctx) {
            StatementVisitor statementVisitor = new StatementVisitor();

            List<Statement> statements = new ArrayList<>();
            for (int i = 0; i < ctx.getChildCount(); ++i) {
                ParseTree child = ctx.getChild(i);
                if (child instanceof ParserRuleContext) {
                    statements.add(statementVisitor.visit((ParserRuleContext) child, context));
                }
            }
            return new MethodBody(statements);
        }
    }
}