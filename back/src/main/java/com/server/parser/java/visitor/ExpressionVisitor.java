package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.constant.*;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.Computable;
import com.server.parser.java.value.Value;
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
            if (ctx.orOp != null) {
                return getResolvedOrExpr(ctx);
            }
            if (ctx.andOp != null) {
                return getResolvedAndExpr(ctx);
            }
            if (ctx.eq != null) {
                return getResolvedEqExpr(ctx);
            }
            if (ctx.op != null) {
                return getResolvedBinaryNumberExpr(ctx);
            }
            return getResolvedUnaryExpr(ctx);
        }

        private Expression getResolvedOrExpr(JavaParser.ExpressionContext ctx) {
            Value v1 = visit(ctx.expression(0)).getValue();
            Value v2 = visit(ctx.expression(1)).getValue();
            return new Literal(new BooleanConstant(v1.or(v2)));
        }

        private Expression getResolvedAndExpr(JavaParser.ExpressionContext ctx) {
            Value v1 = visit(ctx.expression(0)).getValue();
            Value v2 = visit(ctx.expression(1)).getValue();
            return new Literal(new BooleanConstant(v1.and(v2)));
        }

        private Expression getResolvedEqExpr(JavaParser.ExpressionContext ctx) {
            Value v1 = visit(ctx.expression(0)).getValue();
            Value v2 = visit(ctx.expression(1)).getValue();
            boolean result = v1.equalsOperator(v2);
            if (ctx.eq.getText().equals("==")) {
                return new Literal(new BooleanConstant(result));
            }
            return new Literal(new BooleanConstant(!result));
        }

        private Expression getResolvedBinaryNumberExpr(JavaParser.ExpressionContext ctx) {
            Value v1 = visit(ctx.expression(0)).getValue();
            Value v2 = visit(ctx.expression(1)).getValue();
            validateValueComputability(v1);
            validateValueComputability(v2);
            Computable computedValue = ((Computable) v1).compute(((Computable) v2), ctx.op.getText());
            return new Literal(computedValue.getConstant());
        }

        private void validateValueComputability(Value v1) {
            if (!(v1 instanceof Computable)) {
                throw new ResolvingException("Operacje matematyczne nie są wspierane dla " + v1.getExpression().getResolvedText());
            }
        }

        private Expression getResolvedUnaryExpr(JavaParser.ExpressionContext ctx) {
            Expression expression = visit(ctx.exprAtom());
            if (ctx.unOp == null) {
                return expression;
            }
            return ctx.unOp.getText().equals("+") ? expression : createNegatedExpression(expression);
        }

        private Literal createNegatedExpression(Expression expression) {
            Constant<?> constant = expression.getLiteral().getConstant();
            if (constant instanceof IntConstant) {
                return new Literal(new IntConstant((Integer) constant.c * -1));
            }
            if (constant instanceof DoubleConstant) {
                return new Literal(new DoubleConstant((Double) constant.c * -1));
            }
            throw new ResolvingException("Operacja niedostępna dla typu " + constant.c.getClass().getSimpleName());
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
            if (ctx.call() != null) {
                return visit(ctx.call());
            }
            if (ctx.nullExpr() != null) {
                return visit(ctx.nullExpr());
            }
            throw new UnsupportedOperationException();
        }

        //*** CALL ***//
        @Override
        public Expression visitCall(JavaParser.CallContext ctx) {
            return context.getVisitor(CallStatement.class).visit(ctx, context).getResult();
        }

        //*** LITERAL **//
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

        //*** OBJECT REF ***//
        @Override
        public Expression visitObjectRefName(JavaParser.ObjectRefNameContext ctx) {
            return context.getVisitor(ObjectRefExpression.class).visit(ctx, context);
        }

        //*** NULL ***//
        @Override
        public Expression visitNullExpr(JavaParser.NullExprContext ctx) {
            return NullExpression.INSTANCE;
        }
    }
}