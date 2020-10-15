package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Literal;
import com.server.parser.java.ast.ObjectRef;
import com.server.parser.java.context.JavaContext;
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
        public Expression visitExprAtom(JavaParser.ExprAtomContext ctx) {
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
                return new Literal(value, '"' + value + '"');
            }
            if (ctx.CHAR_LITERAL() != null) {
                char value = ctx.CHAR_LITERAL().getText().charAt(1);
                return new Literal(value, "'" + value + "'");
            }
            if (ctx.INTEGER_LITERAL() != null) {
                int value = Integer.parseInt(ctx.INTEGER_LITERAL().getText().replaceFirst("[lL]", ""));
                return new Literal(value);
            }
            if (ctx.FLOAT_LITERAL() != null) {
                double value = Double.parseDouble(ctx.FLOAT_LITERAL().getText());
                return new Literal(value);
            }
            if (ctx.BOOLEAN_LITERAL() != null) {
                boolean value = Boolean.parseBoolean(ctx.BOOLEAN_LITERAL().getText());
                return new Literal(value);
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