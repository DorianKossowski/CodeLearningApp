package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.EmptyExpressionPreparer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VariableDefVisitor extends JavaVisitor<VariableDef> {

    @Override
    public VariableDef visit(ParserRuleContext ctx, JavaContext context) {
        return new CallStatementVisitorInternal(context).visit(ctx);
    }

    private static class CallStatementVisitorInternal extends JavaBaseVisitor<VariableDef> {
        private final JavaContext context;

        private CallStatementVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public VariableDef visitMethodVarDec(JavaParser.MethodVarDecContext ctx) {
            VariableDef variableDef = visit(ctx.varDec());
            List<String> modifiers = ctx.varModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            variableDef.setModifiers(modifiers);
            variableDef.setContextMethodName(context.getMethodName());
            context.addVariable(new Variable(variableDef));
            return variableDef;
        }

        @Override
        public VariableDef visitFieldDec(JavaParser.FieldDecContext ctx) {
            VariableDef variableDef = visit(ctx.varDec());
            List<String> modifiers = ctx.fieldModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            variableDef.setModifiers(modifiers);
            context.addField(new Variable(variableDef));
            return variableDef;
        }

        @Override
        public VariableDef visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
            String type = textVisitor.visit(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
            return new VariableDef(JavaGrammarHelper.getOriginalText(ctx), type, id,
                    EmptyExpressionPreparer.prepare(type), false);
        }

        @Override
        public VariableDef visitVarDec(JavaParser.VarDecContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            String type = textVisitor.visit(ctx.type());
            Expression expression;
            if (ctx.expression() != null) {
                expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            } else {
                expression = ctx.parent instanceof JavaParser.FieldDecContext
                        ? EmptyExpressionPreparer.prepare(type)
                        : EmptyExpressionPreparer.prepareUninitialized(id);
            }
            return new VariableDef(JavaGrammarHelper.getOriginalText(ctx), type, id, expression, ctx.expression() != null);
        }
    }
}