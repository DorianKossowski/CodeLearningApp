package com.server.parser.java.call;

import com.server.parser.java.ast.ConstructorHeader;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.util.TypeCorrectnessChecker;

import java.util.*;
import java.util.function.Predicate;

public class MatchingCallableFinder {
    private final Map<MethodHeader, Method> callableWithContext;

    public MatchingCallableFinder(Map<MethodHeader, Method> callableWithContext) {
        this.callableWithContext = Objects.requireNonNull(callableWithContext, "callableWithContext cannot be null");
    }

    Optional<Method> find(CallReference callReference, List<Expression> invocationArgs) {
        Predicate<MethodHeader> isProperTypeCallable = getCallableTypePredicate(callReference);
        return callableWithContext.entrySet().stream()
                .filter(entry -> isProperTypeCallable.test(entry.getKey()))
                .filter(entry -> isMatchingMethod(callReference.getCallName(), invocationArgs, entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    private Predicate<MethodHeader> getCallableTypePredicate(CallReference callReference) {
        return callReference instanceof ConstructorCallReference ?
                headerClass -> headerClass instanceof ConstructorHeader :
                $ -> true;
    }

    private boolean isMatchingMethod(String invocationName, List<Expression> invocationArguments, MethodHeader header) {
        return hasMatchingName(invocationName, header.getName()) &&
                hasMatchingArguments(invocationArguments, header.getArguments());
    }

    private boolean hasMatchingName(String invocationName, String name) {
        return name.equals(invocationName);
    }

    boolean hasMatchingArguments(List<Expression> invocationArguments, List<VariableDef> arguments) {
        if (arguments.size() != invocationArguments.size()) {
            return false;
        }
        Iterator<VariableDef> argumentsIt = arguments.iterator();
        Iterator<Expression> invocationArgumentsIt = invocationArguments.iterator();
        while (argumentsIt.hasNext()) {
            VariableDef argument = argumentsIt.next();
            Expression invocationArgument = invocationArgumentsIt.next();
            if (!checkSingleArgumentTypesCorrectness(argument, invocationArgument)) {
                return false;
            }
        }
        return true;
    }

    boolean checkSingleArgumentTypesCorrectness(VariableDef argument, Expression invocationArgument) {
        return TypeCorrectnessChecker.isCorrect(argument.getType(), invocationArgument);
    }
}