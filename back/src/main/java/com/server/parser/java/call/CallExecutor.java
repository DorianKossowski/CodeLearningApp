package com.server.parser.java.call;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.context.ContextCopyFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementListVisitor;
import com.server.parser.util.TypeCorrectnessChecker;
import com.server.parser.util.exception.ResolvingException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CallExecutor implements Serializable {
    static final int MAX_EXECUTION_LEVEL = 10;
    private int executionLevel = 0;
    private final StatementListVisitor visitor;

    CallExecutor() {
        this(new StatementListVisitor());
    }

    CallExecutor(StatementListVisitor visitor) {
        this.visitor = Objects.requireNonNull(visitor, "visitor cannot be null");
    }

    public CallStatement execute(Method method, CallInvocation invocation) {
        preExecution();
        JavaContext executionContext = ContextCopyFactory.createExecutionContext(method.getMethodContext());
        assignInvocationParameters(method.getHeader().getArguments(), invocation.getArgs(), executionContext);
        List<Statement> statements = visitor.visit(method.getBodyContext(), executionContext);
        Expression returnedExpression = getReturnedExpression(statements);
        validateReturnedExpression(method.getHeader().getResult(), returnedExpression);
        return postExecution(invocation, statements, returnedExpression);
    }

    Expression getReturnedExpression(List<Statement> statements) {
        Optional<ReturnStatement> optionalReturnStatement = statements.stream()
                .flatMap(statement -> statement.getExpressionStatements().stream())
                .filter(statement -> statement instanceof ReturnStatement)
                .map(statement -> (ReturnStatement) statement)
                .findFirst();
        return optionalReturnStatement.map(ReturnStatement::getExpression)
                .orElse(VoidExpression.INSTANCE);
    }

    void validateReturnedExpression(String resultTypeName, Expression returnedExpression) {
        if (!TypeCorrectnessChecker.isCorrect(resultTypeName, returnedExpression)) {
            throw new ResolvingException(String.format("Zwracany element %s nie jest typu %s",
                    returnedExpression.getResolvedText(), resultTypeName));
        }
    }

    void assignInvocationParameters(List<VariableDef> arguments, List<Expression> invocationParameters, JavaContext executionContext) {
        List<String> argumentsNames = arguments.stream()
                .map(VariableDef::getName)
                .collect(Collectors.toList());
        Streams.forEachPair(argumentsNames.stream(), invocationParameters.stream(), executionContext::updateVariable);
    }

    void preExecution() {
        if (executionLevel > MAX_EXECUTION_LEVEL) {
            throw new ResolvingException("Przekroczono ilość dopuszczalnych zagnieżdżonych wywołań równą: " + MAX_EXECUTION_LEVEL);
        }
        ++executionLevel;
    }

    private CallStatement postExecution(CallInvocation invocation, List<Statement> statements, Expression returnedExpression) {
        --executionLevel;
        return new CallStatement(invocation, statements, returnedExpression);
    }

    public CallStatement executePrintMethod(CallInvocation invocation) {
        int argumentsSize = invocation.getArgs().size();
        checkPrintMethodArguments(invocation.getName(), argumentsSize);
        return new CallStatement(invocation, Collections.emptyList());
    }

    private void checkPrintMethodArguments(String methodName, int argumentsSize) {
        if (argumentsSize != 1) {
            throw new ResolvingException(String.format("Metoda %s musi przyjmować tylko jeden argument (wywołano z %d)",
                    methodName, argumentsSize));
        }
    }

    public CallStatement executeSpecialEqualsMethod(CallInvocation invocation) {
        Variable variable = invocation.getCallReference().getVariable()
                .orElseThrow(() -> new IllegalArgumentException("Should provide variable"));
        Expression argument = Iterables.getOnlyElement(invocation.getArgs());

        boolean areEqual = variable.getValue().equalsMethod(argument.getValue());
        Literal result = new Literal(new BooleanConstant(areEqual));
        return new CallStatement(invocation, Collections.emptyList(), result);
    }
}