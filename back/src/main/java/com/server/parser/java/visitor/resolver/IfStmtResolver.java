package com.server.parser.java.visitor.resolver;

import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.exception.ResolvingException;

public class IfStmtResolver {

    public boolean resolveCondition(Expression condition) {
        Value value = condition.getValue();
        if (value instanceof ConstantProvider && ((ConstantProvider) value).getConstant().c instanceof Boolean) {
            return (Boolean) ((ConstantProvider) value).getConstant().c;
        }
        throw new ResolvingException(value.getExpression().getText() + " nie jest typu logicznego");
    }
}