package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.SwitchStatement;
import com.server.parser.java.ast.value.PrimitiveValue;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementListVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.util.*;
import java.util.stream.Collectors;

public class SwitchStmtResolver {
    private static final String EXCEPTION_SUFFIX = " nie jest jednego z typów: char, byte, short, int, Character, " +
            "Byte, Short, Integer, String";
    private final JavaContext context;
    private final StatementListVisitor statementListVisitor = new StatementListVisitor();
    private Integer defaultIndex;

    public SwitchStmtResolver(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    public Statement resolve(JavaParser.SwitchStatementContext switchCtx) {
        defaultIndex = null;
        Value value = resolveExpression(switchCtx.expression());
        List<SwitchElement> switchElements = switchCtx.switchElement().stream()
                .map(this::resolveSwitchElement)
                .collect(Collectors.toList());
        List<List<Expression>> switchLabels = switchElements.stream()
                .map(SwitchElement::getLabelExpressions)
                .collect(Collectors.toList());
        validateLabels(switchLabels);
        return new SwitchStatement(resolveStatements(value, switchElements));
    }

    private List<Statement> resolveStatements(Value value, List<SwitchElement> switchElements) {
        Integer fulfilledCaseIndex = null;
        for (int i = 0; i < switchElements.size(); ++i) {
            if (isLabelFulfilled(value, switchElements.get(i))) {
                fulfilledCaseIndex = i;
                break;
            }
        }
        if (fulfilledCaseIndex != null) {
            return resolveStatements(switchElements, fulfilledCaseIndex);
        } else if (defaultIndex != null) {
            return resolveStatements(switchElements, defaultIndex);
        }
        return Collections.emptyList();
    }

    List<Statement> resolveStatements(List<SwitchElement> switchElements, int startIndex) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (int i = startIndex; i < switchElements.size(); ++i) {
            // TODO handle break;
            statements.addAll(switchElements.get(i).getStatements());
        }
        return statements;
    }

    boolean isLabelFulfilled(Value value, SwitchElement switchElement) {
        boolean fulfilled;
        for (Expression labelExpression : switchElement.getLabelExpressions()) {
            if (labelExpression == null) {
                continue;
            }
            Value labelValue = labelExpression.getValue();
            if (value instanceof PrimitiveValue) {
                fulfilled = value.equalsOperator(labelValue);
            } else {
                fulfilled = value.equalsMethod(labelValue);
            }
            if (fulfilled) {
                return true;
            }
        }
        return false;
    }

    void validateLabels(List<List<Expression>> switchLabels) {
        LinkedHashSet<String> distinctLabelExpressionTexts = new LinkedHashSet<>();
        List<String> labelExpressionTexts = new ArrayList<>();
        for (int i = 0; i < switchLabels.size(); ++i) {
            validateLabel(distinctLabelExpressionTexts, labelExpressionTexts, switchLabels.get(i), i);
        }
    }

    private void validateLabel(LinkedHashSet<String> distinctLabelExpressionTexts, List<String> labelExpressionTexts,
                               List<Expression> switchLabel, int switchLabelIndex) {
        for (Expression labelExpression : switchLabel) {
            if (labelExpression == null) {
                validateDefaultLabel(switchLabelIndex);
            } else {
                validateCaseLabel(distinctLabelExpressionTexts, labelExpressionTexts, labelExpression);
            }
        }
    }

    private void validateCaseLabel(LinkedHashSet<String> distinctLabelExpressionTexts, List<String> labelExpressionTexts,
                                   Expression labelExpression) {
//        TODO handle const expressions
//        if (!(labelExpression instanceof Literal)) {
//            throw new ResolvingException("Etykieta case wymaga stałego wyrażenia, którym nie jest: " + labelExpression.getText());
//        }
        String resolvedText = labelExpression.getResolvedText();
        if (distinctLabelExpressionTexts.add(resolvedText)) {
            labelExpressionTexts.add(resolvedText);
        } else {
            throw new ResolvingException(String.format("Zduplikowana etykieta %s w instrukcji switch", resolvedText));
        }
    }

    private void validateDefaultLabel(int switchLabelIndex) {
        if (defaultIndex == null) {
            defaultIndex = switchLabelIndex;
        } else {
            throw new ResolvingException("Zduplikowana etykieta default w instrukcji switch");
        }
    }

    SwitchElement resolveSwitchElement(JavaParser.SwitchElementContext switchElementContext) {
        List<Expression> labelExpressions = resolveLabelExpressions(switchElementContext.switchElementLabel());
        List<Statement> statements = statementListVisitor.visit(switchElementContext.statementList(), context);
        return new SwitchElement(labelExpressions, statements);
    }

    List<Expression> resolveLabelExpressions(List<JavaParser.SwitchElementLabelContext> switchElementLabelContexts) {
        return switchElementLabelContexts.stream().map(switchElementLabelContext -> {
            if (switchElementLabelContext.CASE() != null) {
                return context.getVisitor(Expression.class).visit(switchElementLabelContext.expression(), context);
            }
            return null;
        }).collect(Collectors.toList());
    }

    Value resolveExpression(JavaParser.ExpressionContext expressionContext) {
        Expression expression = context.getVisitor(Expression.class).visit(expressionContext, context);
        Value value = expression.getValue();
        if (!(value instanceof ConstantProvider)) {
            throw new ResolvingException(value.getExpression().getText() + EXCEPTION_SUFFIX);
        }
        Class<?> valueClass = ((ConstantProvider) value).getConstant().c.getClass();
        if (valueClass.equals(Character.class) || valueClass.equals(Integer.class) || valueClass.equals(String.class)) {
            return value;
        }
        throw new ResolvingException(value.getExpression().getText() + EXCEPTION_SUFFIX);
    }

    static class SwitchElement {
        private final List<Expression> labelExpressions;
        private final List<Statement> statements;

        SwitchElement(List<Expression> labelExpressions, List<Statement> statements) {
            this.labelExpressions = Objects.requireNonNull(labelExpressions, "labelExpressions cannot be null");
            this.statements = Objects.requireNonNull(statements, "statements cannot be null");
        }

        public List<Expression> getLabelExpressions() {
            return labelExpressions;
        }

        public List<Statement> getStatements() {
            return statements;
        }
    }
}