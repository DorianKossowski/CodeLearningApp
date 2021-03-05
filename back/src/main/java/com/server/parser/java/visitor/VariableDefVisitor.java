package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.expression_statement.ArgumentVarDef;
import com.server.parser.java.ast.statement.expression_statement.FieldVarDef;
import com.server.parser.java.ast.statement.expression_statement.MethodVarDef;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.util.ValueType;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.FieldVarInitExpressionFunction;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.util.EmptyExpressionPreparer;
import com.server.parser.util.exception.ResolvingException;
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
            variableDef.setContextMethodName(context.getParameters().getMethodName());
            context.addVariable(new MethodVar(variableDef));
            return variableDef;
        }

        @Override
        public VariableDef visitFieldDec(JavaParser.FieldDecContext ctx) {
            FieldVarDef variableDef = (FieldVarDef) visit(ctx.varDec());
            List<String> modifiers = ctx.fieldModifier().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList());
            variableDef.setModifiers(modifiers);
            ((ClassContext) context).addField(new FieldVar(variableDef));
            return variableDef;
        }

        @Override
        public VariableDef visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
            String type = visitValidType(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
            return new ArgumentVarDef(JavaGrammarHelper.getOriginalText(ctx), type, id);
        }

        private String visitValidType(JavaParser.TypeContext typeContext) {
            String type = textVisitor.visit(typeContext);
            validateType(type.replace("[]", ""));
            return type;
        }

        private void validateType(String type) {
            if (type.equals(context.getParameters().getClassName())) {
                return;
            }
            if (ValueType.findByOriginalType(type) == ValueType.GENERIC) {
                throw new ResolvingException("Użyto nieznanego typu: " + type);
            }
        }

        @Override
        public VariableDef visitVarDec(JavaParser.VarDecContext ctx) {
            String type = visitValidType(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
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
            FieldVarInitExpressionFunction function = createInitExpressionFunction(type, ctx);
            return new FieldVarDef(JavaGrammarHelper.getOriginalText(ctx), type, id, function, ctx.expression() != null);
        }

        private FieldVarInitExpressionFunction createInitExpressionFunction(String type, JavaParser.VarDecContext ctx) {
            if (ctx.expression() == null) {
                return new FieldVarInitExpressionFunction("", (context) -> EmptyExpressionPreparer.prepare(type));
            }
            return new FieldVarInitExpressionFunction(ctx.expression().getText(),
                    (context) -> context.getVisitor(Expression.class).visit(ctx.expression(), context)
            );
        }
    }
}