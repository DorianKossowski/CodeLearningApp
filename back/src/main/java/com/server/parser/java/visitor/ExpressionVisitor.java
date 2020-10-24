package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.constant.*;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.ObjectRef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.exception.ResolvingException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class ExpressionVisitor extends JavaVisitor<Expression> {

    @Override
    public Expression visit(ParserRuleContext ctx, JavaContext context) {
        return new ExpressionVisitorInternal(context).visit(ctx);
    }

    private static class ExpressionVisitorInternal extends JavaBaseVisitor<Expression> {
        private final JavaContext context;

        public ExpressionVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public Expression visitExpression(JavaParser.ExpressionContext ctx) {
            if (ctx.op == null) {
                return getResolvedUnaryExpr(ctx);
            }
            return getResolvedBinaryExpr(ctx);
        }

        private Expression getResolvedBinaryExpr(JavaParser.ExpressionContext ctx) {
            Constant<?> c1 = visit(ctx.expression(0)).getResolved();
            Constant<?> c2 = visit(ctx.expression(1)).getResolved();
            Constant<?> result = c1.compute(c2, ctx.op.getText());
            return new Literal(result);
        }

        private Expression getResolvedUnaryExpr(JavaParser.ExpressionContext ctx) {
            Expression expression = visit(ctx.exprAtom());
            if (ctx.unOp == null) {
                return expression;
            }
            return ctx.unOp.getText().equals("+") ? expression : createNegatedExpression(expression);
        }

        private Literal createNegatedExpression(Expression expression) {
            Constant<?> constant = expression.getResolved();
            if (constant instanceof IntConstant) {
                return new Literal(new IntConstant((Integer) constant.getValue() * -1));
            }
            if (constant instanceof DoubleConstant) {
                return new Literal(new DoubleConstant((Double) constant.getValue() * -1));
            }
            throw new ResolvingException("Operacja niedostępna dla typu " + constant.getValue().getClass().getSimpleName());
        }

        @Override
        public Expression visitExprAtom(JavaParser.ExprAtomContext ctx) {
            if (ctx.LPAREN() != null) {
                return visit(ctx.expression());
            }
            if (ctx.literal() != null) {
                return visit(ctx.literal());
            }
            if (ctx.objectRefName() != null) {
                return visit(ctx.objectRefName());
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public Expression visitLiteral(JavaParser.LiteralContext ctx) {
            if (ctx.STRING_LITERAL() != null) {
                String value = JavaGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText());
                return new Literal(new StringConstant(value));
            }
            if (ctx.CHAR_LITERAL() != null) {
                char value = ctx.CHAR_LITERAL().getText().charAt(1);
                return new Literal(new CharacterConstant(value));
            }
            if (ctx.INTEGER_LITERAL() != null) {
                int value = Integer.parseInt(ctx.INTEGER_LITERAL().getText().replaceFirst("[lL]", ""));
                return new Literal(new IntConstant(value));
            }
            if (ctx.FLOAT_LITERAL() != null) {
                double value = Double.parseDouble(ctx.FLOAT_LITERAL().getText());
                return new Literal(new DoubleConstant(value));
            }
            if (ctx.BOOLEAN_LITERAL() != null) {
                boolean value = Boolean.parseBoolean(ctx.BOOLEAN_LITERAL().getText());
                return new Literal(new BooleanConstant(value));
            }
            throw new UnsupportedOperationException("Provided literal is not supported");
        }

        @Override
        public Expression visitObjectRefName(JavaParser.ObjectRefNameContext ctx) {
            String text = textVisitor.visit(ctx);
            return new ObjectRef(text, context.getCurrentMethodContext().getValue(text));
        }
    }
}