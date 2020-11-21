package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodBody;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.JavaContext;
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
            context.createCurrentMethodContext(methodHeader.getName());
            MethodBody methodBody = visit(ctx.methodBody());
            return new Method(context.getCurrentClassName(), methodHeader, methodBody);
        }

        MethodHeader visit(JavaParser.MethodHeaderContext ctx) {
            List<String> modifiers = ctx.methodModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            String methodResult = ctx.methodResult().getText();
            String identifier = ctx.identifier().getText();
            List<VariableDef> args = visit(ctx.methodArgs());
            return new MethodHeader(modifiers, methodResult, identifier, args);
        }

        List<VariableDef> visit(JavaParser.MethodArgsContext ctx) {
            JavaVisitor<VariableDef> visitor = context.getVisitor(VariableDef.class);
            return ctx.singleMethodArg().stream()
                    .map(singleMethodArgContext -> visitor.visit(singleMethodArgContext, context))
                    .collect(Collectors.toList());
        }

        MethodBody visit(JavaParser.MethodBodyContext ctx) {
            StatementVisitor statementVisitor = new StatementVisitor();

            List<Statement> statements = new ArrayList<>();
            JavaParser.StatementListContext statementListContext = ctx.statementList();
            for (int i = 0; i < statementListContext.getChildCount(); ++i) {
                ParseTree child = statementListContext.getChild(i);
                if (child instanceof ParserRuleContext) {
                    statements.add(statementVisitor.visit((ParserRuleContext) child, context));
                }
            }
            return new MethodBody(statements);
        }
    }
}