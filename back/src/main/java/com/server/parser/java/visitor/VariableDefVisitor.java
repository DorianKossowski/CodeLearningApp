package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.expression_statement.FieldVarDef;
import com.server.parser.java.ast.statement.expression_statement.MethodVarDef;
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
            MethodVarDef variableDef = (MethodVarDef) visit(ctx.varDec());
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
            FieldVarDef variableDef = (FieldVarDef) visit(ctx.varDec());
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
            // TODO ArgumentVarDef?
            return new MethodVarDef(JavaGrammarHelper.getOriginalText(ctx), type, id,
                    EmptyExpressionPreparer.prepare(type), false);
        }

        @Override
        public VariableDef visitVarDec(JavaParser.VarDecContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            String type = textVisitor.visit(ctx.type());
            if (ctx.parent instanceof JavaParser.FieldDecContext) {
                return createFieldVarDef(id, type, ctx);
            } else if (ctx.parent instanceof JavaParser.MethodVarDecContext) {
                return createMethodVarDef(id, type, ctx);
            }
            throw new UnsupportedOperationException(String.format("Variable creation from %s not supported",
                    ctx.parent.getClass().getSimpleName()));
        }

        private MethodVarDef createMethodVarDef(String id, String type, JavaParser.VarDecContext ctx) {
            Expression expression = ctx.expression() != null ?
                    context.getVisitor(Expression.class).visit(ctx.expression(), context) :
                    EmptyExpressionPreparer.prepareUninitialized(id);
            return new MethodVarDef(JavaGrammarHelper.getOriginalText(ctx), type, id, expression,
                    ctx.expression() != null);
        }

        private FieldVarDef createFieldVarDef(String id, String type, JavaParser.VarDecContext ctx) {
            Expression expression = ctx.expression() != null ?
                    context.getVisitor(Expression.class).visit(ctx.expression(), context) :
                    EmptyExpressionPreparer.prepare(type);
            return new FieldVarDef(JavaGrammarHelper.getOriginalText(ctx), type, id, expression,
                    ctx.expression() != null);
        }
    }
}